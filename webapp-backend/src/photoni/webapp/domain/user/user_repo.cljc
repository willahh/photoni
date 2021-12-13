(ns photoni.webapp.domain.user.user-repo
  "User repository Port")

(defprotocol UserRepository
  (add-user [user-repo user-fields])
  (get-user-by-user-id [user-repo user-id])
  (delete-user-by-user-id [user-repo user-id]))
