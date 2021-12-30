(ns photoni.webapp.domain.user.user-command-handler
  (:require [photoni.webapp.domain.common.command-executor :refer [register-command-handler]]
            [photoni.webapp.domain.user.user :as user]
            [photoni.webapp.domain.user.user-service :as user-service]))

;; TODO: Maybe use a macro to generate handlers
(register-command-handler
  ::user/create-user-command
  #(user-service/create-user %))

(register-command-handler
  ::user/delete-user-by-user-id-command
  #(user-service/delete-user %))
