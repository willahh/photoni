(ns photoni.webapp.domain.user.user-command-test
  (:require [clojure.test :refer [deftest is testing]])
  (:require [photoni.webapp.domain.user.user-command :refer [create-user-command]]))

(deftest create-user-command-test
  (let [id (java.util.UUID/randomUUID)]
    (is (= #:user.command{:name   :photoni.webapp.domain.user.user-command/create-user-command,
                          :fields #:user{:id    id
                                         :name  "User",
                                         :title "Title",
                                         :email "user@email.com",
                                         :role  "role1",
                                         :age   24}}
           (create-user-command {:id    id
                                 :name  "User"
                                 :title "Title"
                                 :email "user@email.com"
                                 :role  "role1"
                                 :age   24})))))
