(ns photoni.webapp.domain.user.user-service
  (:require [photoni.webapp.domain.common.event-bus :as event-bus]
            [photoni.webapp.domain.user.user-repository-protocol :as user-repository-protocol]
            [photoni.webapp.domain.user.user-event :as user-event]
            [photoni.webapp.domain.user.user-command :as user-command]
            [photoni.webapp.domain.user.user-command-handler :as user-command-handler]))

(defn get-users
  [get-users-query user-repo event-bus]
  (let [users-entities (user-repository-protocol/get-users user-repo)
        event (user-event/users-retrieved-event get-users-query users-entities)]
    (event-bus/publish! event-bus event)
    users-entities))

(defn get-user-by-id
  [get-user-query user-repo event-bus]
  (let [user-id (get-in get-user-query [:query.user.get-user-by-id/fields :user/id])
        user-entity (user-repository-protocol/get-user-by-user-id user-repo user-id)
        event (user-event/user-retrieved-event get-user-query user-entity)]
    (event-bus/publish! event-bus event)
    user-entity))

(defn create-user
  [create-user-command user-repo event-bus]
  (user-command-handler/create-user-command-handler create-user-command user-repo event-bus))

(defn delete-user
  [delete-user-by-user-id-command user-repo event-bus]
  (user-command-handler/delete-user-command-handler delete-user-by-user-id-command user-repo event-bus))
