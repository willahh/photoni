(ns photoni.webapp.domain.search.search)

(defn search-clauses-json->search-clauses
  "Convert a search clause from a json format into search clauses"
  [search-clauses-json]
  (let [r (rest search-clauses-json)
        first-operator (keyword (first search-clauses-json))]
    (vec (concat [first-operator]
                 (mapv (fn [clause]
                         (let [operator (keyword (first clause))
                               r (rest clause)]
                           (vec (concat [operator]
                                        (mapv (fn [clause]
                                                (let [[field value] (rest clause)
                                                      field (keyword field)
                                                      comparator (keyword (first clause))]
                                                  [comparator field value]))
                                              r))))) r)))))
