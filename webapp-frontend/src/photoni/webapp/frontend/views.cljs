(ns photoni.webapp.frontend.views
  (:require
    [re-frame.core :as re-frame]
    [re-frame.core :refer [subscribe dispatch]]
    [photoni.webapp.frontend.routes :as routes]
    [photoni.webapp.frontend.pages.home.home :as page-home]
    [photoni.webapp.frontend.pages.about.about :as page-about]
    [photoni.webapp.frontend.pages.user.users-page :refer [users-page]]))


(defmethod routes/panels :view/home []
  [page-home/home-panel])

(defmethod routes/panels :view/about []
  [page-about/about-panel])

(defmethod routes/panels :view/user []
  [users-page])

(re-frame/reg-sub
  ::active-panel
  (fn [db _]
    (prn "[3] reg-sub ::active-panel" (:active-panel db))
    (:active-panel db)))

(defn main-panel []
  (prn "main-panel")
  (let [active-panel (re-frame/subscribe [::active-panel])]
    (prn "active-panel" @active-panel)
    (routes/panels @active-panel)))
