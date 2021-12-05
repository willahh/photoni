(ns photoni.webapp.infra.postgres.db-postgres
  (:require [jdbc.pool.c3p0 :as pool]))

(def db
  (pool/make-datasource-spec
    {:subprotocol "postgresql"
     :subname "//localhost:5432/photoni"
     :user "user"
     :password "password"}))



(comment

  (sql/db-do-commands "postgresql://localhost:5432/shouter"
                      (sql/create-table-ddl :testing [[:data :text]]))

  )