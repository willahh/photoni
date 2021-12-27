(ns photoni.webapp.domain.common.state
  (:require [mount.core :refer [defstate]]
            [photoni.webapp.infra.postgres.user.user-postgres-repo :refer [user-postgres-repository]]
            [photoni.webapp.infra.inmem.eventbus-inmem-repo :refer [event-bus-inmem]]))

(defstate user-repository
  :start user-postgres-repository)

(defstate event-bus
  :start event-bus-inmem)

#_(defstate command-type->command-handler
  :start (atom {})
  :stop (fn [] {}))

(def command-type->command-handler (atom {}))
