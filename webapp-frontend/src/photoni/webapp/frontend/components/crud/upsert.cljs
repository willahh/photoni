(ns photoni.webapp.frontend.components.crud.upsert
  (:require [photoni.webapp.frontend.components.components :as components]
            [photoni.webapp.frontend.utils.tailwind-styles :as styles]
            [photoni.webapp.frontend.components.icons :as icons]
            [re-frame.core :as re-frame :refer [subscribe dispatch reg-event-fx reg-event-db reg-sub]]
            [reagent.core :as r]))

(defn input-skeleton-loader
  []
  [:div.animate-pulse.flex
   [:div.flex-1
    [:div {:class [styles/h-6 styles/bg-gray-200 styles/rounded]}]]])

(defn button-submit
  [status label]
  (cond
    (= status :form.status/default)
    [:button.w-full.flex.justify-center.py-2.px-4.border.border-transparent.rounded-md.shadow-sm.text-sm.font-medium.text-white.bg-indigo-600.hover:bg-indigo-700.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
     {:type "submit"}
     label]

    (= status :form.status/processing)
    [:button.w-full.flex.justify-center.py-2.px-4.border.border-transparent.rounded-md.shadow-sm.text-sm.font-medium.text-white.bg-indigo-600.hover:bg-indigo-700.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
     {:type "button" :disabled true}
     [:svg.animate-spin.h-5.w-5.mr-3.... {:viewBox "0 0 24 24"}] "Processing..."]

    (= status :form.status/loading)
    [:span.w-full.flex.justify-center.py-2.px-4.border.border-transparent.rounded-md.shadow-sm.text-sm.font-medium.text-white.bg-indigo-600.hover:bg-indigo-700.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
     {:class [styles/flex styles/animate-pulse]}
     [:span {:class [styles/bg-gray-200 styles/rounded]}]]))

(defn button-cancel
  [status label]
  (cond
    (= status :form.status/default)
    [:button.w-full.flex.justify-center.py-2.px-4.border.border-transparent.rounded-md.shadow-sm.text-sm.font-medium.text-white.bg-indigo-600.hover:bg-indigo-700.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
     {:type "submit"} label]

    (= status :form.status/processing)
    [:button.w-full.flex.justify-center.py-2.px-4.border.border-transparent.rounded-md.shadow-sm.text-sm.font-medium.text-white.bg-indigo-600.hover:bg-indigo-700.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
     {:type "submit" :disabled true}
     label]

    (= status :form.status/loading)
    [:span.w-full.flex.justify-center.py-2.px-4.border.border-transparent.rounded-md.shadow-sm.text-sm.font-medium.text-white.bg-indigo-600.hover:bg-indigo-700.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
     {:class [styles/flex styles/animate-pulse]}
     [:span {:class [styles/bg-gray-200 styles/rounded]}]]))






(defn upsert-view
  [{:keys [update?
           title
           subtitle
           update-title
           update-subtitle
           status
           columns
           row
           submit-fn
           add-user-fn
           delete-user-fn
           go-to-home-fn
           go-to-about-fn
           go-to-user-fn]}]
  (let [form-row-state (r/atom {})
        ]
    (prn ">>>>>>>>> 01 upsert-view RENDER form-row:" form-row-state)
    (fn [{:keys [update?
                 title
                 subtitle
                 update-title
                 update-subtitle
                 status
                 columns
                 row
                 submit-fn
                 add-user-fn
                 delete-user-fn
                 go-to-home-fn
                 go-to-about-fn
                 go-to-user-fn]}]
      (let [form-row-state-empty? (empty? @form-row-state)
            form-editable? (or (not update?) (not form-row-state-empty?))]
        (when form-row-state-empty?
          (reset! form-row-state row))

        (prn "#2 status" status)


        [:form.space-y-8.divide-y.divide-gray-200
         {:on-submit (fn [e]
                       (prn "on submit")
                       (.preventDefault e)
                       #_(reset! form-processing? true)
                       (let [row (reduce (fn [acc [key {:keys [label pkey coercion] :as column-conf}]]
                                           (let [value (get @form-row-state key)]
                                             (if (not pkey)
                                               (assoc acc key (if (fn? coercion)
                                                                (coercion value)
                                                                value))
                                               acc)))
                                         {}
                                         columns)]
                         (submit-fn row)))}

         [:div.space-y-8.divide-y.divide-gray-200
          [:div.pt-8
           [:div
            [:h3.text-lg.leading-6.font-medium.text-gray-900 (if update? update-title title)]
            [:p.mt-1.text-sm.text-gray-500 (if update? update-subtitle subtitle)]]
           [:div.mt-6.grid.grid-cols-1.gap-y-6.gap-x-4.sm:grid-cols-6
            (doall
              (keep
                (fn [[key {:keys [label pkey coercion] :as column-conf}]]
                  (let [row-value (get @form-row-state key)]
                    (when-not pkey
                      ^{:key key}
                      [:div.sm:col-span-3
                       [:label.block.text-sm.font-medium.text-gray-700 {:for key} label]
                       [:div.mt-1
                        (cond
                          (= status :form.status/loading)
                          [input-skeleton-loader]

                          (or (= status :form.status/default)
                              (= status :form.status/processing))
                          [:input#first-name.shadow-sm.focus:ring-indigo-500.focus:border-indigo-500.block.w-full.sm:text-sm.border-gray-300.rounded-md
                           {:type      "text"
                            :name      key
                            ;; :auto-complete "given-name"
                            :value     row-value
                            :on-change (fn [e]
                                         (prn "onchange :")
                                         (let [value (-> e .-target .-value)]
                                           (when form-editable?
                                             (swap! form-row-state assoc key value))))}])
                        ]])))
                columns))

            [:div
             (let [label (if update? "Update" "Create")]
               [button-submit status label])
             [button-cancel status "Cancel"]





             ]

            ]]

          ]])))
  )