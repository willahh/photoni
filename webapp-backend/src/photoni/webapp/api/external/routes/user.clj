(ns photoni.webapp.api.external.routes.user
  (:require [clojure.spec.alpha :as s]
            [spec-tools.core :as st]
            [photoni.webapp.domain.common.utils :as common-utils]
            [photoni.webapp.utils :as utils]
            [photoni.webapp.api.tags :as tags]
            [photoni.webapp.infra.inmem.eventbus-inmem-repo :refer [event-bus-inmem-repository]]
            [photoni.webapp.infra.postgres.user.user-postgres-repo :refer [user-postgres-repository]]
            [photoni.webapp.domain.user.user-entity :as user-entity]
            [photoni.webapp.domain.user.user-service :as user-service]
            [photoni.webapp.domain.user.user-query :as user-query]
            [photoni.webapp.domain.user.user-command :as user-command]
            [clojure.spec.gen.alpha :as gen])
  (:import (java.util UUID)))



;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ GET users                                                                 │
;; └───────────────────────────────────────────────────────────────────────────┘
(def spec-users [:vector user-entity/spec-user])
(def api-users-get-users
  {:summary     "List of users"
   :description ""
   :tags        #{tags/users}
   ;;:responses   {200 {:body spec-users}}
   :handler     (fn [request]
                  (let [users-entities (user-service/get-users (user-query/get-users))]
                    {:status 200
                     :body   users-entities}))})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ GET user by user id                                                       │
;; └───────────────────────────────────────────────────────────────────────────┘
(def api-users-get-user-by-id
  {:summary     "Get user by id"
   :description ""
   :tags        #{tags/users}
   :parameters  {:path [:map user-entity/spec-id]}
   ;;:responses   {200 {:body user-entity/spec-user}}
   :handler     (fn [{{:user/keys [id]} :path-params}]
                  (let [user-entity (user-service/get-user-by-id (user-query/get-user-by-id-query (UUID/fromString id)))]
                    (if user-entity
                      {:status 200
                       :body   user-entity}
                      {:status 404})))})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ DELETE user by user id                                                    │
;; └───────────────────────────────────────────────────────────────────────────┘
(def api-users-delete-user-by-id
  {:summary     "Delete user by user id"
   :description ""
   :tags        #{tags/users}
   :parameters  {:path [:map user-entity/spec-id]}
   ;;:responses   {200 {:body {}}}
   :handler     (fn [{{{:user/keys [id]} :path} :parameters}]
                  (prn "id" id)
                  (user-service/delete-user (user-command/delete-user-by-user-id-command id))
                  {:status 200
                   :body   {}})})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ POST create user                                                          │
;; └───────────────────────────────────────────────────────────────────────────┘
(def api-users-create-user
  {:summary     "Upsert an user"
   :description ""
   :tags        #{tags/users}
   :parameters  {:body [:map
                        (common-utils/set-spec-field-optional user-entity/spec-id)
                        user-entity/spec-name
                        user-entity/spec-title
                        user-entity/spec-email
                        user-entity/spec-role
                        user-entity/spec-age]}
   ;;:responses   {200 {:body user-entity/spec-user}}
   :handler     (fn [{{{:user/keys [id role email age name title]} :body :as body} :parameters}]
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
(def routes
  [["/api/users" {:get  api-users-get-users
                  :post api-users-create-user}]
   ["/api/users/{user/id}" {:get    api-users-get-user-by-id
                            :delete api-users-delete-user-by-id}]])
