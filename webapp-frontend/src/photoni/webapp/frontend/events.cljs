(ns photoni.webapp.frontend.events
  (:require
    [re-frame.core :as re-frame]
    [day8.re-frame.tracing :refer-macros [fn-traced]]
    [photoni.webapp.frontend.reframedb.db :as reframe-db]
    ))

(re-frame/reg-event-db
  ::initialize-db
  (fn-traced [_ _]
             reframe-db/default-db))

(re-frame/reg-event-fx
  ::navigate
  (fn-traced [_ [_ handler route-params]]
             {:navigate [handler route-params]}))

(re-frame/reg-event-fx
  ::set-active-panel
  (fn-traced [{:keys [db]} [_ active-panel params]]
             {:db (-> db
                      (assoc :active-panel active-panel)
                      (assoc :route-params params))}))
