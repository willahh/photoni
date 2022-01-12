(ns photoni.webapp.domain.common.validation
  (:require [malli.core :as m]
            [malli.error :as me]
            [photoni.webapp.domain.common.log :as log]
            #?(:clj [clojure.data.json :as json])))

(defn valid-spec [spec x]
  (if (m/validate spec x)
    x
    (let [error-message (me/humanize (m/explain spec x))]
      (do #?(:clj (throw (java.lang.IllegalArgumentException. (json/write-str error-message)))
             :cljs (throw (js/Error. error-message)))
          (log/info {:service ::valid-spec} error-message)))))
