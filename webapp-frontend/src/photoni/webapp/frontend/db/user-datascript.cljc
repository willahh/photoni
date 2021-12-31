(ns photoni.webapp.frontend.db.user-datascript
  (:require [photoni.webapp.frontend.db.db :refer [conn]]
            [datascript.core :as d]
            [datascript.db :as db]))

;;;;;;;;;;;;; TODO: maybe work with entity-id
;; db utils
(defn field-id->eid
  "Find an entity id by a field name and a field id"
  [field field-id]
  (let [entities (d/q `[:find ?e
                        :where [?e ~field ~field-id]]
                      @conn)
        entity-id (ffirst entities)]
    entity-id))


;; db user
(defn user-record->user
  "Transform an User record into a domain User"
  [user-record]
  (select-keys user-record
               [:user/id
                :user/name
                :user/title
                :user/email
                :user/role
                :user/age]))

(defn add-user
  [user]
  (d/transact! conn [user]))

(defn find-all-users
  []
  (let [user-records (d/q '[:find [(pull ?e [*]) ...]
                         :where
                         [?e :user/id]]
                       @conn)]
    (map user-record->user user-records)))

(defn find-user-by-user-id
  [user-id]
  (let [eid (field-id->eid :user/id user-id)]
    (when eid
      (d/pull @conn '[*] eid))))

(defn delete-user-by-user-id
  [user-id]
  (let [eid (field-id->eid :user/id user-id)]
    (when eid
      (d/transact! conn [[:db.fn/retractEntity eid]]))))



(comment
  (add-user {:user/id   1
             :user/name "alice1"
             :user/age  27})
  (add-user {:user/id   2
             :user/name "alice2"
             :user/age  27})

  (find-all-users)
  (find-user-by-user-id 1)
  (find-user-by-user-id 2)
  (delete-user-by-user-id 1)
  (delete-user-by-user-id 2)










  (let [user-id "1"
        entities (d/q `[:find ?e
                        :where [?e :user/id ~user-id]]
                      @conn)
        entity-id (ffirst entities)]
    entity-id)

  (d/q '[:find ?e
         :where [?e :user/id]]
       @conn)

  (d/q '[:find (pull ?e pattern)
         :in $
         :where [?e :user/id ?name]])



  (d/q '[:find [(pull [:name :order]) ...]
         :in $]
       @conn)


  (d/q '[:find (pull ?e [*])
         :where
         [?e :user/id]]
       @conn)

  (d/q '[:find [(pull ?e [*]) ...]
         :where
         [?e :user/id]]
       @conn)






  )