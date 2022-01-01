(ns photoni.webapp.frontend.pages.user.users-page
  (:require ["@headlessui/react" :refer (Transition)]
            [reagent.core :as r]
            [re-frame.core :as re-frame :refer [subscribe dispatch reg-event-fx reg-event-db reg-sub]]
            [photoni.webapp.frontend.pages.user.user-repository-frontend]
            [photoni.webapp.frontend.pages.user.user-db-sub-event]
            [photoni.webapp.frontend.components.components :as components]
    #_[photoni.webapp.frontend.domain.user :as domain-user]
            [photoni.webapp.frontend.utils.tailwind-styles :as styles]
            [photoni.webapp.frontend.components.layout :as layout]
            [photoni.webapp.frontend.pages.common.layout-default :as layout-default]
            [photoni.webapp.frontend.components.crud.list :as crud-list]))


;;;;;;;;;;;;;; TODO cljs interop https://stackoverflow.com/questions/58977738/javascript-to-clojurescript-interop-with-react-spring
(prn "Transition: " Transition)

(defn users-page
  []
  (r/create-class
    {:display-name "users-page"
     :reagent-render
                   (fn users-page-render []
                     (let [users-rows @(subscribe [:user.sub/users-rows])
                           loading? @(subscribe [:user.sub/users-loading])]
                       [layout-default/layout-view
                        [crud-list/crud-list
                         {:title    "Users"
                          :rows     (map (fn [{:user/keys [id title name age email role]}]
                                           #:user{:id      id
                                                  :picture "TODO"
                                                  :name    name
                                                  :title   title
                                                  :age     age
                                                  :email   email
                                                  :role    role})
                                         users-rows)
                          :columns  {:user/id      {:label "Id" :format (fn [v]
                                                                          [:div {:class [styles/overflow-ellipsis
                                                                                         styles/truncate
                                                                                         styles/w-12]
                                                                                 :title v}
                                                                           v])}
                                     :user/picture {:label "" :format (fn [v]
                                                                        [:div.flex-shrink-0.w-10.h-10
                                                                         [:img.w-full.h-full.rounded-full {:src "/images/user-picture-sample.jpeg" :alt ""}]])}
                                     :user/name    {:label "Name"}
                                     :user/title   {:label "Title"}
                                     :user/age     {:label "Age"}
                                     :user/email   {:label "Email"}
                                     :user/role    {:label "Role" :format (fn [v]
                                                                            [:span.relative.inline-block.px-3.py-1.font-semibold.text-green-900.leading-tight
                                                                             [:span.absolute.inset-0.bg-green-200.opacity-50.rounded-full {:aria-hidden "true"}]
                                                                             [:span.relative v]])}}
                          :loading? loading?
                          :add-user-fn
                                    (fn []
                                      #_(let [user (domain-user/generate-user-stub)]
                                          (dispatch [:user.event/add-user user])))
                          :delete-user-fn
                                    (fn [user-id]
                                      (dispatch [:user.event/delete-user user-id]))}]]))
     :component-did-mount
                   (fn users-page-did-mount [this]
                     (dispatch [:user.event/fetch-users]))
     :component-will-unmount
                   (fn users-page-will-unmount [this]
                     (prn "users-page-will-unmount")
                     )}))