(ns photoni.webapp.infra.postgres.db-postgres
  (:require [mount.core :refer [defstate]]
            [next.jdbc :as jdbc]))

(def db-config
  {:dbtype   "postgresql"
   :dbname   "photoni"
   :host     "localhost"
   :user     "user"
   :password "password"})

(defstate db
  :start (jdbc/get-datasource db-config)
  )
