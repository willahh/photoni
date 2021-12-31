(ns photoni.webapp.domain.group.group
  (:require [malli.core :as m]
            [photoni.webapp.domain.common.command :as command]
            [photoni.webapp.domain.common.event :as event]
            [photoni.webapp.domain.common.query :as query]))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [constructor]                                                             │
;; └───────────────────────────────────────────────────────────────────────────┘
(defn ->group
  [{:keys [id name title email role age] :as fields}]
  #:group{:id    id
          :name  name
          :title title
          :email email
          :role  role
          :age   age})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [spec]                                                                    │
;; └───────────────────────────────────────────────────────────────────────────┘
(def non-empty-string
  (m/from-ast {:type       :string
               :properties {:min 1}}))

(def group-id-spec
  [:group/id {:title               "id parameter"
              :description         "Description for id parameter"
              :json-schema/default "4c0e630d-19f9-47a8-9e89-225dc7c8e338"}
   uuid?])
(def group-name-spec
  [:group/name {:title               "Name parameter"
                :description         "Description for name parameter"
                :json-schema/default "User"}
   [:and string? non-empty-string]])

(def group-spec
  [:map
   group-id-spec
   group-name-spec])


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [repository]                                                              │
;; └───────────────────────────────────────────────────────────────────────────┘
(defprotocol GroupRepositoryProtocol
  (get-groups [group-repo])
  (create-group [group-repo group-fields])
  (get-group-by-group-id [group-repo group-id])
  (delete-group-by-group-id [group-repo group-id]))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [query] get-groups                                                        │
;; └───────────────────────────────────────────────────────────────────────────┘
(def get-groups-spec
  [:map
   [:type [:enum ::get-groups]]])

(defn get-groups-query
  []
  (query/->query ::get-groups get-groups-spec))

(defn get-groups-event [get-groups-query groups]
  (event/->event ::groups-retrieved-event {:from-query get-groups-query
                                           :entities   groups}))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [query] get-group-by-id                                                   │
;; └───────────────────────────────────────────────────────────────────────────┘
(def get-group-by-id-spec
  [:map
   [:type [:enum ::get-group-by-id-query]]
   [:fields
    [:map group-id-spec]]])

(defn get-group-by-id-query
  [group-id]
  (query/->query ::get-group-by-id-query
                 get-group-by-id-spec
                 #:group{:id group-id}))

(defn get-group-by-id-event [get-group-query entity]
  (event/->event ::group-retrieved-event {:from-query get-group-query
                                          :entity     entity}))

;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [command] create-group                                                    │
;; └───────────────────────────────────────────────────────────────────────────┘
(def create-group-spec
  [:map
   [:type [:enum ::create-group-command]]
   [:fields
    group-spec]])

(defn create-group-command
  [{:keys [id name title email role age] :as fields}]
  (command/->command ::create-group-command
                     create-group-spec
                     #:group{:id    id
                             :name  name
                             :title title
                             :email email
                             :role  role
                             :age   age}))

(defn create-group-event [add-group-command entity]
  (event/->event ::group-added-event {:from-command add-group-command
                                      :entity       entity}))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [command] delete-group-by-group-id                                        │
;; └───────────────────────────────────────────────────────────────────────────┘
(def delete-group-by-group-id-spec
  [:map
   [:type [:enum ::delete-group-by-group-id-command]]
   [:fields
    [:map
     group-id-spec]]])

(defn delete-group-by-group-id-command
  [group-id]
  (command/->command ::delete-group-by-group-id-command
                     delete-group-by-group-id-spec
                     #:group{:id group-id}))

(defn delete-group-by-group-id-event [delete-group-by-group-id-command]
  (event/->event ::group-deleted-event {:from-command delete-group-by-group-id-command}))

