(ns photoni.webapp.domain.common.log)

(defn log
  ([message]
   (log {} message))
  ([mdc message]
   #?(:clj (println (str mdc message))
      :cljs (js/console.log (str mdc message)))))

(defn info
  ([message]
   (info {} message))
  ([mdc message]
   #?(:clj (println (str mdc message))
      :cljs (js/console.info (str mdc message)))))

(defn error
  ([message]
   (error nil message))
  ([mdc message]
   #?(:clj (println (str mdc message))
      :cljs (js/console.error (str mdc message)))))

(defn warn
  ([message]
   (info nil message))
  ([mdc message]
   #?(:clj (println (str mdc message))
      :cljs (js/console.warn (str mdc message)))))