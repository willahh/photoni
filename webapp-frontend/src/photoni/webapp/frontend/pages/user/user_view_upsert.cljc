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
            [photoni.webapp.frontend.pages.common.layout-default :as layout-default]))

(defn user-view-upsert
  [{:keys [user-id] :as route-params}]
  (r/create-class
    {
     ;;:display-name "users-page"
     :reagent-render
     (fn user-view-create-render []
       (let [update? (some? user-id)
             user-row @(subscribe [:user.sub/upsert-user-row])
             status @(subscribe [:user.sub/users-upsert-status])
             errors @(subscribe [:user.sub/users-upsert-errors])]
         [layout-default/layout-view
          [crud-upsert/upsert-view
           {:update?         update?
            :status          status
            :errors          errors
            :row             user-row
            :title           "Create a new user"
            :subtitle        ""
            :update-title    "Update an user"
            :update-subtitle ""
            :columns         {:user/id      {:label    "Id"
                                             :pkey     true
                                             :format   (fn [v]
                                                         [:div {:class [styles/overflow-ellipsis
                                                                        styles/truncate
                                                                        styles/w-12]
                                                                :title v}
                                                          v])
                                             :coercion (fn [x] (uuid x))}
                              :user/picture {:label  "Picture"
                                             :format (fn [v]
                                                       [:div.flex-shrink-0.w-10.h-10
                                                        [:img.w-full.h-full.rounded-full {:src "/images/user-picture-sample.jpeg" :alt ""}]])}
                              :user/name    {:label "Name"}
                              :user/title   {:label "Title"}
                              :user/age     {:label "Age" :coercion (fn [x] (prn "coerce age " x) (js/parseInt x))}
                              :user/email   {:label "Email"}
                              :user/role    {:config {:type :field.config.type/select
                                                      :rows [{:key :role/admin :value "Admin"}
                                                             {:key :role/super-admin :value "Super admin"}
                                                             {:key :role/validateur :value "Validateur"}]}
                                             :label  "Role"}}

            :submit-fn       (fn [user-fields]
                               (prn "#1 submit-fn user-fields:" user-fields)
                               (dispatch [:user.event/upsert-user user-fields]))
            :add-user-fn     #(dispatch [::events/navigate :view/user-insert])
            :delete-user-fn
                             (fn [user-id]
                               (dispatch [:user.event/delete-user user-id]))}]]))

     :component-did-mount
     (fn user-view-create-did-mount [this]
       (let [update? (some? user-id)]
         (when update?
           (dispatch [:user.event/fetch-user (uuid user-id)]))))

     :component-will-unmount
     (fn user-view-create-will-unmount [this]
       (prn "user-view-create-will-unmount")
       (dispatch [:user.event/clear-upsert-user-row])
       )}))


(comment
  (do
    (def opts {:field.config/type :field.config.type/select
               :field.config/rows [{:key :role/admin :value "Admin"}
                                   {:key :role/super-admin :value "Super admin"}
                                   {:key :role/validateur :value "Validateur"}]
               :label             "Role"})

    (let [{:keys              [label]
           :field.config/keys [rows type]} opts]
      type))




  (do
    (def human {:person/name       "Franklin"
                :person/age        25
                :hobby/name        "running"
                :field.config/rows ["a"]})
    (let [{:person/keys       [age]
           :field.config/keys [rows]
           hobby-name         :hobby/name
           person-name        :person/name} human]
      rows))

  )