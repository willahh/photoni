(ns photoni.webapp.frontend.components.crud.upsert
  (:require [photoni.webapp.domain.utils :as utils]
            [photoni.webapp.frontend.components.components :as components]
            [photoni.webapp.frontend.utils.tailwind-styles :as styles]
            [photoni.webapp.frontend.components.icons :as icons]
            [re-frame.core :as re-frame :refer [subscribe dispatch reg-event-fx reg-event-db reg-sub]]
            [reagent.core :as r]
            [photoni.webapp.frontend.translations.translations :refer [trans]]))

(defn input-skeleton-loader
  []
  [:div.animate-pulse.flex
   [:div.flex-1
    [:div {:class [styles/h-6 styles/bg-gray-200 styles/rounded]}]]])

(defn button-submit
  [status label]
  (cond
    (= status :form.status/default)
    [:button.ml-3.inline-flex.justify-center.py-2.px-4.border.border-transparent.shadow-sm.text-sm.font-medium.rounded-md.text-white.bg-indigo-600.hover:bg-indigo-700.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
     {:type "submit"} label]

    (= status :form.status/processing)
    [:button.ml-3.inline-flex.justify-center.py-2.px-4.border.border-transparent.shadow-sm.text-sm.font-medium.rounded-md.text-white.bg-indigo-600.hover:bg-indigo-700.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
     {:type "submit" :disabled true}
     [:svg.animate-spin.h-5.w-5.mr-3.... {:viewBox "0 0 24 24"}] "Processing..."]

    (= status :form.status/loading)
    [:button.ml-3.inline-flex.justify-center.py-2.px-4.border.border-transparent.shadow-sm.text-sm.font-medium.rounded-md.text-white.bg-indigo-600.hover:bg-indigo-700.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
     {:class [styles/flex styles/animate-pulse]}
     [:span {:class [styles/bg-gray-200 styles/rounded]}]]))

(defn button-cancel
  [status label cancel-fn]
  (cond
    (= status :form.status/default)
    [:button.bg-white.py-2.px-4.border.border-gray-300.rounded-md.shadow-sm.text-sm.font-medium.text-gray-700.hover:bg-gray-50.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
     {:type "button" :on-click (fn [e] (cancel-fn e))} label]

    (= status :form.status/processing)
    [:button.bg-white.py-2.px-4.border.border-gray-300.rounded-md.shadow-sm.text-sm.font-medium.text-gray-700.hover:bg-gray-50.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
     {:type "button" :disabled true} label]

    (= status :form.status/loading)
    [:button.bg-white.py-2.px-4.border.border-gray-300.rounded-md.shadow-sm.text-sm.font-medium.text-gray-700.hover:bg-gray-50.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500
     {:class [styles/flex styles/animate-pulse]}
     [:span {:class [styles/bg-gray-200 styles/rounded]}]]))

(defn form-validation-row
  [form-status form-mode cancel-fn]
  [:div.pt-5
   [:div.flex.justify-end
    [button-cancel form-status "Cancel" cancel-fn]
    (let [label (case form-mode
                  :form.mode/insert "Create"
                  :form.mode/copy "Create"
                  :form.mode/edit "Update")]
      [button-submit form-status label])]])


(defn field-text
  [{:keys [form-row-state editable? form-status errors error? key value]}]
  (cond
    (= form-status :form.status/loading)
    [input-skeleton-loader]

    (or (= form-status :form.status/default)
        (= form-status :form.status/processing))
    [:span [:input#first-name.shadow-sm.focus:ring-indigo-500.focus:border-indigo-500.block.w-full.sm:text-sm.border-gray-300.rounded-md
            {:type      "text"
             :class     [(when error? styles/border)
                         (when error? styles/border-red-500)]
             :name      key
             :value     value
             :on-change (fn [e]
                          (prn "onchange :")
                          (let [value (-> e .-target .-value)]
                            (when editable?
                              (swap! form-row-state assoc key value))))}]
     (when-let [error-messages (get-in errors [:fields key])]
       [:div.flex.items-center.font-medium.tracking-wide.text-red-500.text-xs.mt-1.ml-1
        (clojure.string/join " - " error-messages)])]))


(defn field-error-message
  [errors key]
  (when-let [error-messages (get-in errors [:fields key])]
    [:div.flex.items-center.font-medium.tracking-wide.text-red-500.text-xs.mt-1.ml-1
     (clojure.string/join " - " error-messages)]))



