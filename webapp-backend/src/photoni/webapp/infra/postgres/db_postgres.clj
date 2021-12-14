(ns photoni.webapp.infra.postgres.db-postgres
  (:require [jdbc.pool.c3p0 :as pool]
            [mount.core :refer [defstate]]))

(defstate db :start (pool/make-datasource-spec
                      {:subprotocol "postgresql"
                       :subname     "//localhost:5432/photoni"
                       :user        "user"
                       :password    "password"})
  :stop (fn [] (prn "Postgres c3po pool connection stopped")))
