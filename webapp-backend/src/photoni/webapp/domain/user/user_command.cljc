(ns photoni.webapp.domain.user.user-command
  (:require [clojure.spec.alpha :as s]
            [photoni.webapp.domain.common.command :as command]))

;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ CreateUserCommand                                                         │
;; └───────────────────────────────────────────────────────────────────────────┘
(s/def :command.user.create-user/type (s/with-gen keyword? #(s/gen #{:photoni.webapp.domain.user.user-command/create-user-command})))
(s/def :command.user.create-user/fields :user/user)
(s/def :command.user.create-user/command (s/keys :req-un [:command.user.create-user/type
                                                          :command.user.create-user/fields]))

(defn create-user-command
  [{:keys [id name title email role age] :as fields}]
  (command/->command ::create-user-command
                     :command.user.create-user/command
                     #:user{:id    id
                            :name  name
                            :title title
                            :email email
                            :role  role
                            :age   age}))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ DeleteUserByUserIdCommand                                                 │
;; └───────────────────────────────────────────────────────────────────────────┘
(s/def :command.user.delete-user-by-user-id/type (s/with-gen keyword? #(s/gen #{::delete-user-by-user-id-command})))
(s/def :command.user.delete-user-by-user-id/fields (s/keys :req [:user/id]))
(s/def :command.user.delete-user-by-user-id/command (s/keys :req-un [:command.user.delete-user-by-user-id/type
                                                                     :command.user.delete-user-by-user-id/fields]))
(defn delete-user-by-user-id-command
  [user-id]
  (command/->command ::delete-user-by-user-id-command
                     :command.user.delete-user-by-user-id/command
                     #:user{:id user-id}))

