(ns photoni.webapp.infra.postgres.group.group-postgres-repo
  "Group adapter - Postgres repository"
  (:require [mount.core :refer [defstate]]
            [hugsql.core :as hugsql]
            [photoni.webapp.infra.postgres.utils :refer [->boolean]]
            [photoni.webapp.domain.group.group :refer [GroupRepositoryProtocol]]
            [photoni.webapp.infra.postgres.db-postgres :refer [db]]
            [photoni.webapp.domain.group.group :as group]))

(hugsql/def-db-fns "photoni/webapp/infra/postgres/group/group.sql")

(def group-db->group-domain-fields
  {:group_id   :group/id
   :name       :group/name
   :updated_by :group/updated-by
   :updated_at :group/updated-at})

(defn group-domain-fields->group-db
  [group-domain]
  (merge (clojure.set/rename-keys group-domain (clojure.set/map-invert group-db->group-domain-fields))
         {:updated_by "TODO-group-name"}))

(defn group-db->group-domain
  [group-db]
  (clojure.set/rename-keys group-db group-db->group-domain-fields))

(defrecord GroupPostgresRepository []
  GroupRepositoryProtocol
  (get-groups [group-repo]
    (map group-db->group-domain (select-groups db)))
  (create-group [group-repo group-fields]
    (let [insert-db-row (upsert-group db (group-domain-fields->group-db group-fields))]
      (when insert-db-row
        (group-db->group-domain insert-db-row))))
  (get-group-by-group-id [group-repo group-id]
    (when-let [group-db (select-group-by-id db {:group_id group-id})]
      (group-db->group-domain group-db)))
  (delete-group-by-group-id [group-repo group-id]
    (->boolean (delete-group-by-id db {:group_id group-id}))))

(defstate group-postgres-repository
  :start (->GroupPostgresRepository))

