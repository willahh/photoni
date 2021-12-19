(ns photoni.webapp.domain.common.command-executor
  (:require [mount.core :refer [defstate]]
            [photoni.webapp.domain.common.state :as state]))

(defn register-command-handler
  [command-type command-handler]
  (swap! state/command-type->command-handler assoc command-type command-handler))

(defn execute
  [{:keys [type] :as command}]
  (let [command-handler (get @state/command-type->command-handler type)]
    (command-handler command)))

(comment
  (mount.core/start)
  (require '[photoni.webapp.domain.user.user-command :as user-command])

  (let [fields {:id    #uuid"6550ac0a-34cf-4235-858b-32644a29ea31"
                :name  "User"
                :title "Title"
                :email "user@email.com"
                :role  "role1"
                :age   24}
        add-user-command (user-command/create-user-command fields)]
    (execute add-user-command))

  )