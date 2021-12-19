(ns photoni.webapp.infra.inmem.user-inmem-repo-test
  (:require [clojure.test :refer [deftest is testing]]
            [photoni.webapp.domain.user.user-repository-protocol :as user-repo]
            [photoni.webapp.infra.inmem.eventbus-inmem-repo :refer [event-bus-inmem]]
            [photoni.webapp.infra.inmem.user-inmem-repo :refer [user-repository-inmem]]))

(deftest add-user-test
  (let [user-id (java.util.UUID/randomUUID)
        add-user-fields #:user{:id    user-id
                               :name  "Name"
                               :title "Title"
                               :email "user@email.com"
                               :role  "role"
                               :age   24}
        _ (user-repo/add-user user-repository-inmem add-user-fields)]
    (is (= (user-repo/get-user-by-user-id user-repository-inmem user-id)
           add-user-fields))))

(deftest delete-user-test
  (let [user-id (java.util.UUID/randomUUID)
        add-user-fields #:user{:id    user-id
                               :name  "Name"
                               :title "Title"
                               :email "user@email.com"
                               :role  "role"
                               :age   24}
        _ (user-repo/add-user user-repository-inmem add-user-fields)
        _ (user-repo/delete-user-by-user-id user-repository-inmem user-id)]
    (is (nil? (user-repo/get-user-by-user-id user-repository-inmem user-id)))))
