(ns photoni.webapp.domain.user.user-entity
  "User entity"
  (:require [webapp.domain.resources.samples.data :as samples-data]
            [malli.core :as m]))

(def email-regex #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")

(def non-empty-string
  (m/from-ast {:type       :string
               :properties {:min 1}}))

(def spec-id [:user/id {:title               "id parameter"
                        :description         "Description for id parameter"
                        :json-schema/default "4c0e630d-19f9-47a8-9e89-225dc7c8e338"}
              uuid?])
(def spec-name [:user/name {:title               "Name parameter"
                            :description         "Description for name parameter"
                            :json-schema/default "User"}
                [:and string? non-empty-string]])
(def spec-title [:user/title [:and string? non-empty-string]])
(def spec-email [:user/email {:title               "User email"
                              :description         ""
                              :json-schema/default "user@mail.com"}
                 [:and string? non-empty-string [:re email-regex]]])
(def spec-role [:user/role {:title               "Role parameter"
                            :description         "Description for rle parameter"
                            :json-schema/default "role/admin"}
                [:enum "role/admin"]])                      ;; TODO: use keyword here (need to fix api coercion before, not trivial stuff)
(def spec-age [:user/age nat-int?])

(def spec-user
  [:map
   spec-id
   spec-name
   spec-title
   spec-email
   spec-role
   spec-age])

(defn ->user
  [{:keys [id name title email role age] :as fields}]
  #:user{:id    id
         :name  name
         :title title
         :email email
         :role  role
         :age   age})

