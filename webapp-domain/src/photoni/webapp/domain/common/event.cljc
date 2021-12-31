(ns photoni.webapp.domain.common.event)

(defn ->event
  [event-type payload]
  #:event{:uuid       (java.util.UUID/randomUUID)
          :type       event-type
          :created-at (java.util.Date.)
          :payload    payload})