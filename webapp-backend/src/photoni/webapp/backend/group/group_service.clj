(ns photoni.webapp.backend.group.group-service
  (:require [mount.core :refer [defstate]]
            [photoni.webapp.domain.common.event-bus-repo-inmem :refer [event-bus-repository-inmem]]
            [photoni.webapp.domain.group.group-service :refer [->groupService]]
            [photoni.webapp.backend.group.group-postgres-repo :refer [group-repository-postgres]]))

(defstate group-service-repo
  :start (->groupService group-repository-postgres event-bus-repository-inmem))
