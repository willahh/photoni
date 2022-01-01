(ns photoni.webapp.domain.user.user-repository)

(defprotocol UserRepository
  (find-users-by [user-repo {:keys [fields clauses orders limit offset] :as query-fields}])
  (get-users [user-repo])
  (create-user [user-repo user-fields])
  (get-user-by-user-id [user-repo user-id])
  (delete-user-by-user-id [user-repo user-id]))