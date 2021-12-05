(ns photoni.webapp.domain.user.user-service-test
  (:require [clojure.test :refer [deftest is testing]]
            [photoni.webapp.infra.inmem.eventbus-inmem-repo :as eventbus-inmem-repo]
            [photoni.webapp.infra.inmem.user-inmem-repo :as user-inmem-repo]
            [photoni.webapp.service.user.user-service :as user-service]
            [photoni.webapp.domain.user.user-dto :as user-dto]))

(def user-repo-inmem (user-inmem-repo/->UserInmemoryRepository))
(def event-bus-inmem (eventbus-inmem-repo/->EventBusInMemory))

(deftest scenario-add-retrieve-and-delete-user-test
  (let [user-dto (user-dto/->user-dto {:name  "Name"
                                       :title "Title"
                                       :email "user@email.com"
                                       :role  "role"
                                       :age   24})
        user-entity (user-service/add-user user-repo-inmem user-dto event-bus-inmem)
        user-retrieved (user-service/get-user user-repo-inmem (:user/id user-entity) event-bus-inmem)
        user-id (get user-retrieved :user/id)]

    (testing "Test user retrieved identity"
      (is (= user-dto
           (dissoc user-retrieved :user/id))))

    (testing "Delete retrieved user then retrieved it again should return nil"
      (user-service/delete-user user-repo-inmem user-id event-bus-inmem)
      (is (= nil
             (user-service/get-user user-repo-inmem user-id event-bus-inmem))))))

