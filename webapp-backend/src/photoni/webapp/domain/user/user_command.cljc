(ns photoni.webapp.domain.user.user-command
  (:require [photoni.webapp.domain.common.command :as command]
            [photoni.webapp.domain.user.user-entity :as user-entity]))

;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ CreateUserCommand                                                         │
;; └───────────────────────────────────────────────────────────────────────────┘
(def create-user-command-spec
  [:map
   [:type [:enum ::create-user-command]]
   [:fields
    user-entity/spec-user]])

(defn create-user-command
  [{:keys [id name title email role age] :as fields}]
  (command/->command ::create-user-command
                     create-user-command-spec
                     #:user{:id    id
                                  :name  name
                                  :title title
                                  :email email
                                  :role  role
                                  :age   age}))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ DeleteUserByUserIdCommand                                                 │
;; └───────────────────────────────────────────────────────────────────────────┘
(def delete-user-by-user-id-command-spec
  [:map
   [:type [:enum ::delete-user-by-user-id-command]]
   [:fields
    [:map
     user-entity/spec-id]]])

(defn delete-user-by-user-id-command
  [user-id]
  (command/->command ::delete-user-by-user-id-command
                     delete-user-by-user-id-command-spec
                     #:user{:id user-id}))

