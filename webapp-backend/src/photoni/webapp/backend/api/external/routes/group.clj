(ns photoni.webapp.backend.api.external.routes.group
  (:require [photoni.webapp.backend.api.api-utils :as api-utils]
            [photoni.webapp.domain.group.group :as group]
            [photoni.webapp.domain.group.group-service :as group-service])
  (:import (java.util UUID)))

(def tag-group "group")

;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [GET] groups                                                              │
;; └───────────────────────────────────────────────────────────────────────────┘
(def spec-groups [:vector group/group-spec])
(def api-groups-get-groups
  #^{:route/method :get
     :route/path   "/api/groups"}
  {:summary     "List of groups"
   :description ""
   :tags        #{tag-group}
   :responses   {200 {:body spec-groups}}
   :handler     (fn [request]
                  (let [groups-entities (group-service/get-groups (group/get-groups-query))]
                    {:status 200
                     :body   groups-entities}))})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [GET] group by group id                                                   │
;; └───────────────────────────────────────────────────────────────────────────┘
(def api-groups-get-group-by-id
  #^{:route/method :get
     :route/path   "/api/groups/{group/id}"}
  {:summary     "Get group by id"
   :description ""
   :tags        #{tag-group}
   :parameters  {:path [:map group/group-id-spec]}
   :responses   {200 {:body group/group-spec}}
   :handler     (fn [{{:group/keys [id]} :path-params}]
                  (let [group-entity (group-service/get-group-by-group-id (group/get-group-by-id-query (UUID/fromString id)))]
                    (if group-entity
                      {:status 200 :body group-entity}
                      {:status 404})))})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [DELETE] group by group id                                                │
;; └───────────────────────────────────────────────────────────────────────────┘
(def api-groups-delete-group-by-id
  #^{:route/method :delete
     :route/path   "/api/groups/{group/id}"}
  {:summary     "Delete group by group id"
   :description ""
   :tags        #{tag-group}
   :parameters  {:path [:map group/group-id-spec]}
   ;:responses   {200 [:map {}]
   ;              404 [:map {}]}
   :handler     (fn [{{{:group/keys [id]} :path} :parameters}]
                  (let [deleted? (group-service/delete-group-by-group-id (group/delete-group-by-group-id-command id))]
                    (if deleted?
                      {:status 200 :body {}}
                      {:status 404 :body {}})))})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [POST] create group                                                       │
;; └───────────────────────────────────────────────────────────────────────────┘
(def api-groups-create-group
  #^{:route/method :post
     :route/path   "/api/groups"}
  {:summary     "Upsert an group"
   :description ""
   :tags        #{tag-group}
   :parameters  {:body [:map
                        (api-utils/set-spec-field-optional group/group-id-spec)
                        group/group-name-spec]}
   :responses   {200 {:body group/group-spec}}
   :handler     (fn [{{{:group/keys [id role email age name title]} :body :as body} :parameters}]
                  (let [insert? (nil? id)
                        id (or id (java.util.UUID/randomUUID))
                        group-entity (group-service/create-group
                                       (group/create-group-command {:id    id
                                                                    :name  name
                                                                    :title title
                                                                    :email email
                                                                    :role  role
                                                                    :age   age}))]
                    {:status (if insert? 201 200)
                     :body   group-entity}))})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ Routes                                                                    │
;; └───────────────────────────────────────────────────────────────────────────┘
(def routes (api-utils/ns->routes 'photoni.webapp.backend.api.external.routes.group))

