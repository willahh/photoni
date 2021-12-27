(ns photoni.webapp.infra.inmem.eventbus-inmem-repo
  (:require [mount.core :refer [defstate]]
            [photoni.webapp.domain.common.log :as log]
            [photoni.webapp.domain.common.event-bus :as event-bus]))

(defrecord EventBusInMemory []
  event-bus/EventBus
  (subscribe [_ event]
    (println "subscribe to:" event))
  (publish! [_ event]
    (log/info "Publish event" event)))

(defstate event-bus-inmem-repository
          :start (->EventBusInMemory))
