(ns photoni.webapp.frontend.pages.user.user-db-sub-event
  (:require [reagent.core :as r]
            [re-frame.core :as re-frame :refer [subscribe dispatch reg-event-fx reg-event-db reg-sub reg-cofx]]
            [cljs.core.async :refer [<!]]
            [photoni.webapp.frontend.events :as events]
            [photoni.webapp.domain.user.user-service :as user-service]
            [photoni.webapp.domain.user.user :as user]
            [photoni.webapp.domain.user.user-repository :as user-repository]
            [photoni.webapp.frontend.pages.user.user-service :refer [user-service-repo]])
  (:require-macros [cljs.core.async.macros :refer [go]]))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ user-db                                                                   │
;; └───────────────────────────────────────────────────────────────────────────┘
(def user-db
  {:users {:loading? false
           :total    0
           :count    0
           :rows     []}})

(def path-users-loading? [:users :loading?])


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ get-users [sub]                                                           │
;; └───────────────────────────────────────────────────────────────────────────┘
(reg-sub :user.sub/users-rows (fn [db _] (get-in db [:users :rows])))
(reg-sub :user.sub/users-loading (fn [db _] (get-in db path-users-loading?)))

(reg-event-db
  :user.event/fetch-users
  (fn [db _]
    (try
      (go (let [{:keys [success body] :as response}
                (<! (user-service/get-users user-service-repo {}))
                user-entities body]
            (if success
              (dispatch [:user.event/fetch-users-success user-entities])
              (dispatch [:user.event/fetch-users-failure response]))))
      (assoc-in db path-users-loading? true)
      (catch js/Error e
        (dispatch [:user.event/fetch-users-failure {:error e}])))))

(reg-event-db
  :user.event/fetch-users-success
  (fn [db [_ users]]
    (-> db
        (assoc-in [:users] users)
        (assoc-in path-users-loading? false))))

(reg-event-db
  :user.event/fetch-users-failure
  (fn [db [_ response]]
    (js/console.error "fetch-users-failure" response)
    db))




;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ find-users [event]                                                           │
;; └───────────────────────────────────────────────────────────────────────────┘
;(reg-event-db
;  :initialise-db-with-dt
;  (fn [db [_ stored-db]]
;    (assoc db :users (db-user/find-all-users))))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ add-user [event]                                                          │
;; └───────────────────────────────────────────────────────────────────────────┘
;(reg-event-fx
;  :user.event/add-user
;  (fn [{:keys [db]} [_ user]]
;    (try
;      (do (db-user/add-user user)
;          {:dispatch [:user.event/add-user-success user]})
;      (catch js/Error e
;        {:dispatch [:user.event/add-user-failure user]}))))
;
;(reg-event-db
;  :user.event/add-user-success
;  (fn [db [_ user]]
;    (assoc db :users (conj (:users db) user))))
;
;(reg-event-db
;  :user.event/add-user-failure
;  (fn [db [_ user-id]]
;    (prn "add user failure" user-id)))




;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ upsert-user                                                               │
;; └───────────────────────────────────────────────────────────────────────────┘
(reg-event-fx
  :user.event/upsert-user
  (fn [{:keys [db]} [_ user-fields]]
    (prn "upsert-user" user-fields)
    (try
      (go
        (let [{:keys [success body] :as response}
              (<! (user-service/create-user user-service-repo
                    (user/create-user-command user-fields)))
              user-entity body]
          (if success
            (dispatch [:user.event/upsert-user-success user-entity])
            (dispatch [:user.event/upsert-user-failure response]))))
      (catch js/Error e
        (dispatch [:user.event/upsert-user-failure {:error e}])))
    {}))

(reg-event-db
  :user.event/upsert-user-success
  (fn [db [_ user-entity]]
    (dispatch [::events/navigate :view/user])
    db))

(reg-event-db
  :user.event/upsert-user-failure
  (fn [db [_ response]]
    (js/console.error "upsert-user-failure" response)
    db))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ delete-user [event]                                                       │
;; └───────────────────────────────────────────────────────────────────────────┘
(reg-event-fx
  :user.event/delete-user
  (fn [{:keys [db]} [_ user-id]]
    (go
      (let [response (<! (user-service/delete-user-by-user-id user-service-repo
                                                              (user/delete-user-by-user-id-command user-id)))]
        (dispatch [:user.event/delete-user-success response])))))

(reg-event-db
  :user.event/delete-user-success
  (fn [db [_ user-id]]
    (dispatch [:user.event/fetch-users])
    db))

(reg-event-db
  :user.event/delete-user-failure
  (fn [db [_ user-id]]
    (prn "delete user failure" user-id)))

