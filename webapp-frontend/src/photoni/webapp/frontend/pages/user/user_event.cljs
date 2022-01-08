(ns photoni.webapp.frontend.pages.user.user-event
  (:require [reagent.core :as r]
            [re-frame.core :as re-frame :refer [subscribe dispatch reg-event-fx reg-event-db reg-sub reg-cofx]]
            [cljs.core.async :refer [<!]]
            [photoni.webapp.frontend.events :as events]
            [photoni.webapp.domain.user.user-service :as user-service]
            [photoni.webapp.domain.user.user :as user]
            [photoni.webapp.domain.user.user-repository :as user-repository]
            [photoni.webapp.frontend.pages.user.user-service :refer [user-service-repo]]
            [photoni.webapp.frontend.pages.user.user-db :as user-db])
  (:require-macros [cljs.core.async.macros :refer [go]]))


(reg-event-db
  :user.event/clear-upsert-user-row
  (fn [db _]
    (prn ":user.event/clear-upsert-user-row")
    (assoc-in db user-db/path-upsert-user-row {})))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ get-users [sub]                                                           │
;; └───────────────────────────────────────────────────────────────────────────┘
(reg-sub :user.sub/users-rows (fn [db _] (get-in db user-db/path-users-rows)))
(reg-sub :user.sub/users-loading (fn [db _] (get-in db user-db/path-users-loading?)))
(reg-sub :user.sub/users-upsert-status (fn [db _] (get-in db user-db/path-upsert-status)))


(reg-event-fx
  :user.event/fetch-users
  (fn [{:keys [db]} _]
    (try (go (let [{:keys [success body] :as response}
                   (<! (user-service/get-users user-service-repo {}))
                   user-entities body]
               (if success
                 (dispatch [:user.event/fetch-users-success user-entities])
                 (dispatch [:user.event/fetch-users-failure response]))))
         (catch js/Error e
           (dispatch [:user.event/fetch-users-failure {:error e}])))
    {:db (-> db
             (assoc-in user-db/path-users-loading? true))}))

(reg-event-db
  :user.event/fetch-users-success
  (fn [db [_ users]]
    (-> db
        (assoc-in user-db/path-users users)
        (assoc-in user-db/path-users-loading? false))))

(reg-event-db
  :user.event/fetch-users-failure
  (fn [db [_ response]]
    (js/console.error "fetch-users-failure" response)
    db))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ get-user                                                                  │
;; └───────────────────────────────────────────────────────────────────────────┘
(reg-sub :user.sub/upsert-user-row (fn [db _] (get-in db user-db/path-upsert-user-row)))

(reg-event-fx
  :user.event/fetch-user
  (fn [{:keys [db]} [_ user-id]]
    (prn ":user.event/fetch-user user-id:" user-id)
    (try (go (let [{:keys [success body] :as response}
                   (<! (user-service/get-user-by-user-id
                         user-service-repo
                         (user/get-user-by-id-query user-id)))
                   user-entity body]
               (if success
                 (dispatch [:user.event/fetch-user-success user-entity])
                 (dispatch [:user.event/fetch-user-failure response]))))
         (catch js/Error e
           (dispatch [:user.event/fetch-user-failure {:error e}])))
    {:db (-> db
             (assoc-in user-db/path-upsert-user-row {})
             (assoc-in user-db/path-upsert-status :form.status/loading))}))

(reg-event-db
  :user.event/fetch-user-success
  (fn [db [_ user]]
    (prn ":user.event/fetch-user-success" "user:" user)
    (-> db
        (assoc-in user-db/path-upsert-user-row user)
        (assoc-in user-db/path-upsert-status :form.status/default))))

(reg-event-db
  :user.event/fetch-user-failure
  (fn [db [_ response]]
    (js/console.error "fetch-users-failure" response)
    (-> db
        (assoc-in user-db/path-upsert-user-row {})
        (assoc-in user-db/path-upsert-status :form.status/default))))




;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ find-users [event]                                                           │
;; └───────────────────────────────────────────────────────────────────────────┘
;(reg-event-db
;  :initialise-db-with-dt
;  (fn [db [_ stored-db]]
;    (assoc db :users (db-user/find-all-users))))





;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ upsert-user                                                               │
;; └───────────────────────────────────────────────────────────────────────────┘
(reg-event-fx
  :user.event/upsert-user
  (fn [{:keys [db]} [_ user-fields]]
    (let [user-id (or (:user/id user-fields) (random-uuid))
          user-fields (merge user-fields {:user/id user-id})]
      (try (go
             (let [{:keys [success body] :as response}
                   (<! (user-service/create-user user-service-repo
                                                 (user/create-user-command user-fields)))
                   user-entity body]
               (if success
                 (dispatch [:user.event/upsert-user-success user-entity])
                 (dispatch [:user.event/upsert-user-failure response]))))
           (catch js/Error e
             (dispatch [:user.event/upsert-user-failure {:error e}]))))
    {:db (-> db
             (assoc-in user-db/path-upsert-status :form.status/processing))}))

(reg-event-fx
  :user.event/upsert-user-success
  (fn [{:keys [db]} [_ user-entity]]
    (dispatch [::events/navigate :view/user])
    {:db (-> db
             (assoc-in user-db/path-upsert-status :form.status/default))}))

(reg-event-db
  :user.event/upsert-user-failure
  (fn [db [_ response]]
    (js/console.error "upsert-user-failure" response)
    (-> db
        (assoc-in user-db/path-upsert-status :form.status/default))))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ delete-user [event]                                                       │
;; └───────────────────────────────────────────────────────────────────────────┘
(reg-event-fx
  :user.event/delete-user
  (fn [{:keys [db]} [_ user-id]]
    (try (go (let [{:keys [success body] :as response}
                   (<! (user-service/delete-user-by-user-id
                         user-service-repo
                         (user/delete-user-by-user-id-command user-id)))]
               (if success
                 (dispatch [:user.event/delete-user-success body])
                 (dispatch [:user.event/delete-user-failure response]))))
         (catch js/Error e
           (dispatch [:user.event/delete-user-failure {:error e}])))))

(reg-event-db
  :user.event/delete-user-success
  (fn [db [_ user-id]]
    (dispatch [:user.event/fetch-users])
    db))

(reg-event-db
  :user.event/delete-user-failure
  (fn [db [_ response]]
    (js/console.error "delete-user-failure" response)
    db))

