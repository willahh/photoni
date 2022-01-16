(ns photoni.webapp.domain.group.group-service
  (:require [photoni.webapp.domain.common.event-bus :as event-bus]
            [photoni.webapp.domain.group.group-repository :as group-repository]
            [photoni.webapp.domain.group.group :as group]))

(defprotocol groupServiceProtocol
  (find-groups-by [_ find-groups-by-query])
  (get-groups [_ get-groups-query])
  (create-group [_ create-group-command])
  (get-group-by-group-id [_ get-group-query])
  (delete-group-by-group-id [_ delete-group-by-group-id-command]))

(defrecord groupService [group-repo event-bus-repo]
  groupServiceProtocol
  (get-groups
    [_ get-groups-query]
    (let [groups-entities (group-repository/get-groups group-repo)
          event (group/groups-retrieved-event get-groups-query groups-entities)]
      (event-bus/publish! event-bus-repo event)
      groups-entities))
  (find-groups-by
    [_ find-groups-by-query]
    (let [query-fields (get-in find-groups-by-query [:fields])
          result (group-repository/find-groups-by group-repo query-fields)
          event (group/find-groups-by-event find-groups-by-query result)]
      (event-bus/publish! event-bus-repo event)
      result))
  (get-group-by-group-id
    [_ get-group-query]
    (let [group-id (get-in get-group-query [:fields :group/id])
          group-entity (group-repository/get-group-by-group-id group-repo group-id)
          event (group/group-retrieved-event get-group-query group-entity)]
      (event-bus/publish! event-bus-repo event)
      group-entity))
  (create-group
    [_ create-group-command]
    (let [group-fields (get-in create-group-command [:fields])
          group-entity (group-repository/create-group group-repo group-fields)
          event (group/group-added-event create-group-command group-entity)]
      (event-bus/publish! event-bus-repo event)
      group-entity))
  (delete-group-by-group-id
    [_ delete-group-by-group-id-command]
    (let [group-id (get-in delete-group-by-group-id-command [:fields :group/id])
          deleted? (group-repository/delete-group-by-group-id group-repo group-id)
          event (group/group-deleted-event delete-group-by-group-id-command)]
      (event-bus/publish! event-bus-repo event)
      deleted?)))

