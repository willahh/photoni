(ns photoni.webapp.domain.group.group-command-handler
  (:require [photoni.webapp.domain.common.command-executor :refer [register-command-handler]]
            [photoni.webapp.domain.group.group :as group]
            [photoni.webapp.domain.group.group-service :as group-service]))
;
;(register-command-handler
;  ::group/create-group-command
;  #(group-service/create-group %))
;
;(register-command-handler
;  ::group/delete-group-by-group-id-command
;  #(group-service/delete-group-by-group-id %))
