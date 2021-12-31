(ns user
  (:require [mount.core]
            [photoni.webapp.domain.common.state-WIP-TODO]
            [photoni.webapp.domain.user.user-service]
            [photoni.webapp.api.external.server]))

(defn go []
  (mount.core/start))

(comment
  (mount.core/start)
  (mount.core/stop)
  )