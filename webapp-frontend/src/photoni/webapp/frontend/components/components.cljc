(ns photoni.webapp.frontend.components.components
  (:require [reagent.core :as r]
            [photoni.webapp.frontend.utils.tailwind-styles :as styles]))

(defn load-spinner
  []
  [:div.flex.items-center.justify-center.space-x-2
   {:class [styles/md:absolute styles/md:w-full]}
   [:div.spinner-border.animate-spin.inline-block.w-12.h-12.border-4.rounded-full {:role "status"}
    [:span.visually-hidden "Loading..."]]])

(defn button
  []
  [:button.inline-flex.items-center.p-1.border.border-transparent.rounded-full.shadow-sm.text-white.bg-indigo-600.hover:bg-indigo-700.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
   {:class [styles/bg-indigo-600] :type "button"}
   [:svg.h-5.w-5 {:xmlns "http://www.w3.org/2000/svg" :viewBox "0 0 20 20" :fill "currentColor" :aria-hidden "true"}
    [:path {:fill-rule "evenodd" :d "M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z" :clip-rule "evenodd"}]]])

(defn button-w-icon
  [{:keys [href] :as opts} icon]
  [(if href :a :button)
   (merge {:class "inline-flex items-center p-1 border border-transparent rounded-full shadow-sm text-black hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"}
          opts)
   icon])

