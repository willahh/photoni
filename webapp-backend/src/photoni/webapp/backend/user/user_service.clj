(ns photoni.webapp.backend.user.user-service
  (:require [mount.core :refer [defstate]]
            [photoni.webapp.domain.common.event-bus-repo-inmem :refer [event-bus-repository-inmem]]
            [photoni.webapp.domain.user.user-service :refer [->UserService]]
            [photoni.webapp.backend.user.user-postgres-repo :refer [user-repository-postgres]]))

(defstate user-service-repo
  :start (->UserService user-repository-postgres event-bus-repository-inmem))
