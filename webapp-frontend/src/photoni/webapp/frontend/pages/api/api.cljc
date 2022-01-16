(ns photoni.webapp.frontend.pages.api.api
  (:require
    [re-frame.core :as re-frame :refer [subscribe dispatch]]
    [photoni.webapp.frontend.pages.common.layout-default :as layout-default]))

(defn page-api []
  [layout-default/layout-view
   [:iframe {:src    "http://localhost:3000/index.html#/"
             :width  "100%"
             :style {:height "calc(100vh - 200px)"}}]])
