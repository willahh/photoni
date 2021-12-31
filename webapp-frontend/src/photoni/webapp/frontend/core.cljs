(ns photoni.webapp.frontend.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [photoni.webapp.frontend.routes :as routes]
   [photoni.webapp.frontend.views :as views]
   [photoni.webapp.frontend.config :as config]
   [photoni.webapp.frontend.pages.user.user-list]
   [photoni.webapp.frontend.events :as events]
   [day8.re-frame.tracing :refer-macros [fn-traced]]))

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


(comment
  (let [p (js/fetch "http://localhost:8280")]
    (.then p (fn [response]
               (println "x" x))))

  ;; TODO: Async await for CLJS https://github.com/roman01la/cljs-async-await
  (try
    (async/go
      (<p! (js/fetch "http://localhost:8280")))
    (catch js/Object error
      (.error js/console "Failed retrieving companies, " error)))



  (defn request-companies []
    (try
      (async/go
        (<p! (getDocs (collection (getFirestore) "companies"))))
      (catch js/Object error
        (.error js/console "Failed retrieving companies, " error))))

  )