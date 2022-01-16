(ns photoni.webapp.frontend.components.crud.list
  (:require [photoni.webapp.frontend.components.components :as components]
            [photoni.webapp.frontend.utils.tailwind-styles :as styles]
            [photoni.webapp.frontend.components.icons :as icons]
            [reagent.core :as r]))

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

  (let [delete-modal-visible (r/atom false)
        user-id-to-delete (r/atom nil)]
    (fn [{:keys [title
                 rows
                 columns
                 add-user-fn
                 delete-user-fn
                 go-to-home-fn
                 go-to-about-fn
                 go-to-user-fn
                 loading?]}]
      [:div {:class [styles/md:relative]}


       [:div.pb-5.border-b.border-gray-200.sm:flex.sm:items-center.sm:justify-between
        [:h2.text-lg.leading-6.font-medium.text-gray-900
         title]
        [:div.mt-3.flex.sm:mt-0.sm:ml-4
         [:button.inline-flex.items-center.px-4.py-2.border.border-gray-300.rounded-md.shadow-sm.text-sm.font-medium.text-gray-700.bg-white.hover:bg-gray-50.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
          {:type "button"} icons/icon-view-grid]
         [:button.inline-flex.items-center.px-4.py-2.border.border-gray-300.rounded-md.shadow-sm.text-sm.font-medium.text-gray-700.bg-white.hover:bg-gray-50.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
          {:type "button"} icons/icon-view-list]
         [:button.ml-3.inline-flex.items-center.px-4.py-2.border.border-transparent.rounded-md.shadow-sm.text-sm.font-medium.text-white.bg-indigo-600.hover:bg-indigo-700.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
          {:type "button" :on-click #(add-user-fn)} [:span {:class [styles/flex styles/items-center]}
                                                     icons/icon-add "Add user"]]]]







       [:div.flex.flex-col
        [:div.-my-2.overflow-x-auto.sm:-mx-6.lg:-mx-8
         [:div.py-2.align-middle.inline-block.min-w-full.sm:px-6.lg:px-8
          [:div.shadow.overflow-hidden.border-b.border-gray-200.sm:rounded-lg
           (when @delete-modal-visible
             [components/modal-confirm-delete
              "Confirm the delete ?"
              ""
              (fn []
                ;; TODO: do not convert uuid here, should be done on repository
                (delete-user-fn (uuid @user-id-to-delete))) delete-modal-visible])
           (when loading? [components/load-spinner])
           [:table.min-w-full.divide-y.divide-gray-200
            [:thead.bg-gray-50
             [:tr
              (map (fn [[key {:keys [label sort]}]]
                     ^{:key key} [:th.px-6.py-3.text-left.text-xs.font-medium.text-gray-500.uppercase.tracking-wider {:scope "col"}
                                  [:div {:class [styles/flex styles/items-center]}
                                   (when sort (icons/icon-arrow-up 4 4))
                                   label]])
                   columns)

              [:th.relative.px-6.py-3 {:scope "col"}
               [:span.sr-only "Edit"]]]]


            [:tbody

             (map (fn [{:user/keys [id] :as row}]
                    ^{:key id} [:tr.bg-white
                                [:td.px-6.py-4.whitespace-nowrap.text-right.text-sm.font-medium
                                 {:class [styles/border-b styles/border-gray-200]}
                                 [:input#comments.focus:ring-indigo-500.h-4.w-4.text-indigo-600.border-gray-300.rounded {:aria-describedby "comments-description" :name "comments" :type "checkbox"}]]
                                (map (fn [[k value]]
                                       (let [column-conf (get columns k)
                                             {:keys [format]} column-conf]
                                         ^{:key k} [:td.px-6.py-4.whitespace-nowrap.text-sm.text-gray-500
                                                    {:class [styles/border-b styles/border-gray-200]}
                                                    (if format
                                                      (format value)
                                                      value)]))
                                     row)

                                [:td.px-6.py-4.whitespace-nowrap.text-right.text-sm.font-medium
                                 {:class [styles/border-b styles/border-gray-200]}

                                 [components/button-w-icon {:href     (str "/user/" id "/edit")
                                                            :title    "Edit"} icons/icon-edit]
                                 [components/button-w-icon {:href     (str "/user/" id "/copy")
                                                            :title    "Duplicate"} icons/icon-duplicate]
                                 [components/button-w-icon {:type     "button"
                                                            :title    "Delete"
                                                            :on-click (fn []
                                                                        (reset! delete-modal-visible true)
                                                                        (reset! user-id-to-delete id))} icons/icon-delete]]])
                  rows)
             ]]
           [components/pagination]

           ]]]]]))
  )