(ns photoni.webapp.frontend.views
  (:require
    [re-frame.core :as re-frame]
    [re-frame.core :refer [subscribe dispatch]]
    [photoni.webapp.frontend.routes :as routes]
    [photoni.webapp.frontend.pages.home.home :as page-home]
    [photoni.webapp.frontend.pages.about.about :as page-about]
    [photoni.webapp.frontend.pages.user.user-view-list :refer [user-view-list]]
    [photoni.webapp.frontend.pages.user.user-view-upsert :refer [user-view-upsert]]))


(defmethod routes/panels :view/home []
  [page-home/home-panel])

(defmethod routes/panels :view/about []
  [page-about/about-panel])

(defmethod routes/panels :view/user []
  [user-view-list])

(defmethod routes/panels :view/user-upsert []
  [user-view-upsert])

(re-frame/reg-sub
  ::active-panel
  (fn [db _]
    (:active-panel db)))

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::active-panel])]
    (routes/panels @active-panel)))
