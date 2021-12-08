(ns photoni.webapp.domain.common.command
  (:require [clojure.spec.alpha :as s]
            [photoni.webapp.domain.user.user-entity]
            [photoni.webapp.domain.common.validation :as validation]))

(defn ->command
  [command-name command-spec command-fields]
  {:post [(validation/valid-spec command-spec %)]}
  #:command{:name   command-name
            :fields command-fields})

(comment

  (require '[clojure.spec.gen.alpha :as gen])
  (s/def :command/name keyword?)
  (s/def :command/fields :user/user)
  (s/def :command/user-command (s/keys :req [:command/name :command/fields]))
  (->command ::create-user-command
             :command/user-command
             #:user{:id    (java.util.UUID/randomUUID)
                    :name  "User"
                    :title "Title"
                    :email "user@email.com"
                    :role  "role1"
                    :age   24})
  )
