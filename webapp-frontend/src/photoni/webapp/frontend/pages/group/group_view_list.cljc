(ns photoni.webapp.frontend.pages.group.group-view-list
  (:require ["@headlessui/react" :refer (Transition)]
            [reagent.core :as r]
            [re-frame.core :as re-frame :refer [subscribe dispatch reg-event-fx reg-event-db reg-sub]]
            [photoni.webapp.frontend.pages.group.group-repository-frontend]
            [photoni.webapp.frontend.events :as events]
            [photoni.webapp.frontend.pages.group.group-event]
            [photoni.webapp.frontend.components.components :as components]
            [photoni.webapp.frontend.utils.tailwind-styles :as styles]
            [photoni.webapp.frontend.components.layout :as layout]
            [photoni.webapp.frontend.pages.common.layout-default :as layout-default]
            [photoni.webapp.frontend.components.crud.list :as crud-list]
            [photoni.webapp.frontend.translations.translations :refer [trans]]))


;;;;;;;;;;;;;; TODO cljs interop https://stackoverflow.com/questions/58977738/javascript-to-clojurescript-interop-with-react-spring
(prn "Transition: " Transition)

(defn group-view-list
  []
  (r/create-class
    {:display-name "groups-page"
     :reagent-render
                   (fn groups-page-render []
                     (let [groups-rows @(subscribe [:group.sub/groups-rows])
                           loading? @(subscribe [:group.sub/groups-loading])]
                       [layout-default/layout-view
                        [crud-list/crud-list
                         {:title    (trans :trans.entity.list/title)
                          :trans    {:trans.entity.list/title (trans :trans.group.list/title)}
                          :rows     (map (fn [{:group/keys [id title name age email role]}]
                                           {:id   id
                                            :name name})
                                         groups-rows)
                          :columns  {:select {:label ""
                                              :sort  false}
                                     :id     {:label  (trans :trans.group.field/id) :sort true
                                              :format (fn [v]
                                                        [:div {:class [styles/overflow-ellipsis
                                                                       styles/truncate
                                                                       styles/w-12]
                                                               :title v}
                                                         v])}
                                     :name   {:label (trans :trans.group.field/name) :sort true}}
                          :loading? loading?
                          :add-url  (fn [] (str "/group/insert"))
                          :edit-url (fn [group-id] (str "/group/" group-id "/edit"))
                          :copy-url (fn [group-id] (str "/group/" group-id "/copy"))
                          :delete-group-fn
                                    (fn [group-id]
                                      (prn "delete-group-fn group-id" group-id)
                                      (dispatch [:group.event/delete-group group-id]))}]]))
     :component-did-mount
                   (fn groups-page-did-mount [this]
                     (dispatch [:group.event/fetch-groups]))
     :component-will-unmount
                   (fn groups-page-will-unmount [this]
                     (prn "groups-page-will-unmount")
                     )}))