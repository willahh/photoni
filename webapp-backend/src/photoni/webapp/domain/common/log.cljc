(ns photoni.webapp.domain.common.log)

(defn info
  ([message]
   (info {} message))
  ([mdc message]
   (println mdc message)))

(defn error
  ([message]
   (info {} message))
  ([mdc message]
   (println mdc message)))

(defn warn
  ([message]
   (info {} message))
  ([mdc message]
   (println mdc message)))