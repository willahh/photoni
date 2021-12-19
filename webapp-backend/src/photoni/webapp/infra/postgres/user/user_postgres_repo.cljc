(ns photoni.webapp.infra.postgres.user.user-postgres-repo
  "User adapter - Postgres repository"
  (:require [clojure.java.jdbc :as j]
            [mount.core :refer [defstate]]
            [photoni.webapp.domain.common.log :as log]
            [hugsql.core :as hugsql]
            [photoni.webapp.domain.user.user-repository-protocol :refer [UserRepositoryProtocol]]
            [photoni.webapp.infra.postgres.db-postgres :refer [db]]
            [photoni.webapp.domain.user.user-entity :as user-entity]))

(hugsql/def-db-fns "photoni/webapp/infra/postgres/user/user.sql")


(defn user-fields->user-db
  [{:user/keys [id name title email role age] :as user-domain}]
  {:user-id    id
   :updated-by "user2"
   :name       name
   :title      title
   :email      email
   :role       role
   :age        age})

(defn user-db->user-domain
  [{:keys [user_id name title email role age updated_by updated_at]}]
  #:user{:id    user_id
         :name  name
         :title title
         :email email
         :role  role
         :age   age})

(defrecord UserPostgresRepository []
  UserRepositoryProtocol
  (get-users [user-repo]
    (map user-db->user-domain (select-users db)))
  (create-user [user-repo user-fields]
    (prn "add-user user-fields:" user-fields)
    (let [insert-db-row (upsert-user db (user-fields->user-db user-fields))]
      (when insert-db-row
        (user-db->user-domain insert-db-row))))
  (get-user-by-user-id [user-repo user-id]
    (when-let [user-db (select-user-by-id db {:user-id user-id})]
      (user-db->user-domain user-db)))
  (delete-user-by-user-id [user-repo user-id]
    (delete-user-by-id db {:user-id user-id})))

(defstate user-postgres-repository
  :start (->UserPostgresRepository))