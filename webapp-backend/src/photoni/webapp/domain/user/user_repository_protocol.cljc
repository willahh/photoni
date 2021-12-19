(ns photoni.webapp.domain.user.user-repository-protocol
  "User repository Protocol")

(defprotocol UserRepositoryProtocol
  (get-users [user-repo])
  (create-user [user-repo user-fields])
  (get-user-by-user-id [user-repo user-id])
  (delete-user-by-user-id [user-repo user-id]))