(defn modal-confirm-delete
  [title description delete-fn visible?]
  (fn []
    (when @visible?
      [:div
       [:div.flex.flex-col.space-y-4.min-w-screen.h-screen.animated.fadeIn.faster.fixed.left-0.top-0.flex.justify-center.items-center.inset-0.outline-none.focus:outline-none.opacity-50.bg-gray-900
        {:class     [styles/fixed styles/z-40]
         :tab-index 0
         :on-click  (fn [] (reset! visible? false))
         :on-key-up (fn [e]
                      (when (= "Escape" (.-key e))
                        (reset! visible? false)))}]
       #_[:div.flex.flex-col.p-8.bg-white.shadow-md.hover:shodow-lg.rounded-2xl
          [:div.flex.items-center.justify-between
           [:div.flex.items-center
            [:svg.w-16.h-16.rounded-2xl.p-3.border.border-blue-100.text-blue-400.bg-blue-50 {:xmlns "http://www.w3.org/2000/svg" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor"}
             [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"}]]
            [:div.flex.flex-col.ml-3
             [:div.font-medium.leading-none title]
             [:p.text-sm.text-gray-600.leading-none.mt-1 description]]]
           [:button.flex-no-shrink.bg-red-500.px-5.ml-4.py-2.text-sm.shadow-sm.hover:shadow-lg.font-medium.tracking-wider.border-2.border-red-500.text-white.rounded-full "Delete"]]]
       [:div.flex.flex-col.p-8.bg-gray-800.shadow-md.hover:shodow-lg.rounded-2xl
        {:class [styles/fixed styles/z-50]}
        [:div.flex.items-center.justify-between
         [:div.flex.items-center
          [:svg.w-16.h-16.rounded-2xl.p-3.border.border-gray-800.text-blue-400.bg-gray-900 {:xmlns "http://www.w3.org/2000/svg" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor"}
           [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"}]]
          [:div.flex.flex-col.ml-3
           [:div.font-medium.leading-none.text-gray-100 title]
           [:p.text-sm.text-gray-500.leading-none.mt-1 description]]]
         [:button.flex-no-shrink.bg-red-500.px-5.ml-4.py-2.text-sm.shadow-sm.hover:shadow-lg.font-medium.tracking-wider.border-2.border-red-500.text-white.rounded-full
          {:on-click (fn [] (delete-fn))}
          "Delete"]]]])))


(defn pagination
  []
  [:nav.border-t.border-gray-200.px-4.flex.items-center.justify-between.sm:px-0
   [:div.-mt-px.w-0.flex-1.flex
    [:a.border-t-2.border-transparent.pt-4.pr-1.inline-flex.items-center.text-sm.font-medium.text-gray-500.hover:text-gray-700.hover:border-gray-300 {:href "#"}
     [:svg.mr-3.h-5.w-5.text-gray-400 {:xmlns "http://www.w3.org/2000/svg" :viewBox "0 0 20 20" :fill "currentColor" :aria-hidden "true"}
      [:path {:fill-rule "evenodd" :d "M7.707 14.707a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 1.414L5.414 9H17a1 1 0 110 2H5.414l2.293 2.293a1 1 0 010 1.414z" :clip-rule "evenodd"}]] "Previous"]]
   [:div {:class [styles/mt-px styles/flex]}
    [:a.border-transparent.text-gray-500.hover:text-gray-700.hover:border-gray-300.border-t-2.pt-4.px-4.inline-flex.items-center.text-sm.font-medium {:href "#"} "1"]
    [:a.border-indigo-500.text-indigo-600.border-t-2.pt-4.px-4.inline-flex.items-center.text-sm.font-medium {:href "#" :aria-current "page"} "2"]
    [:a.border-transparent.text-gray-500.hover:text-gray-700.hover:border-gray-300.border-t-2.pt-4.px-4.inline-flex.items-center.text-sm.font-medium {:href "#"} "3"]
    [:span.border-transparent.text-gray-500.border-t-2.pt-4.px-4.inline-flex.items-center.text-sm.font-medium "..."]
    [:a.border-transparent.text-gray-500.hover:text-gray-700.hover:border-gray-300.border-t-2.pt-4.px-4.inline-flex.items-center.text-sm.font-medium {:href "#"} "8"]
    [:a.border-transparent.text-gray-500.hover:text-gray-700.hover:border-gray-300.border-t-2.pt-4.px-4.inline-flex.items-center.text-sm.font-medium {:href "#"} "9"]
    [:a.border-transparent.text-gray-500.hover:text-gray-700.hover:border-gray-300.border-t-2.pt-4.px-4.inline-flex.items-center.text-sm.font-medium {:href "#"} "10"]]
   [:div.-mt-px.w-0.flex-1.flex.justify-end
    [:a.border-t-2.border-transparent.pt-4.pl-1.inline-flex.items-center.text-sm.font-medium.text-gray-500.hover:text-gray-700.hover:border-gray-300 {:href "#"} "Next"
     [:svg.ml-3.h-5.w-5.text-gray-400 {:xmlns "http://www.w3.org/2000/svg" :viewBox "0 0 20 20" :fill "currentColor" :aria-hidden "true"}
      [:path {:fill-rule "evenodd" :d "M12.293 5.293a1 1 0 011.414 0l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414-1.414L14.586 11H3a1 1 0 110-2h11.586l-2.293-2.293a1 1 0 010-1.414z" :clip-rule "evenodd"}]]]]])



(defn user-edit
  []
  [:form.space-y-8.divide-y.divide-gray-200
   [:div.space-y-8.divide-y.divide-gray-200.sm:space-y-5

    [:div.pt-8.space-y-6.sm:pt-10.sm:space-y-5
     [:div
      [:h3.text-lg.leading-6.font-medium.text-gray-900 "User A"]]
     [:div.space-y-6.sm:space-y-5
      [:div.sm:grid.sm:grid-cols-3.sm:gap-4.sm:items-start.sm:border-t.sm:border-gray-200.sm:pt-5
       [:label.block.text-sm.font-medium.text-gray-700.sm:mt-px.sm:pt-2 {:for "first-name"} "First name"]
       [:div.mt-1.sm:mt-0.sm:col-span-2
        [:input#first-name.max-w-lg.block.w-full.shadow-sm.focus:ring-indigo-500.focus:border-indigo-500.sm:max-w-xs.sm:text-sm.border-gray-300.rounded-md {:type "text" :name "first-name" :autocomplete "given-name"}]]]
      [:div.sm:grid.sm:grid-cols-3.sm:gap-4.sm:items-start.sm:border-t.sm:border-gray-200.sm:pt-5
       [:label.block.text-sm.font-medium.text-gray-700.sm:mt-px.sm:pt-2 {:for "last-name"} "Last name"]
       [:div.mt-1.sm:mt-0.sm:col-span-2
        [:input#last-name.max-w-lg.block.w-full.shadow-sm.focus:ring-indigo-500.focus:border-indigo-500.sm:max-w-xs.sm:text-sm.border-gray-300.rounded-md {:type "text" :name "last-name" :autocomplete "family-name"}]]]
      [:div.sm:grid.sm:grid-cols-3.sm:gap-4.sm:items-start.sm:border-t.sm:border-gray-200.sm:pt-5
       [:label.block.text-sm.font-medium.text-gray-700.sm:mt-px.sm:pt-2 {:for "email"} "Email address"]
       [:div.mt-1.sm:mt-0.sm:col-span-2
        [:input#email.block.max-w-lg.w-full.shadow-sm.focus:ring-indigo-500.focus:border-indigo-500.sm:text-sm.border-gray-300.rounded-md {:name "email" :type "email" :autocomplete "email"}]]]
      [:div.sm:grid.sm:grid-cols-3.sm:gap-4.sm:items-start.sm:border-t.sm:border-gray-200.sm:pt-5
       [:label.block.text-sm.font-medium.text-gray-700.sm:mt-px.sm:pt-2 {:for "country"} "Country / Region"]
       [:div.mt-1.sm:mt-0.sm:col-span-2
        [:select#country.max-w-lg.block.focus:ring-indigo-500.focus:border-indigo-500.w-full.shadow-sm.sm:max-w-xs.sm:text-sm.border-gray-300.rounded-md {:name "country" :autocomplete "country"}
         [:option "United States"]
         [:option "Canada"]
         [:option "Mexico"]]]]
      [:div.sm:grid.sm:grid-cols-3.sm:gap-4.sm:items-start.sm:border-t.sm:border-gray-200.sm:pt-5
       [:label.block.text-sm.font-medium.text-gray-700.sm:mt-px.sm:pt-2 {:for "street-address"} "Street address"]
       [:div.mt-1.sm:mt-0.sm:col-span-2
        [:input#street-address.block.max-w-lg.w-full.shadow-sm.focus:ring-indigo-500.focus:border-indigo-500.sm:text-sm.border-gray-300.rounded-md {:type "text" :name "street-address" :autocomplete "street-address"}]]]
      [:div.sm:grid.sm:grid-cols-3.sm:gap-4.sm:items-start.sm:border-t.sm:border-gray-200.sm:pt-5
       [:label.block.text-sm.font-medium.text-gray-700.sm:mt-px.sm:pt-2 {:for "city"} "City"]
       [:div.mt-1.sm:mt-0.sm:col-span-2
        [:input#city.max-w-lg.block.w-full.shadow-sm.focus:ring-indigo-500.focus:border-indigo-500.sm:max-w-xs.sm:text-sm.border-gray-300.rounded-md {:type "text" :name "city"}]]]
      [:div.sm:grid.sm:grid-cols-3.sm:gap-4.sm:items-start.sm:border-t.sm:border-gray-200.sm:pt-5
       [:label.block.text-sm.font-medium.text-gray-700.sm:mt-px.sm:pt-2 {:for "state"} "State / Province"]
       [:div.mt-1.sm:mt-0.sm:col-span-2
        [:input#state.max-w-lg.block.w-full.shadow-sm.focus:ring-indigo-500.focus:border-indigo-500.sm:max-w-xs.sm:text-sm.border-gray-300.rounded-md {:type "text" :name "state"}]]]
      [:div.sm:grid.sm:grid-cols-3.sm:gap-4.sm:items-start.sm:border-t.sm:border-gray-200.sm:pt-5
       [:label.block.text-sm.font-medium.text-gray-700.sm:mt-px.sm:pt-2 {:for "zip"} "ZIP / Postal"]
       [:div.mt-1.sm:mt-0.sm:col-span-2
        [:input#zip.max-w-lg.block.w-full.shadow-sm.focus:ring-indigo-500.focus:border-indigo-500.sm:max-w-xs.sm:text-sm.border-gray-300.rounded-md {:type "text" :name "zip" :autocomplete "postal-code"}]]]]]
    ]
   [:div.pt-5
    [:div.flex.justify-end
     [:button.bg-white.py-2.px-4.border.border-gray-300.rounded-md.shadow-sm.text-sm.font-medium.text-gray-700.hover:bg-gray-50.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500 {:type "button"} "Cancel"]
     [:button.ml-3.inline-flex.justify-center.py-2.px-4.border.border-transparent.shadow-sm.text-sm.font-medium.rounded-md.text-white.bg-indigo-600.hover:bg-indigo-700.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500 {:type "submit"} "Save"]]]])