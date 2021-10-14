(ns frontend.components.components)

(defn user-list
  [users]
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
        (map (fn [user]
               [:tr.bg-white
                [:td.px-6.py-4.whitespace-nowrap.text-sm.font-medium.text-gray-900 "Jane Cooper"]
                [:td.px-6.py-4.whitespace-nowrap.text-sm.text-gray-500 "Regional Paradigm Technician"]
                [:td.px-6.py-4.whitespace-nowrap.text-sm.text-gray-500 "jane.cooper@example.com"]
                [:td.px-6.py-4.whitespace-nowrap.text-sm.text-gray-500 "Admin"]
                [:td.px-6.py-4.whitespace-nowrap.text-right.text-sm.font-medium
                 [:a.text-indigo-600.hover:text-indigo-900 {:href "#"} "Edit"]
                 [:a.text-indigo-600.hover:text-indigo-900 {:href "#"} "Delete"]]])
             users)
        ]]]]]]
  )