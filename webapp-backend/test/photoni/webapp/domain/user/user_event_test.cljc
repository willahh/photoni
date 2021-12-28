(ns photoni.webapp.domain.user.user-event-test
  (:require [clojure.test :refer [deftest is testing]]
            [photoni.webapp.domain.user.user-entity :as user-entity]
            [photoni.webapp.domain.user.user-event :refer [user-added-event]]
            [photoni.webapp.domain.user.user-command :as user-command]))

(deftest user-added-event-test
  (let [fields {:id    #uuid"6550ac0a-34cf-4235-858b-32644a29ea31"
                :name  "User"
                :title "Title"
                :email "user@email.com"
                :role  "role/admin"
                :age   24}
        add-user-command (user-command/create-user-command fields)
        user-created-entity (user-entity/->user fields)]
    (is (= #:event{:uuid       #uuid"a58184aa-7c1b-4ebf-bf1e-0fb25678b857",
                   :type       :photoni.webapp.domain.user.user-event/user-added-event,
                   :created-at #inst"2021-12-19T12:37:45.746-00:00",
                   :payload    {:from-command #:command.user.create-user{:name   :photoni.webapp.domain.user.user-command/create-user-command,
                                                                         :spec   :command.user.create-user/command,
                                                                         :fields #:user{:id    #uuid"6550ac0a-34cf-4235-858b-32644a29ea31",
                                                                                        :name  "User",
                                                                                        :title "Title",
                                                                                        :email "user@email.com",
                                                                                        :role  "role/admin",
                                                                                        :age   24}},
                                :entity       #:user{:id    #uuid"6550ac0a-34cf-4235-858b-32644a29ea31",
                                                     :name  "User",
                                                     :title "Title",
                                                     :email "user@email.com",
                                                     :role  "role/admin",
                                                     :age   24}}}
           (user-added-event add-user-command user-created-entity)))))
