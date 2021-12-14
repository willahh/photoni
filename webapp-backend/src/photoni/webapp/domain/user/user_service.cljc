(ns photoni.webapp.domain.user.user-service
  (:require [photoni.webapp.domain.common.event-bus :as event-bus]
            [photoni.webapp.domain.user.user-repo :as user-repo]
            [photoni.webapp.domain.user.user-event :as user-event]))

(defn add-user
  [add-user-command user-repo event-bus]
  (let [user-fields (:user.command/fields add-user-command)
        user-entity (user-repo/add-user user-repo user-fields)
        event (user-event/user-added-event add-user-command user-entity)]
    (event-bus/publish! event-bus event)))

(defn get-user-by-id
  [get-user-query user-repo event-bus]
  (let [user-id (get-in get-user-query [:user.query/fields :user/id])
        user-entity (user-repo/get-user-by-user-id user-repo user-id)
        event (user-event/user-retrieved-event get-user-query user-entity)]
    (event-bus/publish! event-bus event)
    user-entity))

(defn delete-user
  [command user-repo event-bus]
  (let [user-id (get-in command [:delete-user-by-user-id-command/fields :user/id])
        _ (user-repo/delete-user-by-user-id user-repo user-id)
        event (user-event/user-deleted-event command)]
    (event-bus/publish! event-bus event)))
