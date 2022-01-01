(ns photoni.webapp.frontend.pages.user.user-service
  (:require [mount.core :refer [defstate]]
            [photoni.webapp.domain.common.event-bus-repo-inmem :refer [event-bus-repository-inmem]]
            [photoni.webapp.domain.user.user-service :refer [->UserService]]
            [photoni.webapp.frontend.pages.user.user-repository-frontend :refer [user-repository-js]]))

(def user-service-repo (->UserService @user-repository-js @event-bus-repository-inmem))
