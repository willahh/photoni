(ns frontend.views
  (:require
    [re-frame.core :as re-frame]
    [frontend.routes :as routes]
    [frontend.utils.tailwind-styles :as styles]

    [re-frame.core :refer [subscribe dispatch]]
    [frontend.components.components :as components]
    [frontend.pages.home.home :as page-home]
    [frontend.pages.about.about :as page-about]))


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
