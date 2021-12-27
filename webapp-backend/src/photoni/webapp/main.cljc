(ns photoni.webapp.main
  (:require [mount.core]
            [photoni.webapp.domain.common.state]
            [photoni.webapp.domain.user.user-service]
            [photoni.webapp.api.external.server]))

(comment
  (mount.core/start)
  (mount.core/stop)
  )