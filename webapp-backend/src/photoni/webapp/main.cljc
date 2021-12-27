(ns photoni.webapp.main
  (:require [mount.core :refer [defstate]]
            [photoni.webapp.domain.common.state :as state]
            [photoni.webapp.domain.user.user-service :as user-service]
            [photoni.webapp.api.server :as api-server]))

(comment
  (mount.core/start)
  (mount.core/stop)
  )