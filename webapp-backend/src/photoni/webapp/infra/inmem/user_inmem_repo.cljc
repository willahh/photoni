(ns photoni.webapp.infra.inmem.user-inmem-repo
  "User adapter"
  (:require [photoni.webapp.domain.common.log :as log]
            [photoni.webapp.domain.user.user-repo :as user-repo]))

(def records (atom {}))

(defrecord UserInmemoryRepository []
  user-repo/UserRepository
  (add-user [user-repo user-dto]
    (let [user-id (str (java.util.UUID/randomUUID))
          user-entity (assoc user-dto :user/id user-id)]
      (swap! records assoc user-id user-entity)
      (log/info (str "User " user-id "entity added"))
      user-entity))
  (get-user-by-user-id [user-repo user-id]
    (let [user-entity (get @records user-id)]
      user-entity))
  (delete-user-by-user-id [user-repo user-id]
    (swap! records dissoc user-id)
    user-id))


(comment

  )




