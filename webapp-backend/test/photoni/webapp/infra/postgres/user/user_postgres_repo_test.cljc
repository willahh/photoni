(ns photoni.webapp.infra.postgres.user.user-postgres-repo-test
  (:require [clojure.test :refer [deftest is testing]]
            [photoni.webapp.domain.user.user-repository-protocol :as user-repo]
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

(deftest select-users-test
  (testing "Insert two users, call get-users, should returns the same entities"
    (let [user-1 (user-repo/add-user user-postgres-repository
                                     #:user{:id    (java.util.UUID/randomUUID)
                                            :name  "Name 1"
                                            :title "Title 1"
                                            :email "user@email.com"
                                            :role  "role"
                                            :age   24})
          user-2 (user-repo/add-user user-postgres-repository
                                     #:user{:id    (java.util.UUID/randomUUID)
                                            :name  "Name 2"
                                            :title "Title 2"
                                            :email "user@email.com"
                                            :role  "role"
                                            :age   24})]
      (is (= [user-1 user-2]
             (user-repo/get-users user-postgres-repository))))))
