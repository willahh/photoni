(ns photoni.webapp.domain.user.user-command-handler
  (:require
    [photoni.webapp.domain.common.command-executor :refer [register-command-handler]]
    [photoni.webapp.domain.user.user-command :as user-command]
    [photoni.webapp.domain.user.user-service :as user-service]))

(register-command-handler
  ::user-command/create-user-command
  #(user-service/create-user %))

(register-command-handler
  ::user-command/delete-user-by-user-id-command
  #(user-service/delete-user %))
