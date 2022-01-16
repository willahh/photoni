(ns photoni.webapp.backend.crud.crud
  (:require [next.jdbc :as jdbc]
            [honey.sql :as sql]
            [honey.sql.helpers :as sqlh]
            [photoni.webapp.backend.postgres.db-postgres :refer [db]])
  (:import (java.util UUID)))

(defn- map-domain-fields->db-fields
  [fields-dictionary domain-fields]
  (let [domain-fields->db-fields (clojure.set/map-invert fields-dictionary)]
    (mapv (fn [field]
            (if-let [x (field domain-fields->db-fields)]
              x
              field))
          domain-fields)))

(defn entity->db-fields
  [fields-dictionary entity-to-insert]
  (let [domain-fields->db-fields (clojure.set/map-invert fields-dictionary)]
    (reduce (fn [acc [k v]]
              (assoc acc (get domain-fields->db-fields k) v))
            {} entity-to-insert)))

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
  [table-name fields-dictionary entity-to-insert conflict-field primary-key-qualified-kw]
  (let [entity-to-insert (entity->db-fields fields-dictionary entity-to-insert)
        [row]
        (->> (-> (sqlh/insert-into table-name)
                 (sqlh/values [entity-to-insert])
                 (sqlh/upsert {:on-conflict   [conflict-field],
                               :do-update-set (filter #(not= % primary-key-qualified-kw) (keys fields-dictionary))})
                 (sqlh/returning :*)
                 sql/format)
             (jdbc/execute! db)
             (map-db-fields->domain-fields fields-dictionary))]
    row))

(defn find-by-field-value
  [table-name fields-dictionary field-name field-value]
  (->> (jdbc/execute! db (-> (sqlh/select [:*])
                             (sqlh/from table-name)
                             (sqlh/where [:= field-name field-value])
                             sql/format))
       (map-db-fields->domain-fields fields-dictionary)
       first))