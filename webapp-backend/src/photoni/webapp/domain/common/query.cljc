(ns photoni.webapp.domain.common.query
  (:require [photoni.webapp.domain.common.utils :refer [map->nsmap]]
            [photoni.webapp.domain.common.validation :as validation]
            [photoni.webapp.domain.user.user-entity]))

(defn ->query
  ([query-name query-spec query-fields]
   {:post [(validation/valid-spec query-spec %)]}
   (map->nsmap (cond-> {:name query-name}
                       query-fields (assoc :fields query-fields))
               (namespace query-spec)))
  ([query-name query-spec]
   (->query query-name query-spec nil)))
