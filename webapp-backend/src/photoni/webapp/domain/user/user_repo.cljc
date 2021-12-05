(ns photoni.webapp.domain.user.user-repo
  "User repository Port")

(defprotocol UserRepository
  (add-user [user-repo user-dto])
  (get-user [user-repo user-id])
  (delete-user [user-repo user-id]))
