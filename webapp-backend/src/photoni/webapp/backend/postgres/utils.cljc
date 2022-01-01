(ns photoni.webapp.backend.postgres.utils)

(defn ->boolean
  [n]
  ({0 false 1 true} n))