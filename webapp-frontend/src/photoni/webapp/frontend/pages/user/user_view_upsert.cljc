(ns photoni.webapp.frontend.pages.user.user-view-upsert
  (:require [reagent.core :as r]
            [re-frame.core :as re-frame :refer [subscribe dispatch reg-event-fx reg-event-db reg-sub]]
            [photoni.webapp.frontend.events :as events]
            [photoni.webapp.frontend.components.components :as components]
            [photoni.webapp.frontend.utils.tailwind-styles :as styles]
            [photoni.webapp.frontend.components.layout :as layout]
            [photoni.webapp.frontend.components.crud.upsert :as crud-upsert]
            [photoni.webapp.frontend.pages.user.user-repository-frontend]
            [photoni.webapp.frontend.pages.user.user-event]
            [photoni.webapp.frontend.pages.common.layout-default :as layout-default]
            [photoni.webapp.frontend.translations.translations :refer [trans]]))

(defn user-view-upsert
  [{:keys [user-id] :as route-params}
   {:keys [form-mode] :as opts}]
  (r/create-class
    {:reagent-render
     (fn user-view-create-render []
       (let [user-row @(subscribe [:user.sub/upsert-user-row])
             status @(subscribe [:user.sub/users-upsert-status])
             errors @(subscribe [:user.sub/users-upsert-errors])]
         [layout-default/layout-view
          [crud-upsert/upsert-view
           {:form-mode form-mode
            :status    status
            :errors    errors
            :row       user-row
            :columns   {:user/id      {:label    (trans :trans.user.field/id)
                                       :pkey     true
                                       :format   (fn [v]
                                                   [:div {:class [styles/overflow-ellipsis
                                                                  styles/truncate
                                                                  styles/w-12]
                                                          :title v}
                                                    v])
                                       :coercion (fn [x] (uuid x))}
                        :user/picture {:label  (trans :trans.user.field/picture)
                                       :format (fn [v]
                                                 [:div.flex-shrink-0.w-10.h-10
                                                  [:img.w-full.h-full.rounded-full {:src "/images/user-picture-sample.jpeg" :alt ""}]])}
                        :user/name    {:label (trans :trans.user.field/name)}
                        :user/title   {:label (trans :trans.user.field/title)}
                        :user/age     {:label (trans :trans.user.field/age) :coercion (fn [x] (js/parseInt x))}
                        :user/email   {:label (trans :trans.user.field/email)}
                        :user/role    {:config {:type :field.config.type/select
                                                :rows [{:key :role/admin :value "Admin"}
                                                       {:key :role/super-admin :value "Super admin"}
                                                       {:key :role/validateur :value "Validateur"}]}
                                       :label  (trans :trans.user.field/role)}}

            :submit-fn (fn [user-fields]
                         (prn "#1 submit-fn user-fields:" user-fields)
                         (dispatch [:user.event/upsert-user user-fields]))
            :cancel-fn (fn [e]
                         (prn "#1 cancel-fn")
                         (dispatch [::events/navigate :view/user]))}]]))

     :component-did-mount
     (fn user-view-create-did-mount [this]
       (when (and (some? user-id)
                  (or (= form-mode :form.mode/edit)
                      (= form-mode :form.mode/copy)))
         (dispatch [:user.event/fetch-user (uuid user-id)])))

     :component-will-unmount
     (fn user-view-create-will-unmount [this]
       (dispatch [:user.event/clear-upsert-user-row]))}))


