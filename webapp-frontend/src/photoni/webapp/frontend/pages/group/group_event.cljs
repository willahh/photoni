(ns photoni.webapp.frontend.pages.group.group-event
  (:require [reagent.core :as r]
            [re-frame.core :as re-frame :refer [subscribe dispatch reg-event-fx reg-event-db reg-sub reg-cofx]]
            [cljs.core.async :refer [<!]]
            [photoni.webapp.frontend.events :as events]
            [photoni.webapp.domain.group.group-service :as group-service]
            [photoni.webapp.domain.group.group :as group]
            [photoni.webapp.domain.group.group-repository :as group-repository]
            [photoni.webapp.frontend.pages.group.group-service :refer [group-service-repo]]
            [photoni.webapp.frontend.pages.group.group-db :as group-db])
  (:require-macros [cljs.core.async.macros :refer [go]]))


(reg-event-db
  :group.event/clear-upsert-group-row
  (fn [db _]
    (-> db
        (assoc-in group-db/path-upsert-group-row {})
        (assoc-in group-db/path-upsert-errors nil))))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ get-groups [sub]                                                           │
;; └───────────────────────────────────────────────────────────────────────────┘
(reg-event-fx
  :group.event/fetch-groups
  (fn [{:keys [db]} _]
    (go (try
          (let [{:keys [success body] :as response}
                (<! (group-service/get-groups group-service-repo {}))
                group-entities body]
            (if success
              (dispatch [:group.event/fetch-groups-success group-entities])
              (dispatch [:group.event/fetch-groups-failure response])))
          (catch js/Error e
            (dispatch [:group.event/fetch-groups-failure {:error e}]))))
    {:db (-> db
             (assoc-in group-db/path-groups-loading? true))}))

(reg-event-db
  :group.event/fetch-groups-success
  (fn [db [_ groups]]
    (-> db
        (assoc-in group-db/path-groups groups)
        (assoc-in group-db/path-groups-loading? false))))

(reg-event-db
  :group.event/fetch-groups-failure
  (fn [db [_ response]]
    (js/console.error "fetch-groups-failure" response)
    db))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ get-group                                                                  │
;; └───────────────────────────────────────────────────────────────────────────┘
(reg-event-fx
  :group.event/fetch-group
  (fn [{:keys [db]} [_ group-id]]
    (go (try
          (let [{:keys [success body] :as response}
                (<! (group-service/get-group-by-group-id
                      group-service-repo
                      (group/get-group-by-id-query group-id)))
                group-entity body]
            (if success
              (dispatch [:group.event/fetch-group-success group-entity])
              (dispatch [:group.event/fetch-group-failure response])))
          (catch js/Error e
            (dispatch [:group.event/fetch-group-failure {:error e}]))))
    {:db (-> db
             (assoc-in group-db/path-upsert-group-row {})
             (assoc-in group-db/path-upsert-status :form.status/loading))}))

(reg-event-db
  :group.event/fetch-group-success
  (fn [db [_ group]]
    (-> db
        (assoc-in group-db/path-upsert-group-row group)
        (assoc-in group-db/path-upsert-status :form.status/default))))

(reg-event-db
  :group.event/fetch-group-failure
  (fn [db [_ response]]
    (js/console.error "fetch-groups-failure" response)
    (-> db
        (assoc-in group-db/path-upsert-group-row {})
        (assoc-in group-db/path-upsert-status :form.status/default))))




;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ find-groups [event]                                                           │
;; └───────────────────────────────────────────────────────────────────────────┘
;(reg-event-db
;  :initialise-db-with-dt
;  (fn [db [_ stored-db]]
;    (assoc db :groups (db-group/find-all-groups))))





;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ upsert-group                                                               │
;; └───────────────────────────────────────────────────────────────────────────┘
(reg-event-fx
  :group.event/upsert-group
  (fn [{:keys [db]} [_ group-fields]]
    (let []
      (go (try
            (let [{:keys [success body] :as response}
                  (<! (group-service/create-group group-service-repo
                                                (group/create-group-command group-fields)))
                  group-entity body]
              (if success
                (dispatch [:group.event/upsert-group-success group-entity])
                (dispatch [:group.event/upsert-group-failure response])))
            (catch js/Error e
              (dispatch [:group.event/upsert-group-failure {:error e}])))))
    {:db (-> db
             (assoc-in group-db/path-upsert-status :form.status/processing))}))

(reg-event-fx
  :group.event/upsert-group-success
  (fn [{:keys [db]} [_ group-entity]]
    (dispatch [::events/navigate :view/group])
    {:db (-> db
             (assoc-in group-db/path-upsert-status :form.status/default)
             (assoc-in group-db/path-upsert-errors nil))}))

(reg-event-db
  :group.event/upsert-group-failure
  (fn [db [_ {:keys [error] :as response}]]
    (when error
      (let [message (.-message error)]
        (js/console.error message)))
    (-> db
        (assoc-in group-db/path-upsert-status :form.status/default)
        (assoc-in group-db/path-upsert-errors (when error
                                               (cljs.reader/read-string (.-message error)))))))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ delete-group [event]                                                       │
;; └───────────────────────────────────────────────────────────────────────────┘
(reg-event-fx
  :group.event/delete-group
  (fn [{:keys [db]} [_ group-id]]
    (go (try
          (let [{:keys [success body] :as response}
                (<! (group-service/delete-group-by-group-id
                      group-service-repo
                      (group/delete-group-by-group-id-command group-id)))]
            (if success
              (dispatch [:group.event/delete-group-success body])
              (dispatch [:group.event/delete-group-failure response])))
          (catch js/Error e
            (dispatch [:group.event/delete-group-failure {:error e}]))))))

(reg-event-db
  :group.event/delete-group-success
  (fn [db [_ group-id]]
    (dispatch [:group.event/fetch-groups])
    db))

(reg-event-db
  :group.event/delete-group-failure
  (fn [db [_ response]]
    (js/console.error "delete-group-failure" response)
    db))

