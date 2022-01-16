(ns photoni.webapp.frontend.pages.not-found.not-found
  (:require
    [photoni.webapp.frontend.pages.common.layout-default :as layout-default]))

(defn not-found-panel []
  [layout-default/layout-view
   [:div
    "404"]])

