(ns photoni.webapp.frontend.pages.user.user-db-sub-event
  (:require [reagent.core :as r]
            [re-frame.core :as re-frame :refer [subscribe dispatch reg-event-fx reg-event-db reg-sub reg-cofx]]
            [photoni.webapp.frontend.events :as events]
            [photoni.webapp.domain.user.user-service :as user-service]
            [photoni.webapp.domain.user.user-repository :as user-repository]
            [photoni.webapp.frontend.pages.user.user-service :refer [user-service-repo]]))


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
  :user.event/users-fetched
  (fn [db [_ users]]
    (-> db
        (assoc-in [:users] users)
        (assoc-in path-users-loading? false))))

(reg-event-db
  :user.event/fetch-users
  (fn [db _]
    (.then (user-service/get-users user-service-repo {})
           (fn [users]
             (dispatch [:user.event/users-fetched users])))
    (assoc-in db path-users-loading? true)))




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
;; │ delete-user [event]                                                       │
;; └───────────────────────────────────────────────────────────────────────────┘
;(reg-event-fx
;  :user.event/delete-user
;  (fn [{:keys [db]} [_ user-id]]
;    (try
;      (do (db-user/delete-user-by-user-id user-id)
;          {:dispatch [:user.event/delete-user-success user-id]})
;      (catch js/Error e
;        {:dispatch [:user.event/delete-user-failure user-id]}))))
;
;(reg-event-db
;  :user.event/delete-user-success
;  (fn [db [_ user-id]]
;    (let [users (->> (:users db)
;                     (filter #(not= (:user.event/id %) user-id)))]
;      (assoc db :users users))))
;
;(reg-event-db
;  :user.event/delete-user-failure
;  (fn [db [_ user-id]]
;    (prn "delete user failure" user-id)))

