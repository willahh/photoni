(ns photoni.webapp.domain.common.log)

(defn info
  ([message]
   (info {} message))
  ([mdc message]
   (println mdc message)))

(defn error
  ([message]
   (error {} message))
  ([mdc message]
   (println (str mdc message))))

(defn warn
  ([message]
   (info {} message))
  ([mdc message]
   (println mdc message)))