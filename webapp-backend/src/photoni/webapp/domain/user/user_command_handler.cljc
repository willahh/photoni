(ns photoni.webapp.domain.user.user-command-handler
  (:require
    [mount.core :as mount]
    [photoni.webapp.domain.common.state :refer [user-repository event-bus]]
    [photoni.webapp.domain.common.command-executor :refer [register-command-handler]]
    [photoni.webapp.domain.user.user-command :as user-command]
    [photoni.webapp.domain.common.event-bus :as event-bus]
    [photoni.webapp.domain.user.user-repository-protocol :as user-repository-protocol]
    [photoni.webapp.domain.user.user-event :as user-event]))

(defn create-user-command-handler
  [create-user-command user-repo event-bus]
  (let [user-fields (get-in create-user-command [:fields])
        user-entity (user-repository-protocol/create-user user-repo user-fields)
        event (user-event/user-added-event create-user-command user-entity)]
    (event-bus/publish! event-bus event)))
(register-command-handler :photoni.webapp.domain.user.user-command/create-user-command
                          #(create-user-command-handler % user-repository event-bus))

;; TODO: defonce into create-user-command-handler function
(defonce test (do (prn "defonce test")))

(defn delete-user-command-handler
  [delete-user-by-user-id-command user-repo event-bus]
  (let [user-id (get-in delete-user-by-user-id-command [:fields :user/id])
        _ (user-repository-protocol/delete-user-by-user-id user-repo user-id)
        event (user-event/user-deleted-event delete-user-by-user-id-command)]
    (event-bus/publish! event-bus event)))



