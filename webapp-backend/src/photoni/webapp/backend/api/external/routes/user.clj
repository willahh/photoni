(ns photoni.webapp.backend.api.external.routes.user
  (:require [photoni.webapp.backend.api.api-utils :as api-utils]
            [photoni.webapp.backend.user.user-service :refer [user-service-repo]]
            [photoni.webapp.domain.search.search :as search]
            [photoni.webapp.domain.user.user :as user]
            [photoni.webapp.domain.user.user-service :as user-service]
            [photoni.webapp.domain.role.role-entity :as role])
  (:import (java.util UUID)))

(def tag-user "user")


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ GET find users by                                                         │
;; └───────────────────────────────────────────────────────────────────────────┘
(def api-find-users-by
  #^{:route/method :get
     :route/path   "/api/users"}
  {:summary     "Find users by query"
   :description ""
   :tags        #{tag-user}
   ;; :parameters  {:body user/find-users-by-fields-spec} ;; TODO: Need to coerce parameters
   :responses   {200 {:body user/find-users-by-response-spec}}
   :handler     (fn [{:keys [body-params]}]
                  (prn "body-params2" body-params)

                  (do "TODO TEMP DEBUG"
                      (def body-params body-params)
                      (def clauses (:clauses body-params))
                      (def fields (:fields body-params))
                      (def search-clause (search/search-clauses-json->search-clauses clauses)))

                  (let [fields (:fields body-params)
                        clauses (:clauses body-params)
                        fields (mapv keyword fields)
                        search-clause (search/search-clauses-json->search-clauses clauses)
                        results (user-service/find-users-by
                                  user-service-repo
                                  (user/find-users-by-query (cond-> {:fields fields}
                                                                    search-clause (assoc :clauses search-clause))))]
                    {:status 200 :body results}))})

;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ GET users                                                                 │
;; └───────────────────────────────────────────────────────────────────────────┘
;(def spec-users [:vector user/spec-user])
;(def api-users-get-users
;  #^{:route/method :get
;     :route/path   "/api/users"}
;  {:summary     "List of users"
;   :description ""
;   :tags        #{tag-user}
;   :responses   {200 {:body spec-users}}
;   :handler     (fn [request]
;                  (let [users-entities (user-service/get-users (user/get-users-query))]
;                    {:status 200
;                     :body   users-entities}))})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ GET user by user id                                                       │
;; └───────────────────────────────────────────────────────────────────────────┘
(def api-users-get-user-by-id
  #^{:route/method :get
     :route/path   "/api/users/{user/id}"}
  {:summary     "Get user by id"
   :description ""
   :tags        #{tag-user}
   :parameters  {:path [:map user/spec-id]}
   :responses   {200 {:body user/spec-user}}
   :handler     (fn [{{:user/keys [id]} :path-params}]
                  (let [user-entity (user-service/get-user-by-user-id
                                      user-service-repo
                                      (user/get-user-by-id-query (UUID/fromString id)))]
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
   :tags        #{tag-user}
   :parameters  {:path [:map user/spec-id]}
   ;:responses   {200 [:map {}]
   ;              404 [:map {}]}
   :handler     (fn [{{{:user/keys [id]} :path} :parameters}]
                  (let [deleted? (user-service/delete-user-by-user-id
                                   user-service-repo (user/delete-user-by-user-id-command id))]
                    (if deleted?
                      {:status 200 :body {}}
                      {:status 404 :body {}})))})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ POST create user                                                          │
;; └───────────────────────────────────────────────────────────────────────────┘
(def api-users-create-user
  #^{:route/method :post
     :route/path   "/api/users"}
  {:summary     "Upsert an user"
   :description ""
   :tags        #{tag-user}
   :parameters  {:body [:map
                        (api-utils/set-spec-field-optional user/spec-id)
                        user/spec-name
                        user/spec-title
                        user/spec-email
                        role/spec-role
                        user/spec-age]}
   :responses   {200 {:body user/spec-user}}
   :handler     (fn [{{{:user/keys [id role email age name title]} :body :as body} :parameters}]
                  (prn "CREATE USER x :" id role email age)

                  (do
                    (def id id)
                    (def role role)
                    (def email email)
                    (def age age)
                    #_(def name name)
                    (def title title)
                    )
                  (let [insert? (nil? id)
                        id (or id (java.util.UUID/randomUUID))
                        user-entity (user-service/create-user
                                      user-service-repo
                                      (user/create-user-command #:user{:id    id
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
(def routes (api-utils/ns->routes 'photoni.webapp.backend.api.external.routes.user))

