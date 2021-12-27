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
            [clojure.spec.gen.alpha :as gen]
            [photoni.webapp.domain.common.state :refer [user-repository event-bus]])
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
                  (let [users-entities (user-service/get-users (user-query/get-users) user-repository event-bus)]
                    {:status 200
                     :body   users-entities}))})

;
;;; ┌───────────────────────────────────────────────────────────────────────────┐
;;; │ GET user by user id                                                       │
;;; └───────────────────────────────────────────────────────────────────────────┘
;(def api-users-get-user-by-id
;  {:summary     "Get user by id"
;   :description ""
;   :tags        #{tags/users tags/GET}
;   :parameters  {:path [:map [:user-id uuid?]]}
;   :responses   {200 {:body user-entity/spec-user}}
;   :handler     (fn [{{:keys [user-id]} :path-params}]
;                  (let [user-id (UUID/fromString user-id)
;                        query (user-query/get-user-by-id-query user-id)
;                        user-entity (user-service/get-user-by-id query user-postgres-repository event-bus-inmem)]
;                    {:status 200
;                     :body   user-entity}))})
;
;
;;; ┌───────────────────────────────────────────────────────────────────────────┐
;;; │ DELETE user by user id                                                    │
;;; └───────────────────────────────────────────────────────────────────────────┘
;(def api-users-delete-user-by-id
;  {:summary     "Delete user by user id"
;   :description ""
;   :tags        #{tags/users tags/DELETE}
;   :parameters  {:path [:map [:user-id uuid?]]}
;   ;;:responses   {200 {:body {}}}
;   :handler     (fn [{{{:keys [user-id]} :query} :parameters}]
;                  (let [command (user-command/delete-user-by-user-id-command user-id)
;                        _ (user-service/delete-user command user-postgres-repository event-bus-inmem)]
;                    {:status 200
;                     :body   {}}))})
;
;
;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ POST create user                                                          │
;; └───────────────────────────────────────────────────────────────────────────┘
(def spec-users user-entity/spec-user)

#_{:summary    "plus with malli body parameters"
   :parameters {:body [:map
                       [:x
                        {:title               "X parameter"
                         :description         "Description for X parameter"
                         :json-schema/default 42}
                        int?]
                       [:y int?]]}
   :responses  {200 {:body [:map [:total int?]]}}
   :handler    (fn [{{{:keys [x y]} :body} :parameters}]
                 {:status 200
                  :body   {:total (+ x y)}})}

(def api-users-create-user
  {:summary     "Create an user"
   :description "Create an user"
   :tags        #{tags/users tags/PUT}
   :parameters  {:body user-entity/spec-user}
   :responses   {200 {:body user-entity/spec-user}}
   :handler     (fn [{{{:keys [role email age name title id]} :body} :parameters}]
                  (let [user-entity (user-service/create-user
                                         (user-command/create-user-command {:id    id
                                                                            :name  name
                                                                            :title title
                                                                            :email email
                                                                            :role  role
                                                                            :age   age})
                                         user-repository event-bus)]
                    {:status 200
                     :body   user-entity}))})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ Routes aggregate                                                          │
;; └───────────────────────────────────────────────────────────────────────────┘

(def routes-aggregate
  [["/api/users" {:get api-users-get-users
                  :post api-users-create-user
                  }]
   ;["/api/users/{user-id}" {:get    api-users-get-user-by-id
   ;
   ;                         :delete api-users-delete-user-by-id
   ;                         }]

   ])
