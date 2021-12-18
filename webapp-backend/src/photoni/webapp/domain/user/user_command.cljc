(ns photoni.webapp.domain.user.user-command
  (:require [clojure.spec.alpha :as s]
            [photoni.webapp.domain.common.command :as command]
            [photoni.webapp.domain.common.validation :as validation]))

(s/def :user.command/name keyword?)
(s/def :user.command/name (s/with-gen keyword? #(s/gen #{:photoni.webapp.domain.user.user-command/create-user-command})))
(s/def :user.command/fields :user/user)
(s/def :user.command/user-command (s/keys :req [:user.command/name
                                                :user.command/fields]))
(defn create-user-command
  [{:keys [id name title email role age]}]
  (command/->command ::create-user-command
                     :user.command/user-command
                     #:user{:id    id
                            :name  name
                            :title title
                            :email email
                            :role  role
                            :age   age}))



(s/def :delete-user-by-user-id-command/name (s/with-gen keyword? #(s/gen #{::delete-user-by-user-id-command})))
(s/def :delete-user-by-user-id-command/fields (s/keys :req [:user/id]))
(s/def :delete-user-by-user-id-command/user-command (s/keys :req [:delete-user-by-user-id-command/name
                                                                  :delete-user-by-user-id-command/fields]))
(defn delete-user-by-user-id-command
  [user-id]
  (command/->command ::delete-user-by-user-id-command
                     :delete-user-by-user-id-command/user-command
                     #:user{:id user-id}))

