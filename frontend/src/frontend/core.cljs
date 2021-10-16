(ns frontend.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [frontend.routes :as routes]
   [frontend.views :as views]
   [frontend.config :as config]
   [frontend.pages.user.user-list]
   [frontend.events :as events]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [frontend.utils.persist-localstorage]))

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
