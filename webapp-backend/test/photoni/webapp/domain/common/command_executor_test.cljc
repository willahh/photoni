(ns photoni.webapp.domain.common.command-executor-test
  (:require [clojure.test :refer [deftest is testing]]
            [photoni.webapp.domain.user.user-command :as user-command]
            [photoni.webapp.domain.common.command-executor :refer [execute]]))

(deftest execute-add-user-command
  (let [fields {:id    #uuid"6550ac0a-34cf-4235-858b-32644a29ea31"
                :name  "User"
                :title "Title"
                :email "user@email.com"
                :role  "role1"
                :age   24}
        cmd (user-command/create-user-command fields)]
    (execute cmd)))

(deftest execute-delete-user-command
  (let [user-id #uuid"6550ac0a-34cf-4235-858b-32644a29ea31"
        cmd (user-command/delete-user-by-user-id-command user-id)]
    (execute cmd)))


(comment
  (mount.core/start)
  )