(ns photoni.webapp.domain.common.command
  (:require [photoni.webapp.domain.common.utils :refer [map->nsmap]]
            [photoni.webapp.domain.common.validation :as validation]
            [photoni.webapp.domain.user.user-entity]))

(defn ->command
  [command-type command-spec command-fields]
  {:post [(validation/valid-spec command-spec %)]}
  {:type   command-type
   :spec   command-spec
   :fields command-fields})
