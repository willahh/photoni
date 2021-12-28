(ns photoni.webapp.infra.postgres.crud.crud
  (:require [next.jdbc :as jdbc]
            [honey.sql :as sql]
            [honey.sql.helpers :as sqlh]
            [photoni.webapp.infra.postgres.db-postgres :refer [db]]))

(defn find-by
  "Find fields by clauses, orders, limit and offset"
  [{:keys [table fields clauses orders limit offset]}]
  (->> (cond-> (-> (sqlh/select fields)
                   (sqlh/from table))
               (seq clauses) (sqlh/where clauses)
               (seq orders) (conj (apply sqlh/order-by orders))
               limit (sqlh/limit limit)
               offset (sqlh/offset offset)
               true sql/format)
       (jdbc/execute! db)))
