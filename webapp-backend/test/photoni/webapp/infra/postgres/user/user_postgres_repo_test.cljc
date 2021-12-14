(ns photoni.webapp.infra.postgres.user.user-postgres-repo-test
  (:require [clojure.test :refer [deftest is testing]]
            [photoni.webapp.domain.user.user-repo :as user-repo]
            [photoni.webapp.infra.inmem.eventbus-inmem-repo :refer [event-bus-inmem]]
            [photoni.webapp.infra.postgres.user.user-postgres-repo :refer [user-postgres-repository]]))

(deftest add-user-test
  (let [user-id (java.util.UUID/randomUUID)
        add-user-fields #:user{:id    user-id
                               :name  "Name"
                               :title "Title"
                               :email "user@email.com"
                               :role  "role"
                               :age   24}
        _ (user-repo/add-user user-postgres-repository add-user-fields)]
    (is (= (user-repo/get-user-by-user-id user-postgres-repository user-id)
           add-user-fields))))

(deftest delete-user-test
  (let [user-id (java.util.UUID/randomUUID)
        add-user-fields #:user{:id    user-id
                               :name  "Name"
                               :title "Title"
                               :email "user@email.com"
                               :role  "role"
                               :age   24}
        _ (user-repo/add-user user-postgres-repository add-user-fields)
        _ (user-repo/delete-user-by-user-id user-postgres-repository user-id)]
    (is (nil? (user-repo/get-user-by-user-id user-postgres-repository user-id)))))
