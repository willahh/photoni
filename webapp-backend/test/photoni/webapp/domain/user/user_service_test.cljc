(ns photoni.webapp.domain.user.user-service-test
  (:require [clojure.test :refer [deftest is testing]]
            [photoni.webapp.infra.inmem.eventbus-inmem-repo :as eventbus-inmem-repo]
            [photoni.webapp.infra.inmem.user-inmem-repo :as user-inmem-repo]
            [photoni.webapp.domain.user.user-service :as user-service]
            [photoni.webapp.domain.user.user-command :as user-command]
            [photoni.webapp.domain.user.user-query :as user-query]))

(def user-repo-inmem (user-inmem-repo/->UserInmemoryRepository))
(def event-bus-inmem (eventbus-inmem-repo/->EventBusInMemory))

(deftest scenario-add-retrieve-and-delete-user-test
  (let [user-id (java.util.UUID/randomUUID)
        add-user-command (user-command/create-user-command {:id    user-id
                                                            :name  "User"
                                                            :title "Title"
                                                            :email "user@email.com"
                                                            :role  "role1"
                                                            :age   24})
        _ (user-service/add-user add-user-command user-repo-inmem event-bus-inmem)
        user-retrieved (user-service/get-user-by-id (user-query/get-user-by-id-query user-id) user-repo-inmem event-bus-inmem)]

    (testing "Test user retrieved identity"
      (is (= add-user-command
             (dissoc user-retrieved :user/id))))

    (testing "Delete retrieved user then retrieved it again should return nil"
      (user-service/delete-user user-repo-inmem user-id event-bus-inmem)
      (is (= nil
             (user-service/get-user-by-id (user-query/get-user-by-id-query user-id) user-repo-inmem event-bus-inmem))))))