(defmulti field (fn [{:keys [config]}]
                  (or (:type config)
                      :field.config.type/text-field)))

(defmethod field :field.config.type/text-field
  [{:keys [form-row-state editable? form-status errors error? key value]}]
  (cond
    (= form-status :form.status/loading)
    [input-skeleton-loader]

    (or (= form-status :form.status/default)
        (= form-status :form.status/processing))
    [:span [:input#first-name.shadow-sm.focus:ring-indigo-500.focus:border-indigo-500.block.w-full.sm:text-sm.border-gray-300.rounded-md
            {:type      "text"
             :class     [(when error? styles/border)
                         (when error? styles/border-red-500)]
             :name      key
             :value     value
             :on-change (fn [e]
                          (prn "onchange :")
                          (let [value (-> e .-target .-value)]
                            (when editable?
                              (swap! form-row-state assoc key value))))}]
     [field-error-message errors key]]))


(defmethod field :field.config.type/select
  [{:keys [form-row-state editable? form-status errors error? key value config]}]
  (cond
    (= form-status :form.status/loading)
    [input-skeleton-loader]

    (or (= form-status :form.status/default)
        (= form-status :form.status/processing))
    [:span
     (into [:select.shadow-sm.focus:ring-indigo-500.focus:border-indigo-500.block.w-full.sm:text-sm.border-gray-300.rounded-md
            {:class     [(when error? styles/border)
                         (when error? styles/border-red-500)]
             :name      key
             :value     value
             :on-change (fn [e]
                          (prn "onchange :")
                          (let [value (-> e .-target .-value)]
                            (when editable?
                              (swap! form-row-state assoc key value))))}]
           (concat [[:option {:value ""} ""]]
                   (map (fn [{:keys [key value] :as row}]
                          [:option {:value (utils/kw->str key)} value])
                        (:rows config))))
     [field-error-message errors key]]))



(defn upsert-view
  [{:keys [form-mode
           status
           errors
           columns
           row
           submit-fn
           cancel-fn]}]
  (let [form-row-state (r/atom {})]
    (fn [{:keys [form-mode
                 status
                 errors
                 columns
                 row
                 submit-fn
                 cancel-fn]}]
      (let [form-row-state-empty? (empty? @form-row-state)
            form-editable? (or (not= form-mode :upsert.mode/edit) (not form-row-state-empty?))]
        (when form-row-state-empty?
          (reset! form-row-state row))
        [:form.space-y-8.divide-y.divide-gray-200
         {:on-submit (fn [e]
                       (prn "on submit")
                       (.preventDefault e)
                       (let [row (reduce (fn [acc [key {:keys [label pkey coercion] :as column-conf}]]
                                           (let [value (get @form-row-state key)

                                                 ;; Don't submit primary key field in insert mode or copy mode
                                                 add-field? (if (or (= form-mode :form.mode/insert)
                                                                    (= form-mode :form.mode/copy))
                                                              true
                                                              (not pkey))]
                                             (if add-field?
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
            [:h3.text-lg.leading-6.font-medium.text-gray-900
             (case form-mode
               :form.mode/insert (trans :trans.user.insert/title)
               :form.mode/copy (trans :trans.user.copy/title)
               :form.mode/edit (trans :trans.user.edit/title))]
            [:p.mt-1.text-sm.text-gray-500
             (case form-mode
               :form.mode/insert (trans :trans.user.insert/subtitle)
               :form.mode/copy (trans :trans.user.copy/subtitle)
               :form.mode/edit (trans :trans.user.edit/subtitle))]]
           [:div.mt-6.grid.grid-cols-1.gap-y-6.gap-x-4.sm:grid-cols-6
            (doall
              (keep
                (fn [[key {:keys [label pkey coercion config]
                           :as   column-conf}]]
                  (let [row-value (get @form-row-state key)
                        errors2 (get-in errors [:fields key])]
                    (when-not pkey
                      ^{:key key}
                      [:div.sm:col-span-3
                       [:label.block.text-sm.font-medium.text-gray-700 {:for key} label]
                       [:div.mt-1
                        [field {:config         config
                                :key            key
                                :value          row-value
                                :form-row-state form-row-state
                                :editable?      form-editable?
                                :form-status    status
                                :errors         errors
                                :error?         errors2}]
                        ]])))
                columns))]]]
         [form-validation-row status form-mode cancel-fn]]))))