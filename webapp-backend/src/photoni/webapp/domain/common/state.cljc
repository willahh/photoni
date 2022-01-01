(ns photoni.webapp.domain.common.state
  (:require [mount.core :refer [defstate]]
            [photoni.webapp.backend.user.user-postgres-repo :refer [user-repository-postgres]]
            [photoni.webapp.backend.group.group-postgres-repo :refer [group-repository-postgres]]
            [photoni.webapp.domain.common.event-bus-repo-inmem :refer [event-bus-repository-inmem]]))

;; TODO: Dissocier domain et infra/postgres !!
;; reader macro clj / cljs ? => clj repo postgres / cljs => repo front
(defstate user-repository :start user-repository-postgres)
(defstate group-repository :start group-repository-postgres)
(defstate event-bus-repository :start event-bus-repository-inmem)

(def command-type->command-handler (atom {}))
