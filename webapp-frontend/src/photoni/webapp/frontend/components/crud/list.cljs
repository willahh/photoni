(ns photoni.webapp.frontend.components.crud.list
  (:require [photoni.webapp.frontend.components.components :as components]
            [photoni.webapp.frontend.utils.tailwind-styles :as styles]
            [photoni.webapp.frontend.components.icons :as icons]
            [reagent.core :as r]
            [photoni.webapp.frontend.translations.translations :refer [trans]]))

(defn crud-list
  [{:keys [title
           trans-opts
           rows
           columns
           delete-entity-fn
           loading?
           add-url
           edit-url
           copy-url
           ]}]

  (let [delete-modal-visible (r/atom false)
        entity-id-to-delete (r/atom nil)]
    (fn [{:keys [title
                 trans-opts
                 rows
                 columns
                 delete-entity-fn
                 loading?
                 add-url
                 edit-url
                 copy-url]}]
      [:div {:class [styles/md:relative]}



       ;; top toolbar
       [:div.pb-5.border-b.border-gray-200.sm:flex.sm:items-center.sm:justify-between
        [:h2.text-lg.leading-6.font-medium.text-gray-900
         title]
        [:div.mt-3.flex.sm:mt-0.sm:ml-4
         [:button.inline-flex.items-center.px-4.py-2.border.border-gray-300.rounded-md.shadow-sm.text-sm.font-medium.text-gray-700.bg-white.hover:bg-gray-50.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
          {:type "button" :title (:trans.entity.list/filter trans-opts)}
          icons/icon-filter]
         [:button.inline-flex.items-center.px-4.py-2.border.border-gray-300.rounded-md.shadow-sm.text-sm.font-medium.text-gray-700.bg-white.hover:bg-gray-50.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
          {:type "button" :title (:trans.entity.list/view-grid trans-opts)}
          icons/icon-view-grid]
         [:button.inline-flex.items-center.px-4.py-2.border.border-gray-300.rounded-md.shadow-sm.text-sm.font-medium.text-gray-700.bg-white.hover:bg-gray-50.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
          {:type "button" :title (:trans.entity.list/view-list trans-opts)}
          icons/icon-view-list]
         [:a.ml-3.inline-flex.items-center.px-4.py-2.border.border-transparent.rounded-md.shadow-sm.text-sm.font-medium.text-white.bg-indigo-600.hover:bg-indigo-700.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
          {:href (add-url)}
          [:span {:class [styles/flex styles/items-center]}
           icons/icon-add (:trans.entity.list/add trans-opts)]]]]









       [:div.flex.flex-col
        [:div.-my-2.overflow-x-auto.sm:-mx-6.lg:-mx-8
         [:div.py-2.align-middle.inline-block.min-w-full.sm:px-6.lg:px-8
          [:div.shadow.overflow-hidden.border-b.border-gray-200.sm:rounded-lg
           (when @delete-modal-visible
             [components/modal-confirm-delete
              (trans :trans.entity.list/confirm-delete)
              ""
              (fn []
                (reset! delete-modal-visible false)
                (delete-entity-fn (uuid @entity-id-to-delete))) delete-modal-visible])
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
             (map (fn [{:keys [id] :as row}]
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

                                 [components/button-w-icon {:href  (edit-url id)
                                                            :title "Edit"} icons/icon-edit]
                                 [components/button-w-icon {:href  (copy-url id)
                                                            :title "Duplicate"} icons/icon-duplicate]
                                 [components/button-w-icon {:type     "button"
                                                            :title    "Delete"
                                                            :on-click (fn []
                                                                        (reset! delete-modal-visible true)
                                                                        (reset! entity-id-to-delete id))} icons/icon-delete]]])
                  rows)
             ]]
           [components/pagination]


           [:div {:class [styles/flex styles/items-center styles/gap-4]}
            [:div "Actions sur la sélection"]
            [:div {:styles {:width "100px"}}
             [components/select
              {:width "100px"}
              [[:option {:name ""} "-"]
               [:option {:name :action.list/delete} "Supprimer"]
               [:option {:name :action.list/edit} "Modifier"]
               [:option {:name :action.list/export} "Exporter au format CSV"]]]]
            [components/button-secondary :form.status/default "ok" (fn [e] (prn "click"))]]

           ]]]]]))
  )