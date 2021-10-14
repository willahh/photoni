(ns frontend.pages.home.home
  (:require
    [re-frame.core :as re-frame]
    [frontend.events :as events]
    [frontend.routes :as routes]
    [frontend.subs :as subs]
    [frontend.tailwind-styles :as styles]

    [re-frame.core :refer [subscribe dispatch]]
    [frontend.components.components :as components]))

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

     [:div {:class [styles/md:flex styles/md:items-center styles/md:justify-between]}
      [:div {:class [styles/flex-1 styles/min-w-0]}
       [:h2 {:class [styles/text-2xl styles/font-bold styles/leading-7 styles/text-gray-900
                     styles/sm:text-3xl styles/sm:truncate]} "Back End Developer"]]

      [:div.mt-4.flex.md:mt-0.md:ml-4
       [:button.inline-flex.items-center.px-4.py-2.border.border-gray-300.rounded-md.shadow-sm.text-sm.font-medium.text-gray-700.bg-white.hover:bg-gray-50.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500 {:type "button"} "Edit"]
       [:button.ml-3.inline-flex.items-center.px-4.py-2.border.border-transparent.rounded-md.shadow-sm.text-sm.font-medium.text-white.bg-indigo-600.hover:bg-indigo-700.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
        {:type "button" :on-click (fn []
                                    (dispatch [:add-user {:user/id   (rand)
                                                          :user/name (str "User " (rand))
                                                          :user/age  35}]))} "Add user"]]]

     (let [users @(subscribe [:users])]
       (components/user-list users))
     ]))
