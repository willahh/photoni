(ns photoni.webapp.frontend.views
  (:require
    [re-frame.core :as re-frame]
    [re-frame.core :refer [subscribe dispatch]]
    [photoni.webapp.frontend.routes :as routes]
    [photoni.webapp.frontend.pages.not-found.not-found :as page-not-found]
    [photoni.webapp.frontend.pages.home.home :as page-home]
    [photoni.webapp.frontend.pages.home.home :as page-home]
    [photoni.webapp.frontend.pages.about.about :as page-about]
    [photoni.webapp.frontend.pages.api.api :as page-api]
    [photoni.webapp.frontend.pages.user.user-view-list :refer [user-view-list]]
    [photoni.webapp.frontend.pages.user.user-view-upsert :refer [user-view-upsert]]
    [photoni.webapp.frontend.pages.group.group-view-list :refer [group-view-list]]
    [photoni.webapp.frontend.pages.group.group-view-upsert :refer [group-view-upsert]]))

(defmethod routes/panels :default [_ route-params]
  [page-not-found/not-found-panel])

(defmethod routes/panels :view/home [_ route-params]
  [page-home/home-panel])

(defmethod routes/panels :view/api [_ route-params]
  [page-api/page-api])

(defmethod routes/panels :view/about [_ route-params]
  [page-about/about-panel])


;; user
(defmethod routes/panels :view/user [_ route-params]
  [user-view-list])

(defmethod routes/panels :view/user-edit [_ route-params]
  [user-view-upsert route-params {:form-mode :form.mode/edit}])

(defmethod routes/panels :view/user-copy [_ route-params]
  [user-view-upsert route-params {:form-mode :form.mode/copy}])

(defmethod routes/panels :view/user-insert [_ route-params]
  [user-view-upsert route-params {:form-mode :form.mode/insert}])

;; group
(defmethod routes/panels :view/group [_ route-params]
  [group-view-list])

(defmethod routes/panels :view/group-edit [_ route-params]
  [group-view-upsert route-params {:form-mode :form.mode/edit}])

(defmethod routes/panels :view/group-copy [_ route-params]
  [group-view-upsert route-params {:form-mode :form.mode/copy}])

(defmethod routes/panels :view/group-insert [_ route-params]
  [group-view-upsert route-params {:form-mode :form.mode/insert}])

;;
(re-frame/reg-sub ::active-panel (fn [db _] (:active-panel db)))
(re-frame/reg-sub ::route-params (fn [db _] (:route-params db)))

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::active-panel])
        route-params (re-frame/subscribe [::route-params])]
    (routes/panels @active-panel @route-params)))
