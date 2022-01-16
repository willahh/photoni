(ns photoni.webapp.backend.group.group-postgres-repo
  "group adapter - Postgres repository"
  (:require [mount.core :refer [defstate]]
            [next.jdbc :as jdbc]
            [honey.sql :as sql]
            [honey.sql.helpers :as sqlh]
            [photoni.webapp.domain.group.group-repository :as group-repository]
            [photoni.webapp.backend.crud.crud :as crud]
            [photoni.webapp.backend.postgres.db-postgres :refer [db]]))

(def table-name :pgroup)
(def group-db->group-domain-fields
  {:pgroup/group_id :group/id
   :pgroup/name     :group/name})

(defrecord groupPostgresRepository []
  group-repository/groupRepository
  (find-groups-by [_ query-fields]
    (crud/find-many-by table-name group-db->group-domain-fields query-fields))
  (get-groups [_]
    (crud/find-many-by table-name group-db->group-domain-fields {:fields [:*]}))
  (create-group [_ group-fields]
    (crud/upsert table-name group-db->group-domain-fields group-fields :group_id :pgroup/group_id))
  (get-group-by-group-id [_ group-id]
    (crud/find-by-field-value table-name group-db->group-domain-fields :group_id group-id))
  (delete-group-by-group-id [group-repo group-id]
    (let [[group-db]
          (->> (-> (sqlh/delete-from table-name)
                   (sqlh/where [:= :group_id group-id])
                   (sqlh/returning :*)
                   sql/format)
               (jdbc/execute! db))]
      (not-empty group-db))))

(defstate group-repository-postgres
  :start (->groupPostgresRepository))

(comment
  (group-repository/find-groups-by
    group-repository-postgres
    {:fields [:*]
     :orders [[:group_id :asc]]
     :limit  1
     ;;:offset 3
     })

  )
