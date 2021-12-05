(ns photoni.webapp.domain.user.user-dto
  "User data transfert object"
  (:require [clojure.spec.alpha :as s]
            [photoni.webapp.domain.common.validation :as validation]))

(s/def ::user-dto :user/user-shared)

(defn ->user-dto
  [{:keys [name title email role age]}]
  {:post [(validation/valid-spec ::user-dto %)]}
  #:user{:name  name
         :title title
         :email email
         :role  role
         :age   age})