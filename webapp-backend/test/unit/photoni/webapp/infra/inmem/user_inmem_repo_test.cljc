(ns unit.photoni.webapp.infra.inmem.user-inmem-repo-test
  (:require [clojure.test :refer [deftest is testing]]
            [photoni.webapp.domain.user.user-repository :as user-repository]
            [photoni.webapp.domain.common.event-bus-repo-inmem :refer [event-bus-repository-inmem]]
            [photoni.webapp.infra.inmem.user-inmem-repo :refer [user-repository-inmem]]))

(deftest add-user-test
  (let [user-id (java.util.UUID/randomUUID)
        add-user-fields #:user{:id    user-id
                               :name  "Name"
                               :title "Title"
                               :email "user@email.com"
                               :role  "role"
                               :age   24}
        _ (user-repository/create-user user-repository-inmem add-user-fields)]
    (is (= (user-repository/get-user-by-user-id user-repository-inmem user-id)
           add-user-fields))))

(deftest delete-user-test
  (let [user-id (java.util.UUID/randomUUID)
        add-user-fields #:user{:id    user-id
                               :name  "Name"
                               :title "Title"
                               :email "user@email.com"
                               :role  "role"
                               :age   24}
        _ (user-repository/create-user user-repository-inmem add-user-fields)
        _ (user-repository/delete-user-by-user-id user-repository-inmem user-id)]
    (is (nil? (user-repository/get-user-by-user-id user-repository-inmem user-id)))))
