(ns photoni.webapp.frontend.components.crud.list
  (:require [photoni.webapp.frontend.components.components :as components]
            [photoni.webapp.frontend.utils.tailwind-styles :as styles]
            [photoni.webapp.frontend.components.icons :as icons]))

(defn crud-list
  [{:keys [title
           rows
           columns
           add-user-fn
           delete-user-fn
           go-to-home-fn
           go-to-about-fn
           go-to-user-fn
           loading?]}]

  [:div {:class [styles/md:relative]}
   [:div {:class [styles/md:flex styles/md:items-center styles/md:justify-between]}
    [:div {:class [styles/flex-1 styles/min-w-0]}
     [:h2 {:class [styles/text-2xl styles/font-bold styles/leading-7 styles/text-gray-900
                   styles/sm:text-3xl styles/sm:truncate]} title]]

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
          (map (fn [[key {:keys [label]}]]
                 [:th.px-6.py-3.text-left.text-xs.font-medium.text-gray-500.uppercase.tracking-wider {:scope "col"}
                  [:div {:class [styles/flex styles/items-center]}
                   (icons/icon-arrow-up 4 4)
                   label]])
               columns)

          [:th.relative.px-6.py-3 {:scope "col"}
           [:span.sr-only "Edit"]]]]
        [:tbody
         (when loading? [components/load-spinner])
         (map (fn [{:user/keys [id] :as row}]
                ^{:key id} [:tr.bg-white
                            (map (fn [[k value]]
                                   (let [column-conf (get columns k)
                                         {:keys [format]} column-conf]
                                     [:td.px-6.py-4.whitespace-nowrap.text-sm.text-gray-500
                                      {:class [styles/border-b styles/border-gray-200]}
                                      (if format
                                        (format value)
                                        value)]))
                                 row)

                            [:td.px-6.py-4.whitespace-nowrap.text-right.text-sm.font-medium
                             {:class [styles/border-b styles/border-gray-200]}
                             [:a.text-indigo-600.hover:text-indigo-900 {:href "#"} "Edit"]
                             [:a.text-indigo-600.hover:text-indigo-900 {:href     "#"
                                                                        :on-click (fn [] (delete-user-fn id))} "Delete"]]])
              rows)
         ]]
       [components/pagination]

       ]]]]]
  )