(ns photoni.webapp.frontend.views
  (:require
    [re-frame.core :as re-frame]
    [re-frame.core :refer [subscribe dispatch]]
    [photoni.webapp.frontend.routes :as routes]
    [photoni.webapp.frontend.utils.tailwind-styles :as styles]
    [photoni.webapp.frontend.components.components :as components]
    [photoni.webapp.frontend.pages.home.home :as page-home]
    [photoni.webapp.frontend.pages.about.about :as page-about]))


(defmethod routes/panels :home-panel []
  [page-home/home-panel])

(defmethod routes/panels :about-panel []
  [page-about/about-panel])

;; main


(re-frame/reg-sub
  ::active-panel
  (fn [db _]
    (:active-panel db)))

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::active-panel])]
    (routes/panels @active-panel)))
