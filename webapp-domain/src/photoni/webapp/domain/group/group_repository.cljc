(ns photoni.webapp.domain.group.group-repository)

(defprotocol groupRepository
  (find-groups-by [group-repo {:keys [fields clauses orders limit offset] :as query-fields}])
  (get-groups [group-repo])
  (create-group [group-repo group-fields])
  (get-group-by-group-id [group-repo group-id])
  (delete-group-by-group-id [group-repo group-id]))