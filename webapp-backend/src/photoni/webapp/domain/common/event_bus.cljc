(ns photoni.webapp.domain.common.event-bus)

(defprotocol EventBus
  (subscribe [_ f])
  (publish! [_ event]))
