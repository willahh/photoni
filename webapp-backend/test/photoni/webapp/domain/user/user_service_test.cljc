(ns photoni.webapp.domain.user.user-service-test
  (:require [clojure.test :refer [deftest is testing]]
            [photoni.webapp.infra.inmem.eventbus-inmem-repo :refer [event-bus-inmem]]
            [photoni.webapp.infra.inmem.user-inmem-repo :refer [user-repository-inmem]]
            [photoni.webapp.domain.user.user-service :as user-service]
            [photoni.webapp.domain.user.user-command :as user-command]
            [photoni.webapp.domain.user.user-query :as user-query]))

(deftest scenario-add-retrieve-and-delete-user-test
  (let [user-id (java.util.UUID/randomUUID)
        add-user-command (user-command/create-user-command {:id    user-id
                                                            :name  "User"
                                                            :title "Title"
                                                            :email "user@email.com"
                                                            :role  "role1"
                                                            :age   24})
        _ (user-service/add-user add-user-command user-repository-inmem event-bus-inmem)
        user-retrieved (user-service/get-user-by-id (user-query/get-user-by-id-query user-id) user-repository-inmem event-bus-inmem)]

    (testing "Test user retrieved identity"
      (is (= (get-in add-user-command [:user.command/fields])
             user-retrieved)))

    (testing "Delete retrieved user then retrieved it again should return nil"
      (user-service/delete-user (user-command/delete-user-by-user-id-command user-id) user-repository-inmem event-bus-inmem)
      (is (= nil
             (user-service/get-user-by-id (user-query/get-user-by-id-query user-id) user-repository-inmem event-bus-inmem))))))




(comment
  (require '[photoni.webapp.domain.user.user-command :as user-command])
  (require '[photoni.webapp.infra.inmem.eventbus-inmem-repo :refer [event-bus-inmem]])
  (require '[photoni.webapp.infra.inmem.user-inmem-repo :refer [user-repository-inmem]])
  (require '[photoni.webapp.infra.postgres.user.user-postgres-repo :refer [user-postgres-repository]])

  (require '[clojure.spec.alpha :as s])
  (require '[clojure.spec.gen.alpha :as gen])




  (let [user-id (java.util.UUID/randomUUID)]
    (do "Generate a random user"
        (let [cmd (gen/generate (s/gen :user.command/user-command))
              cmd (update-in cmd [:user.command/fields :user/id] (constantly user-id))]
          (add-user cmd
                    user-postgres-repository event-bus-inmem)))
    (do "Delete user"
        (let [cmd (gen/generate (s/gen :delete-user-by-user-id-command/user-command))]
          (add-user cmd
                    user-postgres-repository event-bus-inmem))))

  )