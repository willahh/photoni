(ns photoni.webapp.service.user.user-service
  (:require [photoni.webapp.domain.common.event-bus :as event-bus]
            [photoni.webapp.domain.common.event :as event]
            [photoni.webapp.domain.common.log :as log]
            [photoni.webapp.domain.user.user-repo :as user-repo]))

(defn add-user
  [user-repo user-dto event-bus]
  (let [user-entity (user-repo/add-user user-repo user-dto)
        user-added-event (event/->event {:name   ::add-user
                                         :entity user-entity})]
    (event-bus/publish! event-bus user-added-event)
    user-entity))

(defn get-user
  [user-repo user-id event-bus]
  (let [user-entity (user-repo/get-user user-repo user-id)
        user-added-event (event/->event {:name   ::get-user
                                         :entity user-entity})]
    (event-bus/publish! event-bus user-added-event)
    user-entity))

(defn delete-user
  [user-repo user-id event-bus]
  (let [user-id (user-repo/delete-user user-repo user-id)
        user-added-event (event/->event {:name    ::delete-user
                                         :user-id user-id})]
    (event-bus/publish! event-bus user-added-event)
    user-id))
