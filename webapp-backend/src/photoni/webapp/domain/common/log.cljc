(ns photoni.webapp.domain.common.log)

(defn info
  ([message]
   (info {} message))
  ([mdc message]
   (println mdc message)))