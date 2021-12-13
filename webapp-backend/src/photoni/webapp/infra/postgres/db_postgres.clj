(ns photoni.webapp.infra.postgres.db-postgres
  (:require [jdbc.pool.c3p0 :as pool]))

(def db
  (pool/make-datasource-spec
    {:subprotocol "postgresql"
     :subname "//localhost:5432/photoni"
     :user "user"
     :password "password"}))
