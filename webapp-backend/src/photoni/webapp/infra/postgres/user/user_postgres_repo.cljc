(ns photoni.webapp.infra.postgres.user.user-postgres-repo
  "User adapter - Postgres repository"
  (:require [clojure.java.jdbc :as j]
            [photoni.webapp.domain.common.log :as log]
            [hugsql.core :as hugsql]
            [photoni.webapp.domain.user.user-repo :as user-repo]
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
  user-repo/UserRepository
  (add-user [user-repo user-fields]
    (let [user-db-row (user-fields->user-db user-fields)]
      (upsert-user db user-db-row)))
  (get-user-by-user-id [user-repo user-id]
    (user-db->user-domain (select-user-by-id db {:user-id user-id})))
  (delete-user-by-user-id [user-repo user-id]
    (delete-user-by-id db {:user-id user-id})
    user-id))

(comment
  (def user-postgres-repository (->UserPostgresRepository))

  (def uuid (java.util.UUID/randomUUID))
  (user-repo/add-user user-postgres-repository
                      {:id    uuid
                       :name  "Name"
                       :title "Title"
                       :email "user@email.com"
                       :role  "role"
                       :age   24})
  (user-repo/get-user-by-user-id user-postgres-repository
                                 uuid)

  (user-repo/delete-user-by-user-id user-postgres-repository
                                    uuid)





  (create-users-table db)

  (upsert-user db {:user-id    uuid
                   :updated-by "user2"
                   :name       "barelyplonker",
                   :title      "Marketing Manager",
                   :email      "konit@aol.com",
                   :role       "KtW604pWpq2Krs730K6",
                   :age        1284658})







  (create-characters-table db)
  (character-by-id db {:id 1})
  (characters-by-ids-specify-cols-sqlvec db
                                         {:ids [1 2], :cols ["name" "specialty"]})

  (j/db-do-commands db "drop table test")
  (create-characters-table db)

  )