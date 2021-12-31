(ns photoni.webapp.domain.group.group-service
  (:require [photoni.webapp.domain.common.state :as state]
            [photoni.webapp.domain.common.event-bus :as event-bus]
            [photoni.webapp.domain.group.group :as group]))

(defn get-groups
  [get-groups-query]
  (let [groups-entities (group/get-groups state/group-repository)
        event (group/get-groups-event get-groups-query groups-entities)]
    (event-bus/publish! state/event-bus-repository event)
    groups-entities))

(defn get-group-by-group-id
  [get-group-query]
  (let [group-id (get-in get-group-query [:fields :group/id])
        group-entity (group/get-group-by-group-id state/group-repository group-id)
        event (group/get-group-by-id-event get-group-query group-entity)]
    (event-bus/publish! state/event-bus-repository event)
    group-entity))

(defn create-group
  [create-group-command]
  (let [group-fields (get-in create-group-command [:fields])
        group-entity (group/create-group state/group-repository group-fields)
        event (group/create-group-event create-group-command group-entity)]
    (event-bus/publish! state/event-bus-repository event)
    group-entity))

(defn delete-group-by-group-id
  [delete-group-by-group-id-command]
  (let [group-id (get-in delete-group-by-group-id-command [:fields :group/id])
        deleted? (group/delete-group-by-group-id state/group-repository group-id)
        event (group/delete-group-by-group-id-event delete-group-by-group-id-command)]
    (event-bus/publish! state/event-bus-repository event)
    deleted?))
