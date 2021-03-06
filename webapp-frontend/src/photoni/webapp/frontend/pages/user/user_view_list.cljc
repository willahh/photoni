(ns photoni.webapp.frontend.pages.user.user-view-list
  (:require ["@headlessui/react" :refer (Transition)]
            [reagent.core :as r]
            [re-frame.core :as re-frame :refer [subscribe dispatch reg-event-fx reg-event-db reg-sub]]
            [photoni.webapp.frontend.pages.user.user-repository-frontend]
            [photoni.webapp.frontend.events :as events]
            [photoni.webapp.frontend.pages.user.user-event]
            [photoni.webapp.frontend.components.components :as components]
    #_[photoni.webapp.frontend.domain.user :as domain-user]
            [photoni.webapp.frontend.utils.tailwind-styles :as styles]
            [photoni.webapp.frontend.components.layout :as layout]
            [photoni.webapp.frontend.pages.common.layout-default :as layout-default]
            [photoni.webapp.frontend.components.crud.list :as crud-list]
            [photoni.webapp.frontend.translations.translations :refer [trans]]))


;;;;;;;;;;;;;; TODO cljs interop https://stackoverflow.com/questions/58977738/javascript-to-clojurescript-interop-with-react-spring
(prn "Transition: " Transition)

(defn user-view-list
  []
  (r/create-class
    {:display-name "users-page"
     :reagent-render
                   (fn users-page-render []
                     (let [users-rows @(subscribe [:user.sub/users-rows])
                           loading? @(subscribe [:user.sub/users-loading])]
                       [layout-default/layout-view
                        [crud-list/crud-list
                         {:title      (trans :trans.user.list/title)
                          :trans-opts {:trans.entity.list/title     (trans :trans.user.list/title)
                                       :trans.entity.list/add       (trans :trans.user.list/add)
                                       :trans.entity.list/filter    (trans :trans.list/filter)
                                       :trans.entity.list/view-grid (trans :trans.list/view-grid)
                                       :trans.entity.list/view-list (trans :trans.list/view-list)}
                          :rows       (map (fn [{:user/keys [id title name age email role]}]
                                             {:id      id
                                              :picture "TODO"
                                              :name    name
                                              :title   title
                                              :age     age
                                              :email   email
                                              :role    role})
                                           users-rows)
                          :columns    {:select  {:label ""
                                                 :sort  false}
                                       :id      {:label  (trans :trans.user.field/id) :sort true
                                                 :format (fn [v]
                                                           [:div {:class [styles/overflow-ellipsis
                                                                          styles/truncate
                                                                          styles/w-12]
                                                                  :title v}
                                                            v])}
                                       :picture {:label  (trans :trans.user.field/picture) :sort true
                                                 :format (fn [v]
                                                           [:div.flex-shrink-0.w-10.h-10
                                                            [:img.w-full.h-full.rounded-full {:src "/images/user-picture-sample.jpeg" :alt ""}]])}
                                       :name    {:label (trans :trans.user.field/name) :sort true}
                                       :title   {:label (trans :trans.user.field/title) :sort true}
                                       :age     {:label (trans :trans.user.field/age) :sort true}
                                       :email   {:label (trans :trans.user.field/email) :sort true}
                                       :role    {:label  (trans :trans.user.field/role) :sort true
                                                 :format (fn [v]
                                                           [:span.relative.inline-block.px-3.py-1.font-semibold.text-green-900.leading-tight
                                                            [:span.absolute.inset-0.bg-green-200.opacity-50.rounded-full {:aria-hidden "true"}]
                                                            [:span.relative v]])}}
                          :loading?   loading?
                          :add-url    (fn [] (str "/user/insert"))
                          :edit-url   (fn [user-id] (str "/user/" user-id "/edit"))
                          :copy-url   (fn [user-id] (str "/user/" user-id "/copy"))
                          :delete-entity-fn
                                      (fn [user-id]
                                        (prn "delete-user-fn user-id" user-id)
                                        (dispatch [:user.event/delete-user user-id]))}]]))
     :component-did-mount
                   (fn users-page-did-mount [this]
                     (dispatch [:user.event/fetch-users]))
     :component-will-unmount
                   (fn users-page-will-unmount [this]
                     (prn "users-page-will-unmount")
                     )}))