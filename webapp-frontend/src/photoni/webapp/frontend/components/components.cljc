(ns photoni.webapp.frontend.components.components
  (:require [photoni.webapp.frontend.utils.tailwind-styles :as styles]))

(defn user-list
  [users {:keys [add-user-fn delete-user-fn]}]
  [:div
   [:div {:class [styles/md:flex styles/md:items-center styles/md:justify-between]}
    [:div {:class [styles/flex-1 styles/min-w-0]}
     [:h2 {:class [styles/text-2xl styles/font-bold styles/leading-7 styles/text-gray-900
                   styles/sm:text-3xl styles/sm:truncate]} "Back End Developer"]]

    [:div.mt-4.flex.md:mt-0.md:ml-4
     [:button.inline-flex.items-center.px-4.py-2.border.border-gray-300.rounded-md.shadow-sm.text-sm.font-medium.text-gray-700.bg-white.hover:bg-gray-50.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500 {:type "button"} "Edit"]
     [:button.ml-3.inline-flex.items-center.px-4.py-2.border.border-transparent.rounded-md.shadow-sm.text-sm.font-medium.text-white.bg-indigo-600.hover:bg-indigo-700.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
      {:type "button" :on-click add-user-fn} "Add user"]]]




   [:div.flex.flex-col
   [:div.-my-2.overflow-x-auto.sm:-mx-6.lg:-mx-8
    [:div.py-2.align-middle.inline-block.min-w-full.sm:px-6.lg:px-8
     [:div.shadow.overflow-hidden.border-b.border-gray-200.sm:rounded-lg
      [:table.min-w-full.divide-y.divide-gray-200
       [:thead.bg-gray-50
        [:tr
         [:th.px-6.py-3.text-left.text-xs.font-medium.text-gray-500.uppercase.tracking-wider {:scope "col"} "Name"]
         [:th.px-6.py-3.text-left.text-xs.font-medium.text-gray-500.uppercase.tracking-wider {:scope "col"} "Title"]
         [:th.px-6.py-3.text-left.text-xs.font-medium.text-gray-500.uppercase.tracking-wider {:scope "col"} "Email"]
         [:th.px-6.py-3.text-left.text-xs.font-medium.text-gray-500.uppercase.tracking-wider {:scope "col"} "Role"]
         [:th.relative.px-6.py-3 {:scope "col"}
          [:span.sr-only "Edit"]]]]
       [:tbody
        (map (fn [^frontend.domain.user {:user/keys [id name title email age role]}]
               ^{:key id}[:tr.bg-white
                [:td.px-6.py-4.whitespace-nowrap.text-sm.font-medium.text-gray-900 name]
                [:td.px-6.py-4.whitespace-nowrap.text-sm.text-gray-500 title]
                [:td.px-6.py-4.whitespace-nowrap.text-sm.text-gray-500 email]
                [:td.px-6.py-4.whitespace-nowrap.text-sm.text-gray-500 role]
                [:td.px-6.py-4.whitespace-nowrap.text-right.text-sm.font-medium
                 [:a.text-indigo-600.hover:text-indigo-900 {:href "#"} "Edit"]
                 [:a.text-indigo-600.hover:text-indigo-900 {:href "#"
                                                            :on-click (fn [] (delete-user-fn id))} "Delete"]]])
             users)
        ]]]]]]]
  )


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