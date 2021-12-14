(ns photoni.webapp.domain.user.user-event
  (:require [photoni.webapp.domain.common.event :as event]))

(defn user-added-event [command entity] (event/->event ::user-added-event command entity))
(defn user-retrieved-event [query entity] (event/->event ::user-retrieved-event query entity))
(defn user-deleted-event [command] (event/->event ::user-deleted-event command))

