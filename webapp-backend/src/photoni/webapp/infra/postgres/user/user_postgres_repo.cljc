(ns photoni.webapp.infra.postgres.user.user-postgres-repo
  "User adapter"
  (:require [clojure.java.jdbc :as j]
            [photoni.webapp.domain.common.log :as log]
            [photoni.webapp.domain.user.user-dto :as user-dto]
            [hugsql.core :as hugsql]
            [photoni.webapp.domain.user.user-repo :as user-repo]
            [photoni.webapp.infra.postgres.db-postgres :refer [db]]
            [photoni.webapp.domain.user.user-entity :as user-entity]))

(hugsql/def-db-fns "photoni/webapp/infra/postgres/user/user.sql")


(defn user-domain->user-db
  [{:user/keys [id name title email role age] :as user-domain}]
  {:user-id    id
   :updated-by "user2"
   :name       name
   :title      title
   :email      email
   :role       role
   :age        age})

(defrecord UserPostgresRepository []
  user-repo/UserRepository
  (add-user [user-repo user-dto]
    (let [user-db (user-domain->user-db user-dto)]
      (upsert-user db user-db)
      (log/info (str "User " (:user/id user-dto) "entity added"))
      (:user/id user-dto)))
  (get-user [user-repo user-id]
    "TODO")
  (delete-user [user-repo user-id]
    "TODO"))

(comment
  (def user-postgres-repository (->UserPostgresRepository))

  (def uuid (java.util.UUID/randomUUID))
  (user-repo/add-user user-postgres-repository
                      (user-dto/->user-dto {:id    uuid
                                            :name  "Name"
                                            :title "Title"
                                            :email "user@email.com"
                                            :role  "role"
                                            :age   24}))



  (create-users-table db)

  (upsert-user db {:user-id    uuid
                   :updated-by "user2"
                   :name       "barelyplonker",
                   :title      "Marketing Manager",
                   :email      "konit@aol.com",
                   :role       "KtW604pWpq2Krs730K6",
                   :age        1284658})

  (select-users db)





  (create-characters-table db)
  (character-by-id db {:id 1})
  (characters-by-ids-specify-cols-sqlvec db
                                         {:ids [1 2], :cols ["name" "specialty"]})

  (j/db-do-commands db "drop table test")
  (create-characters-table db)

  )