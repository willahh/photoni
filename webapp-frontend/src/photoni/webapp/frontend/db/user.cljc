(ns photoni.webapp.frontend.db.user)

;;;;;;;;;;;;; TODO: maybe work with entity-id
;; db utils
(defn field-id->eid
  "Find an entity id by a field name and a field id"
  [field field-id]
  )


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
  )

(defn find-all-users
  []
  )

(defn find-user-by-user-id
  [user-id]
  )

(defn delete-user-by-user-id
  [user-id]
  )



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


  )