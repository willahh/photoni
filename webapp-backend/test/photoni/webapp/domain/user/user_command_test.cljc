(ns photoni.webapp.domain.user.user-command-test
  (:require [clojure.test :refer [deftest is testing]])
  (:require [photoni.webapp.domain.user.user-command :refer [create-user-command
                                                             delete-user-by-user-id-command]]))

(deftest create-user-command-test
  (let [id (java.util.UUID/randomUUID)]
    (is (= {:type   :photoni.webapp.domain.user.user-command/create-user-command
            :fields #:user{:id    id
                           :name  "User",
                           :title "Title",
                           :email "user@email.com",
                           :role  :role/admin,
                           :age   24}}
           (create-user-command {:id    id
                                 :name  "User"
                                 :title "Title"
                                 :email "user@email.com"
                                 :role  :role/admin
                                 :age   24})))))

(deftest delete-user-by-user-id-command-test
  (let [id (java.util.UUID/randomUUID)]
    (is (= {:type   :photoni.webapp.domain.user.user-command/delete-user-by-user-id-command
            :fields #:user{:id id}}
           (delete-user-by-user-id-command id)))))
