(ns photoni.webapp.api.routes.users-routes
  (:require [clojure.spec.alpha :as s]
            [spec-tools.core :as st]
            [photoni.webapp.utils :as utils]
            [photoni.webapp.api.tags :as tags]
            [photoni.webapp.infra.inmem.eventbus-inmem-repo :refer [event-bus-inmem]]
            [photoni.webapp.infra.postgres.user.user-postgres-repo :refer [user-postgres-repository]]
            [photoni.webapp.domain.user.user-entity :as user-entity]
            [photoni.webapp.domain.user.user-service :as user-service]
            [photoni.webapp.domain.user.user-query :as user-query]))


(s/def ::total int?)
(s/def ::users-response (s/keys :req-un [::total]))
(def api-users-get-users
  {:summary     "List of users"
   :description "A description of users endpoint"
   :tags        #{tags/users}
   :responses   {200 {:body ::users-response}}
   :handler     (fn [{{{:keys [x y]} :query} :parameters}]
                  (prn "usrs list")
                  {:status 200
                   :body   {:total (+ x y)}})})





(s/def ::user-id (st/spec {:spec                uuid?
                           :name                "User id"
                           :description         "UUID"
                           :json-schema/default 42}))
(s/def ::get-user-by-id-request (s/keys :req-un [::user-id]))
(def api-users-get-user-by-id
  {:summary     "Get user by id"
   :description ""
   :tags        #{tags/users tags/GET}
   :parameters  {:query ::get-user-by-id-request}
   :responses   {200 {:body :user/user}}
   :handler     (fn [{{{:keys [user-id]} :query} :parameters}]
                  (prn "user-id" user-id)
                  (let [user-entity (user-service/get-user-by-id (user-query/get-user-by-id-query user-id) user-postgres-repository event-bus-inmem)]
                    (prn "user-entity:" user-entity)
                    {:status 200
                     :body   user-entity}))})





(s/def ::x (st/spec {:spec                int?
                     :name                "X parameter"
                     :description         "Description for X parameter"
                     :json-schema/default 42}))
(s/def ::y int?)
(s/def ::total int?)
(s/def ::create-user-request (s/keys :req-un [::x ::y]))
(s/def ::create-user-response (s/keys :req-un [::total]))
(def api-users-create-user
  {:summary     "Create an user"
   :description "Create an user"
   :tags        #{tags/users}
   :parameters  {:query ::create-user-request}
   :responses   {200 {:body ::create-user-response}}
   :handler     (fn [{{{:keys [x y]} :query} :parameters}]
                  (prn "add users" x)
                  {:status 200
                   :body   {:total (+ x y)}})})





(def routes-aggregate
  ["/api/users"
   [["/"
     {:get api-users-get-users}]
    ["/:user-id"
     {:get  api-users-get-user-by-id
      :post api-users-create-user}]]])
