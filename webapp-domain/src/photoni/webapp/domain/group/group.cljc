(ns photoni.webapp.domain.group.group
  (:require [malli.core :as m]
            [malli.generator :as mg]
            [photoni.webapp.domain.utils :as utils]
            [photoni.webapp.domain.common.command :as command]
            [photoni.webapp.domain.common.event :as event]
            [photoni.webapp.domain.common.query :as query]
            [photoni.webapp.domain.role.role-entity :as role]))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [constructor]                                                             │
;; └───────────────────────────────────────────────────────────────────────────┘
(defn ->group
  [{:keys [id name] :as fields}]
  #:group{:id   id
          :name name})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [spec]                                                                    │
;; └───────────────────────────────────────────────────────────────────────────┘
(def non-empty-string
  (m/from-ast {:type       :string
               :properties {:min 1}}))

(def spec-id [:group/id {:title               "id parameter"
                         :description         "Description for id parameter"
                         :json-schema/default "4c0e630d-19f9-47a8-9e89-225dc7c8e338"}
              uuid?])
(def spec-name [:group/name {:title               "Name parameter"
                             :description         "Description for name parameter"
                             :json-schema/default "group"}
                [:and string? non-empty-string]])


(def spec-group
  [:map
   spec-id
   spec-name])

(defn set-spec-field-optional
  [spec-field]
  (update-in spec-field [1] #(assoc % :optional true)))



;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [find-groups-by] [spec] [query] [event]                                   │
;; └───────────────────────────────────────────────────────────────────────────┘
(def find-clauses-spec
  [:cat [:enum :or :and]
   [:repeat
    [:tuple [:enum :or :and]
     [:tuple
      [:enum := :> :< :like :ilike]
      [:or keyword? string? int?]
      [:or keyword? string? int?]]]]])

(def order-by-spec [:vector {:min 1, :max 10} [:tuple keyword? [:enum :asc :desc]]])

(def find-groups-by-fields-spec
  [:map
   [:fields [:vector keyword?]]
   [:clauses {:optional true} find-clauses-spec]
   [:orders {:optional true} order-by-spec]
   [:limit {:optional true} nat-int?]
   [:offset {:optional true} nat-int?]])

(def find-groups-by-spec
  [:map
   [:type [:enum ::find-groups-by]]
   [:fields
    find-groups-by-fields-spec]])

(def find-groups-by-response-spec
  [:map
   [:total int?]
   [:count int?]
   [:rows [:vector [:map
                    (set-spec-field-optional spec-id)
                    (set-spec-field-optional spec-name)]]]])

(defn find-groups-by-query
  [query-fields]
  (query/->query ::find-groups-by find-groups-by-spec query-fields))

(defn find-groups-by-event [find-groups-by-query results]
  (event/->event ::find-groups-by-event {:from-query find-groups-by-query
                                         :results    results}))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [query] get-groups                                                        │
;; └───────────────────────────────────────────────────────────────────────────┘
(def get-groups-query-spec
  [:map
   [:type [:enum ::get-groups]]])

(defn get-groups-query
  []
  (query/->query ::get-groups get-groups-query-spec))

(defn groups-retrieved-event [get-groups-query groups]
  (event/->event ::groups-retrieved-event {:from-query get-groups-query
                                           :entities   groups}))

;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [query] get-group-by-id                                                   │
;; └───────────────────────────────────────────────────────────────────────────┘
(def get-group-by-id-query-spec
  [:map
   [:type [:enum ::get-group-by-id-query]]
   [:fields
    [:map spec-id]]])

(defn get-group-by-id-query
  [group-id]
  (query/->query ::get-group-by-id-query
                 get-group-by-id-query-spec
                 #:group{:id group-id}))

(defn group-retrieved-event [get-group-query entity]
  (event/->event ::group-retrieved-event {:from-query get-group-query
                                          :entity     entity}))



;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [command] create-group                                                    │
;; └───────────────────────────────────────────────────────────────────────────┘
(def create-group-command-spec
  [:map
   [:type [:enum ::create-group-command]]
   [:fields
    (utils/filter-spec-fields spec-group #{:group/id})]])


(defn create-group-command
  [{:group/keys [id name] :as fields}]
  (command/->command ::create-group-command
                     create-group-command-spec
                     (cond-> #:group{:name name}
                             id (assoc :group/id id))))

(defn group-added-event [add-group-command entity]
  (event/->event ::group-added-event {:from-command add-group-command
                                      :entity       entity}))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [command] delete-group-by-group-id                                        │
;; └───────────────────────────────────────────────────────────────────────────┘
(def delete-group-by-group-id-command-spec
  [:map
   [:type [:enum ::delete-group-by-group-id-command]]
   [:fields
    [:map
     spec-id]]])

(defn delete-group-by-group-id-command
  [group-id]
  (command/->command ::delete-group-by-group-id-command
                     delete-group-by-group-id-command-spec
                     #:group{:id group-id}))

(defn group-deleted-event [delete-group-by-group-id-command]
  (event/->event ::group-deleted-event {:from-command delete-group-by-group-id-command}))

