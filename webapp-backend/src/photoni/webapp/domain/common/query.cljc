(ns photoni.webapp.domain.common.query
  (:require [photoni.webapp.domain.common.utils :refer [map->nsmap]]
            [photoni.webapp.domain.common.validation :as validation]
            [photoni.webapp.domain.user.user-entity]))

(defn ->query
  [query-name query-spec query-fields]
  {:post [(validation/valid-spec query-spec %)]}
  (map->nsmap {:name   query-name
               :fields query-fields}
              (namespace query-spec)))
