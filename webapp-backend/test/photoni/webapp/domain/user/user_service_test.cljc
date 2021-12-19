(ns photoni.webapp.domain.user.user-service-test
  (:require [clojure.test :refer [deftest is testing]]
            [photoni.webapp.infra.inmem.eventbus-inmem-repo :refer [event-bus-inmem]]
            [photoni.webapp.infra.inmem.user-inmem-repo :refer [user-repository-inmem]]
            [photoni.webapp.domain.user.user-service :as user-service]
            [photoni.webapp.domain.user.user-command :as user-command]
            [photoni.webapp.domain.user.user-query :as user-query]))

(deftest scenario-add-retrieve-and-delete-user-test
  (let [user-id (java.util.UUID/randomUUID)
        create-user-command (user-command/create-user-command {:id    user-id
                                                               :name  "User"
                                                               :title "Title"
                                                               :email "user@email.com"
                                                               :role  "role1"
                                                               :age   24})
        _ (user-service/create-user create-user-command user-repository-inmem event-bus-inmem)
        user-retrieved (user-service/get-user-by-id (user-query/get-user-by-id-query user-id) user-repository-inmem event-bus-inmem)]

    (testing "Test user retrieved identity"
      (is (= (get-in create-user-command [:fields])
             user-retrieved)))

    (testing "Delete retrieved user then retrieved it again should return nil"
      (user-service/delete-user (user-command/delete-user-by-user-id-command user-id) user-repository-inmem event-bus-inmem)
      (is (= nil
             (user-service/get-user-by-id (user-query/get-user-by-id-query user-id) user-repository-inmem event-bus-inmem))))))




(deftest scenario-add-users-then-get-users-test
  (let [user-1 (user-service/create-user (user-command/create-user-command
                                           {:id    (java.util.UUID/randomUUID)
                                            :name  "User 1"
                                            :title "Title"
                                            :email "user@email.com"
                                            :role  "role1"
                                            :age   24})
                                         user-repository-inmem event-bus-inmem)
        user-2 (user-service/create-user (user-command/create-user-command
                                           {:id    (java.util.UUID/randomUUID)
                                            :name  "User 2"
                                            :title "Title"
                                            :email "user@email.com"
                                            :role  "role1"
                                            :age   24})
                                         user-repository-inmem event-bus-inmem)]
    (is (= [user-1 user-2]
           (user-service/get-users (user-query/get-users)
                                   user-repository-inmem event-bus-inmem)))))



