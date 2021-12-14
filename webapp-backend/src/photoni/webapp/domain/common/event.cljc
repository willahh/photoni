(ns photoni.webapp.domain.common.event)

(defn ->event
  ([name command entity]
   (cond-> #:event{:name         name
                   :created-at   (java.util.Date.)
                   :from-command command}
           entity (assoc :event/entity entity)))
  ([name command]
   (->event name command nil)))