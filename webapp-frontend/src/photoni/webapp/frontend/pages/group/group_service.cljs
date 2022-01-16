(ns photoni.webapp.frontend.pages.group.group-service
  (:require [mount.core :refer [defstate]]
            [photoni.webapp.domain.common.event-bus-repo-inmem :refer [event-bus-repository-inmem]]
            [photoni.webapp.domain.group.group-service :refer [->groupService]]
            [photoni.webapp.frontend.utils.event-bus-repo-inmem-js :refer [event-bus-repository-inmem-js]]
            [photoni.webapp.frontend.pages.group.group-repository-frontend :refer [group-repository-js]]))

(def group-service-repo (->groupService @group-repository-js @event-bus-repository-inmem-js))
