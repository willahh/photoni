(ns photoni.webapp.domain.common.validation
  (:require [malli.core :as m]
            [malli.error :as me]
            [photoni.webapp.domain.common.log :as log]
            #?(:clj [clojure.data.json :as json])))

(defn valid-spec [spec x]
  (do
    (def spec spec)
    (def x x)
    )
  (if (m/validate spec x)
    x
    (let [error-message (me/humanize (m/explain spec x))]
      (do #?(:clj (throw (java.lang.IllegalArgumentException. (json/write-str error-message)))
             :cljs (js/console.error (str error-message)))
          (log/info {:service ::valid-spec} error-message)))))
