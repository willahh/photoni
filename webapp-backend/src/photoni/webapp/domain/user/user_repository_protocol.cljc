(ns photoni.webapp.domain.user.user-repository-protocol)

(defprotocol UserRepositoryProtocol
  (find-users-by [user-repo {:keys [fields clauses orders limit offset]}])
  (get-users [user-repo])
  (create-user [user-repo user-fields])
  (get-user-by-user-id [user-repo user-id])
  (delete-user-by-user-id [user-repo user-id]))
