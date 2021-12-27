(ns photoni.webapp.domain.user.user-query
  (:require [photoni.webapp.domain.common.query :as query]
            [photoni.webapp.domain.user.user-entity :as user-entity]))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ GetUserByIdQuery                                                          │
;; └───────────────────────────────────────────────────────────────────────────┘
(def get-user-by-id-query-spec
  [:map
   [:type [:enum ::get-user-by-id-query]]
   [:fields
    [:map user-entity/spec-id]]])

(defn get-user-by-id-query
  [user-id]
  (query/->query ::get-user-by-id-query
                 get-user-by-id-query-spec
                 #:user{:id user-id}))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ GetUsersQuery                                                             │
;; └───────────────────────────────────────────────────────────────────────────┘
(def get-users-query-spec
  [:map
   [:type [:enum ::get-users]]])

(defn get-users-query
  []
  (query/->query ::get-users get-users-query-spec))



