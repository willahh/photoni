(ns photoni.webapp.domain.common.validation
  (:require [malli.core :as m]
            [malli.error :as me]
            [photoni.webapp.domain.common.log :as log]))

(defn valid-spec [spec x]
  (if (m/validate spec x)
    x
    (log/info {:service ::valid-spec} (me/humanize (m/explain spec x)))))
