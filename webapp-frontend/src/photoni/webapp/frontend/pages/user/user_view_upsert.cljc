(ns photoni.webapp.frontend.pages.user.user-view-upsert
  (:require [reagent.core :as r]
            [re-frame.core :as re-frame :refer [subscribe dispatch reg-event-fx reg-event-db reg-sub]]
            [photoni.webapp.frontend.events :as events]
            [photoni.webapp.frontend.components.components :as components]
            [photoni.webapp.frontend.utils.tailwind-styles :as styles]
            [photoni.webapp.frontend.components.layout :as layout]
            [photoni.webapp.frontend.components.crud.upsert :as crud-upsert]
            [photoni.webapp.frontend.pages.user.user-repository-frontend]
            [photoni.webapp.frontend.pages.user.user-db-sub-event]
            [photoni.webapp.frontend.pages.common.layout-default :as layout-default]))


(defn user-view-upsert
  []
  (r/create-class
    {
     ;;:display-name "users-page"
     :reagent-render
     (fn user-view-create-render []
       (prn "user-view-create-render")
       (let [users-rows @(subscribe [:user.sub/users-rows])
             loading? @(subscribe [:user.sub/users-loading])]
         [layout-default/layout-view
          [crud-upsert/upsert-view
           {:mode        :insert
            :title       "Create a new user"
            :subtitle    ""
            :columns     {:user/id      {:label  "Id"
                                         :pkey   true
                                         :format (fn [v]
                                                   [:div {:class [styles/overflow-ellipsis
                                                                  styles/truncate
                                                                  styles/w-12]
                                                          :title v}
                                                    v])}
                          :user/picture {:label  "Picture"
                                         :format (fn [v]
                                                   [:div.flex-shrink-0.w-10.h-10
                                                    [:img.w-full.h-full.rounded-full {:src "/images/user-picture-sample.jpeg" :alt ""}]])}
                          :user/name    {:label "Name"}
                          :user/title   {:label "Title"}
                          :user/age     {:label "Age" :coercion (fn [x] (prn "coerce age " x) (js/parseInt x))}
                          :user/email   {:label "Email"}
                          :user/role    {:label  "Role"
                                         :format (fn [v]
                                                   [:span.relative.inline-block.px-3.py-1.font-semibold.text-green-900.leading-tight
                                                    [:span.absolute.inset-0.bg-green-200.opacity-50.rounded-full {:aria-hidden "true"}]
                                                    [:span.relative v]])}}

            :row         #:user{:id      #uuid"8350d0df-da16-42f4-b4e9-370b260beedc"
                                :picture "TODO"
                                :name    "name 2"
                                :title   "title"
                                :age     "2"
                                :email   "email@mail.com"
                                :role    "role/admin"}
            :submit-fn   (fn [user-fields]
                           (prn "on submit fn" user-fields)
                           (dispatch [:user.event/upsert-user user-fields]))

            :loading?    loading?
            :add-user-fn #(dispatch [::events/navigate :view/user-upsert])
            :delete-user-fn
                         (fn [user-id]
                           (dispatch [:user.event/delete-user user-id]))}]]))
     :component-did-mount
     (fn user-view-create-did-mount [this]
       )
     :component-will-unmount
     (fn user-view-create-will-unmount [this]
       )}))