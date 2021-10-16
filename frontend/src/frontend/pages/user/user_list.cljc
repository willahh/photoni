(ns frontend.pages.user.user-list
  (:require [frontend.db.db :as db]
            [frontend.db.user :as db-user]
            [frontend.components.components :as components]
            [frontend.domain.user :as domain-user]
            [re-frame.core :as re-frame :refer [subscribe dispatch reg-event-fx reg-event-db reg-sub]]))

(defn user-page
  []
  (let [users @(subscribe [:users])]
    (components/user-list users
                          {:add-user-fn
                           (fn []
                             (let [user (domain-user/generate-user-stub)]
                               (dispatch [:user.event/add-user user])))
                           :delete-user-fn
                           (fn [user-id]
                             (prn ":delete-user-fn")
                             (dispatch [:user.event/delete-user user-id]))})))



;; re-frame events and db handlers
(reg-sub
  :users
  (fn [db _]
    (:users db)))

;; add user
(reg-event-fx
  :user.event/add-user
  (fn [{:keys [db]} [_ user]]
    (try
      (do (db-user/add-user user)
          {:dispatch [:user.event/add-user-success user]})
      (catch js/Error e
        {:dispatch [:user.event/add-user-failure user]}))))

(reg-event-db
  :user.event/add-user-success
  (fn [db [_ user]]
    (assoc db :users (conj (:users db) user))))

(reg-event-db
  :user.event/add-user-failure
  (fn [db [_ user-id]]
    (prn "add user failure" user-id)))


;; delete user
(reg-event-fx
  :user.event/delete-user
  (fn [{:keys [db]} [_ user-id]]
    (try
      (do (db-user/delete-user-by-user-id user-id)
          {:dispatch [:user.event/delete-user-success user-id]})
      (catch js/Error e
        {:dispatch [:user.event/delete-user-failure user-id]}))))

(reg-event-db
  :user.event/delete-user-success
  (fn [db [_ user-id]]
    (let [users (->> (:users db)
                     (filter #(not= (:user/id %) user-id)))]
      (assoc db :users users))))

(reg-event-db
  :user.event/delete-user-failure
  (fn [db [_ user-id]]
    (prn "delete user failure" user-id)))




