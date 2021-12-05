(ns photoni.webapp.domain.user.user-dto
  "User data transfert object"
  (:require [clojure.spec.alpha :as s]
            [photoni.webapp.domain.common.validation :as validation]))

(defn ->user-dto
  [{:keys [id name title email role age]}]
  {:post [(validation/valid-spec :user/user %)]}
  #:user{:id    id
         :name  name
         :title title
         :email email
         :role  role
         :age   age})