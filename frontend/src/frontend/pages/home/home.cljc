(ns frontend.pages.home.home
  (:require
    [frontend.events :as events]
    [frontend.utils.tailwind-styles :as styles]
    [re-frame.core :as re-frame :refer [subscribe dispatch]]
    [frontend.components.components :as components]
    [frontend.domain.user :as domain-user]
    [frontend.pages.user.user-list :as page-user-list]
    [frontend.components.layout :as layout]))

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
