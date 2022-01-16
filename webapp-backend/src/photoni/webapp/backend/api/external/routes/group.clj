(ns photoni.webapp.backend.api.external.routes.group
  (:require [photoni.webapp.backend.api.api-utils :as api-utils]
            [photoni.webapp.backend.group.group-service :refer [group-service-repo]]
            [photoni.webapp.domain.search.search :as search]
            [photoni.webapp.domain.group.group :as group]
            [photoni.webapp.domain.group.group-service :as group-service]
            [photoni.webapp.domain.role.role-entity :as role])
  (:import (java.util UUID)))

(def tag-group "group")


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ GET find groups by                                                        │
;; └───────────────────────────────────────────────────────────────────────────┘
(def api-find-groups-by
  #^{:route/method :get
     :route/path   "/api/groups"}
  {:summary     "Find groups by query"
   :description ""
   :tags        #{tag-group}
   ;; :parameters  {:body group/find-groups-by-fields-spec} ;; TODO: Need to coerce parameters
   :responses   {200 {:body group/find-groups-by-response-spec}}
   :handler     (fn [{:keys [body-params]}]
                  (let [fields (:fields body-params)
                        clauses (:clauses body-params)
                        fields (mapv keyword fields)
                        search-clause (search/search-clauses-json->search-clauses clauses)
                        results (group-service/find-groups-by
                                  group-service-repo
                                  (group/find-groups-by-query (cond-> {:fields fields}
                                                                      search-clause (assoc :clauses search-clause))))]
                    {:status 200 :body results}))})

;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ GET groups                                                                │
;; └───────────────────────────────────────────────────────────────────────────┘
;(def spec-groups [:vector group/spec-group])
;(def api-groups-get-groups
;  #^{:route/method :get
;     :route/path   "/api/groups"}
;  {:summary     "List of groups"
;   :description ""
;   :tags        #{tag-group}
;   :responses   {200 {:body spec-groups}}
;   :handler     (fn [request]
;                  (let [groups-entities (group-service/get-groups (group/get-groups-query))]
;                    {:status 200
;                     :body   groups-entities}))})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ GET group by group id                                                     │
;; └───────────────────────────────────────────────────────────────────────────┘
(def api-groups-get-group-by-id
  #^{:route/method :get
     :route/path   "/api/groups/{group/id}"}
  {:summary     "Get group by id"
   :description ""
   :tags        #{tag-group}
   :parameters  {:path [:map group/spec-id]}
   :responses   {200 {:body group/spec-group}}
   :handler     (fn [{{:group/keys [id]} :path-params}]
                  (let [group-entity (group-service/get-group-by-group-id
                                       group-service-repo
                                       (group/get-group-by-id-query (UUID/fromString id)))]
                    (if group-entity
                      {:status 200 :body group-entity}
                      {:status 404})))})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ DELETE group by group id                                                  │
;; └───────────────────────────────────────────────────────────────────────────┘
(def api-groups-delete-group-by-id
  #^{:route/method :delete
     :route/path   "/api/groups/{group/id}"}
  {:summary     "Delete group by group id"
   :description ""
   :tags        #{tag-group}
   :parameters  {:path [:map group/spec-id]}
   :handler     (fn [{{{:group/keys [id]} :path} :parameters}]
                  (let [deleted? (group-service/delete-group-by-group-id
                                   group-service-repo (group/delete-group-by-group-id-command id))]
                    (if deleted?
                      {:status 200 :body {}}
                      {:status 404 :body {}})))})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ POST create group                                                         │
;; └───────────────────────────────────────────────────────────────────────────┘
(def api-groups-create-group
  #^{:route/method :post
     :route/path   "/api/groups"}
  {:summary     "Upsert an group"
   :description ""
   :tags        #{tag-group}
   :parameters  {:body [:map
                        (api-utils/set-spec-field-optional group/spec-id)
                        group/spec-name]}
   :responses   {200 {:body group/spec-group}}
   :handler     (fn [{{{:group/keys [id name]} :body :as body} :parameters}]
                  (prn "api-groups-create-group handler")
                  (prn "name" name)
                  (def id id)
                  (def name name)
                  (let [insert? (nil? id)
                        id (or id (java.util.UUID/randomUUID))
                        group-entity (group-service/create-group
                                       group-service-repo
                                       (group/create-group-command #:group{:id   id
                                                                           :name name}))]
                    {:status (if insert? 201 200)
                     :body   group-entity}))})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ Routes                                                                    │
;; └───────────────────────────────────────────────────────────────────────────┘
(def routes (api-utils/ns->routes 'photoni.webapp.backend.api.external.routes.group))

