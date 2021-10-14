(ns frontend.pages.home.home
  (:require
    [re-frame.core :as re-frame]
    [frontend.subs :as subs]
    [frontend.events :as events]
    [frontend.tailwind-styles :as styles]
    [re-frame.core :refer [subscribe dispatch]]
    [frontend.components.components :as components]
    [frontend.domain.user :as domain-user]))

(defn home-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div
     [:h1
      (str "Hello from " @name ". This is the Home Page23.")]

     [:div
      [:a {:on-click #(re-frame/dispatch [::events/navigate :about])}
       "Home"]]

     [:div
      [:a {:on-click #(re-frame/dispatch [::events/navigate :about])}
       "About"]]

     [:div
      [:a {:on-click #(re-frame/dispatch [::events/navigate :about])}
       "Users"]]


     (let [users @(subscribe [:users])]
       (components/user-list users {:add-user-fn
                                    (fn []
                                      (let [user (domain-user/generate-user-stub)]
                                        (dispatch [:add-user user])))}))
     ]))
