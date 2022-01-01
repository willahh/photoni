(ns unit.photoni.webapp.infra.postgres.user.user-postgres-repo-test
  (:require [clojure.test :refer [deftest is testing]]
            [photoni.webapp.domain.user.user-repository :as user-repository]
            [photoni.webapp.domain.common.event-bus-repo-inmem :refer [event-bus-repository-inmem]]
            [photoni.webapp.infra.postgres.user.user-postgres-repo :refer [user-repository-postgres]]))

(deftest create-user-test
  (let [user-id (java.util.UUID/randomUUID)
        add-user-fields #:user{:id    user-id
                               :name  "Name"
                               :title "Title"
                               :email "user@email.com"
                               :role  "role"
                               :age   24}
        _ (user-repository/create-user user-repository-postgres add-user-fields)]
    (is (= (user-repository/get-user-by-user-id user-repository-postgres user-id)
           add-user-fields))))

(deftest delete-user-test
  (let [user-id (java.util.UUID/randomUUID)
        add-user-fields #:user{:id    user-id
                               :name  "Name"
                               :title "Title"
                               :email "user@email.com"
                               :role  "role"
                               :age   24}
        _ (user-repository/create-user user-repository-postgres add-user-fields)
        _ (user-repository/delete-user-by-user-id user-repository-postgres user-id)]
    (is (nil? (user-repository/get-user-by-user-id user-repository-postgres user-id)))))

(deftest select-users-test
  (testing "Insert two users, call get-users, should returns the same entities"
    (let [user-1 (user-repository/create-user user-repository-postgres
                                              #:user{:id    (java.util.UUID/randomUUID)
                                                     :name  "Name 1"
                                                     :title "Title 1"
                                                     :email "user@email.com"
                                                     :role  "role"
                                                     :age   24})
          user-2 (user-repository/create-user user-repository-postgres
                                              #:user{:id    (java.util.UUID/randomUUID)
                                                     :name  "Name 2"
                                                     :title "Title 2"
                                                     :email "user@email.com"
                                                     :role  "role"
                                                     :age   24})]
      (is (= [user-1 user-2]
             (user-repository/get-users user-repository-postgres))))))
