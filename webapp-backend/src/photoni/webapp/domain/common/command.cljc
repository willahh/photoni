(ns photoni.webapp.domain.common.command
  (:require [photoni.webapp.domain.common.utils :refer [map->nsmap]]
            [photoni.webapp.domain.common.validation :as validation]
            [photoni.webapp.domain.user.user-entity]))

(defn ->command
  [command-name command-spec command-fields]
  {:post [(validation/valid-spec command-spec %)]}
  (map->nsmap {:name   command-name
               :fields command-fields}
              (namespace command-spec)))
