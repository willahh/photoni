(ns photoni.webapp.frontend.pages.home.home
  (:require
    [re-frame.core :as re-frame :refer [subscribe dispatch]]
    [photoni.webapp.frontend.events :as events]
    [photoni.webapp.frontend.utils.tailwind-styles :as styles]
    [photoni.webapp.frontend.components.components :as components]
    #_[photoni.webapp.frontend.domain.user :as domain-user]
    [photoni.webapp.frontend.pages.user.user-list :as page-user-list]
    [photoni.webapp.frontend.components.layout :as layout]))

(re-frame/reg-sub
  ::name
  (fn [db]
    (:name db)))

(defn home-panel []
  (let [name (subscribe [::name])]

    [layout/layout
     [:div [:h1
            (str "Hello from " @name ". This is the Home Page23.")]

      [:div
       [:a {:on-click #(dispatch [::events/navigate :about])}
        "Home"]]

      [:div
       [:a {:on-click #(dispatch [::events/navigate :about])}
        "About"]]

      [:div
       [:a {:on-click #(dispatch [::events/navigate :about])}
        "Users"]]

      [page-user-list/user-page]]]))
