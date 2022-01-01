(ns photoni.webapp.domain.user.user-service
  (:require [photoni.webapp.domain.common.event-bus :as event-bus]
            [photoni.webapp.domain.user.user-repository :as user-repository]
            [photoni.webapp.domain.user.user :as user]))

(defprotocol UserServiceProtocol
  (find-users-by [_ find-users-by-query])
  (get-users [_ get-users-query])
  (create-user [_ create-user-command])
  (get-user-by-user-id [_ get-user-query])
  (delete-user-by-user-id [_ delete-user-by-user-id-command]))

(defrecord UserService [user-repo event-bus-repo]
  UserServiceProtocol
  (get-users
    [_ get-users-query]
    (prn "get-users (domain)" get-users-query)
    (prn "user-repo" user-repo)

    (let [users-entities (user-repository/get-users user-repo)
          event (user/users-retrieved-event get-users-query users-entities)]
      (event-bus/publish! event-bus-repo event)
      users-entities))
  (find-users-by
    [_ find-users-by-query]
    (let [query-fields (get-in find-users-by-query [:fields])
          result (user-repository/find-users-by user-repo query-fields)
          event (user/find-users-by-event find-users-by-query result)]
      (event-bus/publish! event-bus-repo event)
      result))
  (get-user-by-user-id
    [_ get-user-query]
    (let [user-id (get-in get-user-query [:fields :user/id])
          user-entity (user-repository/get-user-by-user-id user-repo user-id)
          event (user/user-retrieved-event get-user-query user-entity)]
      (event-bus/publish! event-bus-repo event)
      user-entity))
  (create-user
    [_ create-user-command]
    (let [user-fields (get-in create-user-command [:fields])
          user-entity (user-repository/create-user user-repo user-fields)
          event (user/user-added-event create-user-command user-entity)]
      (event-bus/publish! event-bus-repo event)
      user-entity))
  (delete-user-by-user-id
    [_ delete-user-by-user-id-command]
    (let [user-id (get-in delete-user-by-user-id-command [:fields :user/id])
          deleted? (user-repository/delete-user-by-user-id user-repo user-id)
          event (user/user-deleted-event delete-user-by-user-id-command)]
      (event-bus/publish! event-bus-repo event)
      deleted?)))

