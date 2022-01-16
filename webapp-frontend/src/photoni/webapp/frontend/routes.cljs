(ns photoni.webapp.frontend.routes
  (:require
    [bidi.bidi :as bidi]
    [pushy.core :as pushy]
    [re-frame.core :as re-frame]
    [photoni.webapp.frontend.events :as events]))

(defmulti panels identity)

(def routes
  (atom
    ["/" {""            :view/home
          "about"       :view/about
          "user"        :view/user
          "user/"       {[:user-id "/edit"] :view/user-edit
                         [:user-id "/copy"] :view/user-copy}
          "user/insert" :view/user-insert}]))

(defn parse
  [url]
  (bidi/match-route @routes url))

(defn url-for
  [& args]
  (apply bidi/path-for (into [@routes] args)))

(defn dispatch
  [route]
  (let [view-name (:handler route)
        params (:route-params route)]
    (re-frame/dispatch [::events/set-active-panel view-name params])))

(defonce history
         (pushy/pushy dispatch parse))

(defn navigate!
  [handler route-params]
  (let [route-url (apply url-for handler (apply concat route-params))]
    (pushy/set-token! history route-url)))

(defn start!
  []
  (pushy/start! history))

(re-frame/reg-fx
  :navigate
  (fn [[handler route-params]]
    (navigate! handler route-params)))
