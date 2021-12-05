(ns photoni.webapp.domain.common.validation
  (:require [clojure.spec.alpha :as s]
            [photoni.webapp.domain.common.log :as log]))

(defn valid-spec [spec x]
  (if (s/valid? spec x)
    x
    (log/info {:service ::valid-spec} (s/explain spec x))))
