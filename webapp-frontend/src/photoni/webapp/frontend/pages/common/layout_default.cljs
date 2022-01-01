(ns photoni.webapp.frontend.pages.common.layout-default
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [photoni.webapp.frontend.events :as events]
            [photoni.webapp.frontend.components.layout :as layout]))

(defn layout-view
  [component]
  [layout/layout {:view.layout/go-to-home-fn  #(dispatch [::events/navigate :view/home])
                  :view.layout/go-to-about-fn #(dispatch [::events/navigate :view/about])
                  :view.layout/go-to-user-fn  #(dispatch [::events/navigate :view/user])}
   component])

