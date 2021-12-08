(ns photoni.webapp.domain.user.user-command
  (:require [clojure.spec.alpha :as s]
            [photoni.webapp.domain.common.command :as command]
            [photoni.webapp.domain.common.validation :as validation]))

(s/def :user.command/name keyword?)
(s/def :user.command/test string?)
(s/def :user.command/command :user/user)
(s/def :user.command/user-command (s/keys :req-un [:user.command/name
                                                   :user.command/test
                                                   :user.command/command]))
(defn create-user-command
  [{:keys [id name title email role age]}]
  (command/->command ::create-user-command
                     :command/user-command
                     #:user{:id    id
                            :name  name
                            :title title
                            :email email
                            :role  role
                            :age   age}))

;
;(s/def :command/name keyword?)
;(s/def :command/fields :user/user)
;(s/def :command/user-command (s/keys :req [:command/name :command/fields]))

(comment
  (require '[clojure.spec.gen.alpha :as gen])
  (gen/generate (s/gen :user.command/user-command))
  (create-user-command {:id    (java.util.UUID/randomUUID)
                        :name  "User"
                        :title "Title"
                        :email "user@email.com"
                        :role  "role1"
                        :age   24}))
