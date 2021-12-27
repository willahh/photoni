(ns photoni.webapp.api.external.routes.user
  (:require [photoni.webapp.api.tags :as tags]
            [photoni.webapp.api.api-utils :as api-utils]
            [photoni.webapp.domain.user.user-entity :as user-entity]
            [photoni.webapp.domain.user.user-service :as user-service]
            [photoni.webapp.domain.user.user-query :as user-query]
            [photoni.webapp.domain.user.user-command :as user-command])
  (:import (java.util UUID)))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ GET users                                                                 │
;; └───────────────────────────────────────────────────────────────────────────┘
(def spec-users [:vector user-entity/spec-user])
(def api-users-get-users
  #^{:route/method :get
     :route/path   "/api/users"}
  {:summary     "List of users"
   :description ""
   :tags        #{tags/users}
   :responses   {200 {:body spec-users}}
   :handler     (fn [request]
                  (let [users-entities (user-service/get-users (user-query/get-users-query))]
                    {:status 200
                     :body   users-entities}))})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ GET user by user id                                                       │
;; └───────────────────────────────────────────────────────────────────────────┘
(def api-users-get-user-by-id
  #^{:route/method :get
     :route/path   "/api/users/{user/id}"}
  {:summary     "Get user by id"
   :description ""
   :tags        #{tags/users}
   :parameters  {:path [:map user-entity/spec-id]}
   :responses   {200 {:body user-entity/spec-user}}
   :handler     (fn [{{:user/keys [id]} :path-params}]
                  (let [user-entity (user-service/get-user-by-id (user-query/get-user-by-id-query (UUID/fromString id)))]
                    (if user-entity
                      {:status 200 :body user-entity}
                      {:status 404})))})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ DELETE user by user id                                                    │
;; └───────────────────────────────────────────────────────────────────────────┘
(def api-users-delete-user-by-id
  #^{:route/method :delete
     :route/path   "/api/users/{user/id}"}
  {:summary     "Delete user by user id"
   :description ""
   :tags        #{tags/users}
   :parameters  {:path [:map user-entity/spec-id]}
   ;;:responses   {200 {:body {}}}
   :handler     (fn [{{{:user/keys [id]} :path} :parameters}]
                  (let [deleted? (user-service/delete-user (user-command/delete-user-by-user-id-command id))]
                    (if deleted?
                      {:status 200}
                      {:status 404})))})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ POST create user                                                          │
;; └───────────────────────────────────────────────────────────────────────────┘
(def api-users-create-user
  #^{:route/method :post
     :route/path   "/api/users"}
  {:summary     "Upsert an user"
   :description ""
   :tags        #{tags/users}
   :parameters  {:body [:map
                        (api-utils/set-spec-field-optional user-entity/spec-id)
                        user-entity/spec-name
                        user-entity/spec-title
                        user-entity/spec-email
                        user-entity/spec-role
                        user-entity/spec-age]}
   ;;:responses   {200 {:body user-entity/spec-user}}
   :handler     (fn [{{{:user/keys [id role email age name title]} :body :as body} :parameters}]
                  (prn "handler role:" role)
                  (let [insert? (nil? id)
                        id (or id (java.util.UUID/randomUUID))
                        user-entity (user-service/create-user
                                      (user-command/create-user-command {:id    id
                                                                         :name  name
                                                                         :title title
                                                                         :email email
                                                                         :role  role
                                                                         :age   age}))]
                    {:status (if insert? 201 200)
                     :body   user-entity}))})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ Routes                                                                    │
;; └───────────────────────────────────────────────────────────────────────────┘
(def routes (api-utils/ns->routes 'photoni.webapp.api.external.routes.user))

