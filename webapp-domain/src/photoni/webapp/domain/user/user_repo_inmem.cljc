(ns photoni.webapp.domain.user.user-repo-inmem
  "User adapter"
  (:require [photoni.webapp.domain.common.log :as log]
            [photoni.webapp.domain.user.user-repository-protocol :refer [UserRepositoryProtocol]]
            [mount.core :refer [defstate]]))

(def records (atom {}))

(defrecord UserInmemoryRepository []
  UserRepositoryProtocol
  (get-users [user-repo]
    (vals @records))
  (create-user [user-repo user-fields]
    (let [user-id (get user-fields :user/id)
          user-entity (assoc user-fields :user/id user-id)]
      (swap! records assoc user-id user-entity)
      (log/info (str "User " user-id "entity added"))
      (get @records user-id)))
  (get-user-by-user-id [user-repo user-id]
    (let [user-entity (get @records user-id)]
      user-entity))
  (delete-user-by-user-id [user-repo user-id]
    (swap! records dissoc user-id)
    (log/info (str "User " user-id "entity deleted"))))

(defstate user-repository-inmem
  :start (->UserInmemoryRepository))


