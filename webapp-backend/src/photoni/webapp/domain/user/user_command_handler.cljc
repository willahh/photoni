(ns photoni.webapp.domain.user.user-command-handler
  (:require
    [photoni.webapp.domain.common.state :refer [user-repository event-bus]]
    [photoni.webapp.domain.common.command-executor :refer [register-command-handler]]
    [photoni.webapp.domain.user.user-command :as user-command]
    [photoni.webapp.domain.common.event-bus :as event-bus]
    [photoni.webapp.domain.user.user-repository-protocol :as user-repository-protocol]
    [photoni.webapp.domain.user.user-event :as user-event]))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ CreateUserCommandHandler                                                  │
;; └───────────────────────────────────────────────────────────────────────────┘
(defn create-user-command-handler
  [create-user-command user-repo event-bus]
  (let [user-fields (get-in create-user-command [:fields])
        user-entity (user-repository-protocol/create-user user-repo user-fields)
        event (user-event/user-added-event create-user-command user-entity)]
    (event-bus/publish! event-bus event)
    user-entity))

(register-command-handler
  ::user-command/create-user-command
  #(create-user-command-handler % user-repository event-bus))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ DeleteUserCommandHandler                                                  │
;; └───────────────────────────────────────────────────────────────────────────┘
(defn delete-user-command-handler
  [delete-user-by-user-id-command user-repo event-bus]
  (let [user-id (get-in delete-user-by-user-id-command [:fields :user/id])
        _ (user-repository-protocol/delete-user-by-user-id user-repo user-id)
        event (user-event/user-deleted-event delete-user-by-user-id-command)]
    (event-bus/publish! event-bus event)))

(register-command-handler
  ::user-command/delete-user-by-user-id-command
  #(delete-user-command-handler % user-repository event-bus))





