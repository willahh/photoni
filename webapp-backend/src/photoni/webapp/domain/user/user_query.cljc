(ns photoni.webapp.domain.user.user-query
  (:require [clojure.spec.alpha :as s]
            [photoni.webapp.domain.common.query :as query]
            [photoni.webapp.domain.common.validation :as validation]))

;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ GetUserByIdQuery                                                          │
;; └───────────────────────────────────────────────────────────────────────────┘
(s/def :query.user.get-user-by-id/name keyword?)
(s/def :query.user.get-user-by-id/fields (s/keys :req [:user/id]))
(s/def :query.user.get-user-by-id/query (s/keys :req [:query.user.get-user-by-id/name
                                                      :query.user.get-user-by-id/fields]))
(defn get-user-by-id-query
  [user-id]
  (query/->query ::get-user-by-id-query
                 :query.user.get-user-by-id/query
                 #:user{:id user-id}))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ GetUsersQuery                                                             │
;; └───────────────────────────────────────────────────────────────────────────┘
(s/def :query.user.get-users/name keyword?)
(s/def :query.user.get-users/query (s/keys :req [:query.user.get-users/name]))
(defn get-users
  []
  (query/->query ::get-users
                 :query.user.get-users/query))

