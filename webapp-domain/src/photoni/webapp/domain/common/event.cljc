(ns photoni.webapp.domain.common.event)

(defn ->event
  [event-type payload]
  #:event{:uuid       #?(:clj  (java.util.UUID/randomUUID)
                         :cljs (cljs.core/random-uuid))
          :type       event-type
          :created-at #?(:clj (java.util.Date.)
                         :cljs (.getTime (js/Date.)))
          :payload    payload})