(ns photoni.webapp.domain.user.user-event
  (:require [photoni.webapp.domain.common.event :as event]))

(defn user-added-event [add-user-command entity]
  (event/->event ::user-added-event {:from-command add-user-command
                                     :entity       entity}))

(defn user-retrieved-event [get-user-query entity]
  (event/->event ::user-retrieved-event {:from-query get-user-query
                                         :entity     entity}))

(defn user-deleted-event [delete-user-by-user-id-command]
  (event/->event ::user-deleted-event {:from-command delete-user-by-user-id-command}))

(defn users-retrieved-event [get-users-query users]
  (event/->event ::users-retrieved-event {:from-query get-users-query
                                          :entities   users}))

