(ns photoni.webapp.domain.common.query
  (:require [photoni.webapp.domain.common.utils :refer [map->nsmap]]
            [photoni.webapp.domain.common.validation :as validation]
            [photoni.webapp.domain.user.user-entity]))

(defn ->query
  ([query-type query-spec query-fields]
   {:post [(validation/valid-spec query-spec %)]}
   (cond-> {:type query-type}
           query-fields (assoc :fields query-fields)))
  ([query-type query-spec]
   (->query query-type query-spec nil)))
