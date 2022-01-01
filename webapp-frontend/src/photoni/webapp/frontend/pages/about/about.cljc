(ns photoni.webapp.frontend.pages.about.about
  (:require [re-frame.core :as re-frame]
            [re-frame.core :refer [subscribe dispatch]]
            [photoni.webapp.frontend.events :as events]
            [photoni.webapp.frontend.pages.common.layout-default :as layout-default]))

(defn about-panel []
  [layout-default/layout-view
   [:div
    [:h1 "This is the About Page."]

    [:div
     [:a {:on-click #(re-frame/dispatch [::events/navigate :view/home])}
      "go to Home Page"]]]])