(ns photoni.webapp.infra.postgres.utils)

(defn ->boolean
  [n]
  ({0 false 1 true} n))