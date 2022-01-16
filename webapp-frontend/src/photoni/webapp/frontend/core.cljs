(ns photoni.webapp.frontend.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame :refer [reg-event-db dispatch]]
   [photoni.webapp.frontend.reframedb.db :as reframe-db]
   [photoni.webapp.frontend.routes :as routes]
   [photoni.webapp.frontend.views :as views]
   [photoni.webapp.frontend.config :as config]
   [photoni.webapp.frontend.events :as events]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [photoni.webapp.frontend.translations.translations]))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (routes/start!)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
