(ns photoni.webapp.frontend.pages.home.home
  (:require
    [re-frame.core :as re-frame :refer [subscribe dispatch]]
    #_[photoni.webapp.frontend.domain.user :as domain-user]
    [photoni.webapp.frontend.pages.common.layout-default :as layout-default]))

(re-frame/reg-sub
  ::name
  (fn [db]
    (:name db)))

(defn home-panel []
  (let [name (subscribe [::name])]
    [layout-default/layout-view
     [:div
      "home"]]))
