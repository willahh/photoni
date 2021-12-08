(ns photoni.webapp.domain.user.user-event
  (:require [photoni.webapp.domain.common.event :as event]))

(defn user-added-event [command entity] (event/->event ::user-added-event command entity))


(comment
  (user-added-event (photoni.webapp.domain.user.user-command/create-user-command {:id    (java.util.UUID/randomUUID)
                                                                                  :name  "User"
                                                                                  :title "Title"
                                                                                  :email "user@email.com"
                                                                                  :role  "role1"
                                                                                  :age   24})
                    #:user{:id    126,
                           :name  "barelyplonker",
                           :title "Marketing Manager",
                           :email "konit@aol.com",
                           :role  "KtW604pWpq2Krs730K6",
                           :age   1284658})
  )
