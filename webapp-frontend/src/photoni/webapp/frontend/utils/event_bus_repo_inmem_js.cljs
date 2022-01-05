(ns photoni.webapp.frontend.utils.event-bus-repo-inmem-js
  (:require [cljs.core.async :refer [<!]]
            [mount.core :refer [defstate]]
            [photoni.webapp.domain.common.log :as log]
            [photoni.webapp.domain.common.event-bus :as event-bus])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defrecord EventBusInMemory []
  event-bus/EventBus
  (subscribe [_ event]
    (println "subscribe to:" event))
  (publish! [_ event]
    (let [entity (get-in event [:event/payload :entity])
          is-entity-async? (= (type entity) cljs.core.async.impl.channels/ManyToManyChannel)]
      (if is-entity-async?
        (go
          (let [entity-resolved (<! entity)
                event-updated (assoc-in event [:event/payload :entity] entity-resolved)]
            (log/info "Publish event" event-updated)))
        (log/info "Publish event" event)))))

(defstate event-bus-repository-inmem-js
  :start (->EventBusInMemory))
