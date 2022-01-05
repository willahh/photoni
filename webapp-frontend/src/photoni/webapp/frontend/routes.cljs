(ns photoni.webapp.frontend.routes
  (:require
    [bidi.bidi :as bidi]
    [pushy.core :as pushy]
    [re-frame.core :as re-frame]
    [photoni.webapp.frontend.events :as events]))

(defmulti panels identity)
(defmethod panels :default [] [:div "No panel found for this route."])

(def routes
  (atom
    ["/" {""            :view/home
          "about"       :view/about
          "user"        :view/user
          "user/upsert" :view/user-upsert}]))

(defn parse
  [url]
  (bidi/match-route @routes url))

(defn url-for
  [& args]
  (apply bidi/path-for (into [@routes] args)))

(defn dispatch
  [route]
  (prn "dispatch route:" route)
  (let [view-name (:handler route)]
    (re-frame/dispatch [::events/set-active-panel view-name])))

(defonce history
         (pushy/pushy dispatch parse))

(defn navigate!
  [handler]
  (prn "navigate! handler:" handler)
  (pushy/set-token! history (url-for handler)))

(defn start!
  []
  (pushy/start! history))

(re-frame/reg-fx
  :navigate
  (fn [handler]
    (navigate! handler)))
