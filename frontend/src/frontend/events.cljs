(ns frontend.events
  (:require
   [re-frame.core :as re-frame]
   [frontend.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-db
  :initialise-db-with-dt
  (fn [db [_ stored-db]]
    (prn ":initialise-db-with-dt" stored-db)
    (prn "db/find-all-users: " (db/find-all-users))

    db))

(re-frame/reg-event-fx
  ::navigate
  (fn-traced [_ [_ handler]]
   {:navigate handler}))

(re-frame/reg-event-fx
 ::set-active-panel
 (fn-traced [{:keys [db]} [_ active-panel]]
   {:db (assoc db :active-panel active-panel)}))
