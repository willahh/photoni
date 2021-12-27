(ns photoni.webapp.domain.user.user-service
  (:require [photoni.webapp.domain.common.state :as state]
            [photoni.webapp.domain.common.event-bus :as event-bus]
            [photoni.webapp.domain.user.user-repository-protocol :as user-repository-protocol]
            [photoni.webapp.domain.user.user-event :as user-event]))

(defn get-users
  [get-users-query]
  (let [users-entities (user-repository-protocol/get-users state/user-repository)
        event (user-event/users-retrieved-event get-users-query users-entities)]
    (event-bus/publish! state/event-bus-repository event)
    users-entities))

(defn get-user-by-id
  [get-user-query]
  (let [user-id (get-in get-user-query [:fields :user/id])
        user-entity (user-repository-protocol/get-user-by-user-id state/user-repository user-id)
        event (user-event/user-retrieved-event get-user-query user-entity)]
    (event-bus/publish! state/event-bus-repository event)
    user-entity))

(defn create-user
  [create-user-command]
  (let [user-fields (get-in create-user-command [:fields])
        user-entity (user-repository-protocol/create-user state/user-repository user-fields)
        event (user-event/user-added-event create-user-command user-entity)]
    (event-bus/publish! state/event-bus-repository event)
    user-entity))

(defn delete-user
  [delete-user-by-user-id-command]
  (let [user-id (get-in delete-user-by-user-id-command [:fields :user/id])
        deleted? (user-repository-protocol/delete-user-by-user-id state/user-repository user-id)
        event (user-event/user-deleted-event delete-user-by-user-id-command)]
    (event-bus/publish! state/event-bus-repository event)
    deleted?))
