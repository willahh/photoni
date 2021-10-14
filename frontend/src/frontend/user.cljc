(ns frontend.user
  (:require [frontend.db :as db]
            [re-frame.core :as re-frame :refer [reg-event-fx reg-event-db reg-sub]]))


;; re-frame events and db handlers
(reg-sub
  :users  ;; usage: (subscribe [:articles])
  (fn [db _]
    (prn "reg-sub users:" (:users db))
    (:users db)))

(reg-event-fx
  :add-user
  (fn [{:keys [db]} [_ user]]
    (prn "event fx user - : " user)
    (try
      (do (db/add-user user)
          {:dispatch [:add-user-success user]})
      (catch js/Error e
        {:dispatch [:add-user-fail user]}))))

(reg-event-db
  :add-user-success
  (fn [db [_ user]]
    (prn "reg event db :add-user-success user - : " user)
    (assoc db :users (conj (:users db) user))
    ))

(reg-event-db
  :add-user-failure
  (fn [db [_ user]]
    (prn "add user failure")))

