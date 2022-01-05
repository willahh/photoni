(ns photoni.webapp.frontend.components.crud.upsert
  (:require [photoni.webapp.frontend.components.components :as components]
            [photoni.webapp.frontend.utils.tailwind-styles :as styles]
            [photoni.webapp.frontend.components.icons :as icons]
            [reagent.core :as r]))

(defn upsert-view
  [{:keys [mode
           title
           subtitle
           columns
           row
           submit-fn

           add-user-fn
           delete-user-fn
           go-to-home-fn
           go-to-about-fn
           go-to-user-fn
           loading?]}]
  (let [form-state (atom {:user/id   "1"
                          :user/name "name"})
        row (r/atom row)]
    (fn [{:keys [mode
                 title
                 subtitle
                 columns
                 submit-fn

                 add-user-fn
                 delete-user-fn
                 go-to-home-fn
                 go-to-about-fn
                 go-to-user-fn
                 loading?]}]

      (do "TODO DEBUG"
          (def columns columns)
          (def row row))

      [:form.space-y-8.divide-y.divide-gray-200
       {:on-submit (fn [e]
                     (prn "on submit")
                     (reduce (fn [acc [key {:keys [label pkey coercion] :as column-conf}]]

                               (let [value (get @row key)]
                                 (assoc acc key (if (fn? coercion)
                                                  (coercion value)
                                                  value))
                                 ))
                             {}
                             columns)
                     (.preventDefault e)
                     (submit-fn @row))}
       [:div.space-y-8.divide-y.divide-gray-200
        [:div.pt-8
         [:div
          [:h3.text-lg.leading-6.font-medium.text-gray-900 title]
          [:p.mt-1.text-sm.text-gray-500 subtitle]]
         [:div.mt-6.grid.grid-cols-1.gap-y-6.gap-x-4.sm:grid-cols-6

          (doall
            (keep
              (fn [[key {:keys [label pkey coercion] :as column-conf}]]
                (let [row-value (get @row key)]
                  (when-not pkey
                    ^{:key key}
                    [:div.sm:col-span-3
                     [:label.block.text-sm.font-medium.text-gray-700 {:for key} label]
                     [:div.mt-1
                      [:input#first-name.shadow-sm.focus:ring-indigo-500.focus:border-indigo-500.block.w-full.sm:text-sm.border-gray-300.rounded-md
                       {:type      "text"
                        :name      key
                        ;; :auto-complete "given-name"
                        :value     row-value
                        :on-change (fn [e]
                                     (prn "onchange (fn? coercion):" (fn? coercion))
                                     (let [value (-> e .-target .-value)
                                           value (if (fn? coercion)
                                                   (coercion value)
                                                   value)]
                                       (swap! row assoc key value)))}]]])))
              columns))


          [:div
           [:button.w-full.flex.justify-center.py-2.px-4.border.border-transparent.rounded-md.shadow-sm.text-sm.font-medium.text-white.bg-indigo-600.hover:bg-indigo-700.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
            {:type "submit"} "Create"]
           [:button.w-full.flex.justify-center.py-2.px-4.border.border-transparent.rounded-md.shadow-sm.text-sm.font-medium.text-white.bg-indigo-600.hover:bg-indigo-700.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
            {:type "submit"} "Cancel"]]

          ]]

        ]]))
  )