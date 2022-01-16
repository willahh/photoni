(ns photoni.webapp.frontend.pages.group.group-view-upsert
  (:require [reagent.core :as r]
            [re-frame.core :as re-frame :refer [subscribe dispatch reg-event-fx reg-event-db reg-sub]]
            [photoni.webapp.frontend.events :as events]
            [photoni.webapp.frontend.components.components :as components]
            [photoni.webapp.frontend.utils.tailwind-styles :as styles]
            [photoni.webapp.frontend.components.layout :as layout]
            [photoni.webapp.frontend.components.crud.upsert :as crud-upsert]
            [photoni.webapp.frontend.pages.group.group-repository-frontend]
            [photoni.webapp.frontend.pages.group.group-event]
            [photoni.webapp.frontend.pages.common.layout-default :as layout-default]
            [photoni.webapp.frontend.translations.translations :refer [trans]]))

(defn group-view-upsert
  [{:keys [group-id] :as route-params}
   {:keys [form-mode] :as opts}]
  (r/create-class
    {:reagent-render
     (fn group-view-create-render []
       (let [group-row @(subscribe [:group.sub/upsert-group-row])
             status @(subscribe [:group.sub/groups-upsert-status])
             errors @(subscribe [:group.sub/groups-upsert-errors])]
         [layout-default/layout-view
          [crud-upsert/upsert-view
           {:form-mode form-mode
            :status    status
            :errors    errors
            :row       group-row
            :columns   {:id   {:label    (trans :trans.group.field/id)
                               :pkey     true
                               :format   (fn [v]
                                           [:div {:class [styles/overflow-ellipsis
                                                          styles/truncate
                                                          styles/w-12]
                                                  :title v}
                                            v])
                               :coercion (fn [x] (uuid x))}
                        :name {:label (trans :trans.group.field/name)}}

            :submit-fn (fn [group-fields]
                         (prn "#1 submit-fn group-fields:" group-fields)
                         (dispatch [:group.event/upsert-group group-fields]))
            :cancel-fn (fn [e]
                         (prn "#1 cancel-fn")
                         (dispatch [::events/navigate :view/group]))}]]))

     :component-did-mount
     (fn group-view-create-did-mount [this]
       (when (and (some? group-id)
                  (or (= form-mode :form.mode/edit)
                      (= form-mode :form.mode/copy)))
         (dispatch [:group.event/fetch-group (uuid group-id)])))

     :component-will-unmount
     (fn group-view-create-will-unmount [this]
       (dispatch [:group.event/clear-upsert-group-row]))}))


