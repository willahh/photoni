(ns photoni.webapp.domain.user.user-query
  (:require [clojure.spec.alpha :as s]
            [photoni.webapp.domain.common.query :as query]
            [photoni.webapp.domain.common.validation :as validation]))

(s/def :user.query/name keyword?)
(s/def :user.query/fields (s/keys :req [:user/id]))
(s/def :user.query/user-query (s/keys :req [:user.query/name
                                            :user.query/fields]))
(defn get-user-by-id-query
  [user-id]
  (query/->query ::get-user-by-id-query
                 :user.query/user-query
                 #:user{:id user-id}))
