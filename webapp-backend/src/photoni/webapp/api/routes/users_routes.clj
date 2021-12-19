(ns photoni.webapp.api.routes.users-routes
  (:require [clojure.spec.alpha :as s]
            [spec-tools.core :as st]
            [photoni.webapp.utils :as utils]
            [photoni.webapp.api.tags :as tags]
            [photoni.webapp.infra.inmem.eventbus-inmem-repo :refer [event-bus-inmem]]
            [photoni.webapp.infra.postgres.user.user-postgres-repo :refer [user-postgres-repository]]
            [photoni.webapp.domain.user.user-entity :as user-entity]
            [photoni.webapp.domain.user.user-service :as user-service]
            [photoni.webapp.domain.user.user-query :as user-query]
            [photoni.webapp.domain.user.user-command :as user-command]
            [clojure.spec.gen.alpha :as gen]))

(s/def ::user-id (st/spec {:spec                uuid?
                           :name                "User id"
                           :description         "UUID"
                           :json-schema/default #uuid"fe25b88a-088f-4f46-a8bb-1c9f0131b6c2"}))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ GET users                                                                 │
;; └───────────────────────────────────────────────────────────────────────────┘
(s/def ::users-response (s/coll-of :user/user))
(comment
  (require '[clojure.spec.gen.alpha :as gen])
  (gen/generate (s/gen ::users-response))
  )
(def api-users-get-users
  {:summary     "List of users"
   :description "A description of users endpoint"
   :tags        #{tags/users}
   ;;:responses   {200 {:body ::users-response}}
   :handler     (fn [{{{:keys [x y]} :query} :parameters}]
                  (let [users-entities (user-service/get-users (user-query/get-users) user-postgres-repository event-bus-inmem)]
                    {:status 200
                     :body   (utils/qualified-map->underscore-map users-entities)}))})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ GET user by user id                                                       │
;; └───────────────────────────────────────────────────────────────────────────┘
(s/def ::get-user-by-id-request (s/keys :req-un [::user-id]))
(def api-users-get-user-by-id
  {:summary     "Get user by id"
   :description ""
   :tags        #{tags/users tags/GET}
   :parameters  {:path {:user-id string?}}
   :responses   {200 {:body :user/user}}
   :handler     (fn [x]
                  {:status 200
                   :body   [:x "a"]}
                  #_(let [query (user-query/get-user-by-id-query user-id)
                          user-entity (user-service/get-user-by-id query user-postgres-repository event-bus-inmem)]
                      {:status 200
                       :body   user-entity}))})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ DELETE user by user id                                                    │
;; └───────────────────────────────────────────────────────────────────────────┘
(s/def ::delete-user-by-id-request (s/keys :req-un [::user-id]))
(def api-users-delete-user-by-id
  {:summary     "Delete user by user id"
   :description ""
   :tags        #{tags/users tags/DELETE}
   :parameters  {:query ::delete-user-by-id-request}
   :responses   {200 {:body {}}}
   :handler     (fn [{{{:keys [user-id]} :query} :parameters}]
                  (let [command (user-command/delete-user-by-user-id-command user-id)
                        _ (user-service/delete-user command user-postgres-repository event-bus-inmem)]
                    {:status 200
                     :body   {}}))})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ PUT create user                                                           │
;; └───────────────────────────────────────────────────────────────────────────┘
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
   :tags        #{tags/users tags/PUT}
   :parameters  {:query ::create-user-request}
   :responses   {200 {:body ::create-user-response}}
   :handler     (fn [{{{:keys [x y]} :query} :parameters}]
                  (prn "add users" x)
                  {:status 200
                   :body   {:total (+ x y)}})})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ Routes aggregate                                                          │
;; └───────────────────────────────────────────────────────────────────────────┘
#_(def routes-aggregate
    ["/api/users"
     [["/"
       {:get api-users-get-users}]
      ["/test/:user-id"
       {:get    api-users-get-user-by-id
        :post   api-users-create-user
        :delete api-users-delete-user-by-id}]]])

(def routes-aggregate
  [["/api/users" {:get api-users-get-users}]
   ["/api/users/:u/test" {:get    api-users-get-user-by-id
                          :post   api-users-create-user
                          :delete api-users-delete-user-by-id}]])
