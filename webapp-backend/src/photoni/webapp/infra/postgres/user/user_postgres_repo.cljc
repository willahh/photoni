(ns photoni.webapp.infra.postgres.user.user-postgres-repo
  "User adapter - Postgres repository"
  (:require [clojure.java.jdbc :as j]
            [mount.core :refer [defstate]]
            [photoni.webapp.domain.common.log :as log]
            [hugsql.core :as hugsql]
            [photoni.webapp.infra.postgres.utils :refer [->boolean]]
            [photoni.webapp.domain.user.user-repository-protocol :refer [UserRepositoryProtocol]]
            [photoni.webapp.infra.postgres.db-postgres :refer [db]]
            [photoni.webapp.domain.user.user-entity :as user-entity]))

(hugsql/def-db-fns "photoni/webapp/infra/postgres/user/user.sql")

(def user-db->user-domain-fields
  {:user_id :user/id
   :name :user/name
   :title :user/title
   :email :user/email
   :role :user/role
   :age :user/age
   :updated_by :user/updated-by
   :updated_at :user/updated-at})

(defn user-domain-fields->user-db
  [user-domain]
  (merge (clojure.set/rename-keys user-domain (clojure.set/map-invert user-db->user-domain-fields))
         {:updated_by "TODO-user-name"}))

(defn user-db->user-domain
  [user-db]
  (clojure.set/rename-keys user-db user-db->user-domain-fields))

(defrecord UserPostgresRepository []
  UserRepositoryProtocol
  (get-users [user-repo]
    (map user-db->user-domain (select-users db)))
  (create-user [user-repo user-fields]
    (let [insert-db-row (upsert-user db (user-domain-fields->user-db user-fields))]
      (when insert-db-row
        (user-db->user-domain insert-db-row))))
  (get-user-by-user-id [user-repo user-id]
    (when-let [user-db (select-user-by-id db {:user_id user-id})]
      (user-db->user-domain user-db)))
  (delete-user-by-user-id [user-repo user-id]
    (->boolean (delete-user-by-id db {:user_id user-id}))))

(defstate user-postgres-repository
  :start (->UserPostgresRepository))
