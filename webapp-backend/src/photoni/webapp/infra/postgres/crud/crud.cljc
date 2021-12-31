(ns photoni.webapp.infra.postgres.crud.crud
  (:require [next.jdbc :as jdbc]
            [honey.sql :as sql]
            [honey.sql.helpers :as sqlh]
            [photoni.webapp.infra.postgres.db-postgres :refer [db]])
  (:import (java.util UUID)))

(defn- map-domain-fields->db-fields
  [fields-dictionary domain-fields]
  (let [domain-fields->db-fields (clojure.set/map-invert fields-dictionary)]
    (mapv (fn [field]
            (if-let [x (field domain-fields->db-fields)]
              x
              field))
          domain-fields)))

(defn- map-db-fields->domain-fields
  [fields-dictionary db-fields]
  (mapv (fn [db-field]
          (clojure.set/rename-keys db-field fields-dictionary))
        db-fields))

(defn- find-by!
  "Find fields by clauses, orders, limit and offset"
  [{:keys [table fields clauses orders limit offset] :as opts}]
  (->> (cond-> (-> (sqlh/select fields)
                   (sqlh/from table))
               (seq clauses) (sqlh/where clauses)
               (seq orders) (conj (apply sqlh/order-by orders))
               limit (sqlh/limit limit)
               offset (sqlh/offset offset)
               true sql/format)
       (jdbc/execute! db)))

(defn find-many-by
  [table-name fields-dictionary {:keys [fields clauses orders limit offset] :as query-fields}]
  (let [opts (cond-> {:table  table-name
                      :fields (if (seq fields)
                                (map-domain-fields->db-fields fields-dictionary fields)
                                [:*])}
                     (seq clauses) (assoc :clauses clauses)
                     orders (assoc :orders orders)
                     limit (assoc :limit limit)
                     offset (assoc :offset offset))
        rows (->> (find-by! opts))
        rows-count (count rows)
        [{:keys [count]}] (->> (-> (sqlh/select [[:count :*]])
                                   (sqlh/from table-name)
                                   sql/format)
                               (jdbc/execute! db))]
    {:total count
     :count rows-count
     :rows  (map-db-fields->domain-fields fields-dictionary rows)}))

(defn upsert
  ([table-name fields-dictionary entity-to-insert conflict-field]
   (let [domain-fields->db-fields (clojure.set/map-invert fields-dictionary)
         [row]
         (let [update-set (apply sqlh/do-update-set domain-fields->db-fields)]
           (->> (-> (sqlh/insert-into table-name)
                    (sqlh/values [entity-to-insert])
                    (sqlh/upsert (-> (sqlh/on-conflict conflict-field)
                                     update-set))
                    (sqlh/returning :*)
                    sql/format)
                (jdbc/execute! db)
                (map-db-fields->domain-fields fields-dictionary)))]
     row))
  ([table-name fields-dictionary entity-to-insert]
   (upsert table-name fields-dictionary entity-to-insert :id)))

(defn find-by-field-value
  [table-name fields-dictionary field-name field-value]
  (->> (jdbc/execute! db (-> (sqlh/select [:*])
                             (sqlh/from table-name)
                             (sqlh/where [:= field-name field-value])
                             sql/format))
       (map-db-fields->domain-fields fields-dictionary)
       first))