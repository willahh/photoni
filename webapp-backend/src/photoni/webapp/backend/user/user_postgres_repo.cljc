(ns photoni.webapp.backend.user.user-postgres-repo
  "User adapter - Postgres repository"
  (:require [mount.core :refer [defstate]]
            [next.jdbc :as jdbc]
            [honey.sql :as sql]
            [honey.sql.helpers :as sqlh]
            [photoni.webapp.domain.user.user-repository :as user-repository]
            [photoni.webapp.backend.crud.crud :as crud]
            [photoni.webapp.backend.postgres.db-postgres :refer [db]]))

(def user-db->user-domain-fields
  {:puser/id         :user/id
   :puser/name       :user/name
   :puser/title      :user/title
   :puser/email      :user/email
   :puser/role       :user/role
   :puser/age        :user/age
   :puser/updated_by :user/updated-by
   :puser/updated_at :user/updated-at})

(defrecord UserPostgresRepository []
  user-repository/UserRepository
  (find-users-by [_ query-fields]
    (crud/find-many-by :puser user-db->user-domain-fields query-fields))
  (get-users [_]
    (crud/find-many-by :puser user-db->user-domain-fields {:fields [:*]}))
  (create-user [_ user-fields]
    (crud/upsert :puser user-db->user-domain-fields user-fields))
  (get-user-by-user-id [_ user-id]
    (crud/find-by-field-value :puser user-db->user-domain-fields :id user-id))
  (delete-user-by-user-id [user-repo user-id]
    (let [[user-db]
          (->> (-> (sqlh/delete-from :puser)
                   (sqlh/where [:= :id user-id])
                   (sqlh/returning :*)
                   sql/format)
               (jdbc/execute! db))]
      (not-empty user-db))))

(defstate user-repository-postgres
  :start (->UserPostgresRepository))

(comment
  (user-repository-protocol/find-users-by
    user-repository-postgres
    {:fields [:*]
     :orders [[:id :asc]]
     :limit  1
     ;;:offset 3
     })

  )
