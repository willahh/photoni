(ns photoni.webapp.frontend.views
  (:require
    [re-frame.core :as re-frame]
    [re-frame.core :refer [subscribe dispatch]]
    [photoni.webapp.frontend.routes :as routes]
    [photoni.webapp.frontend.pages.home.home :as page-home]
    [photoni.webapp.frontend.pages.about.about :as page-about]
    [photoni.webapp.frontend.pages.user.user-view-list :refer [user-view-list]]
    [photoni.webapp.frontend.pages.user.user-view-upsert :refer [user-view-upsert]]))


(defmethod routes/panels :view/home [_ route-params]
  [page-home/home-panel])

(defmethod routes/panels :view/about [_ route-params]
  [page-about/about-panel])

(defmethod routes/panels :view/user [_ route-params]
  [user-view-list])

(defmethod routes/panels :view/user-edit [_ route-params]
  (prn "routes/panels :view/user-edit" "route-params:" route-params)
  [user-view-upsert route-params])

(defmethod routes/panels :view/user-insert [_ route-params]
  [user-view-upsert])

(re-frame/reg-sub ::active-panel (fn [db _] (:active-panel db)))
(re-frame/reg-sub ::route-params (fn [db _] (:route-params db)))

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::active-panel])
        route-params (re-frame/subscribe [::route-params])]
    (routes/panels @active-panel @route-params)))
