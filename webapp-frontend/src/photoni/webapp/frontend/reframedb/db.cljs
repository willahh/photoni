(ns photoni.webapp.frontend.reframedb.db
  (:require [re-frame.core :as re-frame]
            [day8.re-frame.tracing :refer-macros [fn-traced]]
            [photoni.webapp.frontend.db.db :as db]
            [photoni.webapp.frontend.db.user :as db-user]))

(def default-db
  {:active-panel 1
   :users []})



(re-frame/reg-event-db
  :initialise-db-with-dt
  (fn [db [_ stored-db]]
    (assoc db :users (db-user/find-all-users))))