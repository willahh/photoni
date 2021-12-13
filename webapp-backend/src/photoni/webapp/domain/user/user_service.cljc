(ns photoni.webapp.domain.user.user-service
  (:require [photoni.webapp.domain.common.event-bus :as event-bus]
            [photoni.webapp.domain.common.event :as event]
            [photoni.webapp.domain.common.log :as log]
            [photoni.webapp.domain.user.user-repo :as user-repo]
            [photoni.webapp.domain.user.user-event :as user-event]
            [photoni.webapp.domain.user.user-command :as user-command]
            [photoni.webapp.domain.user.user-query :as user-query]))

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
  [user-repo user-id event-bus]
  (let [user-id (user-repo/delete-user-by-user-id user-repo user-id)
        user-added-event (event/->event {:name    ::delete-user
                                         :user-id user-id})]
    (event-bus/publish! event-bus user-added-event)
    user-id))


(comment
  (require '[photoni.webapp.infra.inmem.user-inmem-repo :as user-inmem-repo])
  (require '[photoni.webapp.infra.inmem.eventbus-inmem-repo :as eventbus-inmem-repo])

  (def user-repo-inmem (user-inmem-repo/->UserInmemoryRepository))
  (def event-bus-inmem (eventbus-inmem-repo/->EventBusInMemory))
  (def id (java.util.UUID/randomUUID))
  (add-user (user-command/create-user-command {:id    id
                                               :name  "User"
                                               :title "Title"
                                               :email "user@email.com"
                                               :role  "role1"
                                               :age   24})
            user-repo-inmem
            event-bus-inmem)

  (get-user-by-id (user-query/get-user-by-id-query id)
                  user-repo-inmem
                  event-bus-inmem)

  (let [a (user-query/get-user-by-id-query (java.util.UUID/randomUUID))]
    (get-in a [:user.query/fields :user/id]))
  )