(ns photoni.webapp.domain.user.user-entity
  "User entity"
  (:require [clojure.spec.alpha :as s]
            [clojure.test.check.generators]
            [spec-tools.core :as st]
            [clojure.spec.gen.alpha :as gen]
            [webapp.domain.resources.samples.data :as samples-data]
            [malli.core :as m]))

(def email-regex #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")
(s/def :generic-type/email (s/and string? #(re-matches email-regex %)))

(s/def :user/id uuid?)
(s/def :user/name (s/with-gen (s/and string? not-empty) #(s/gen samples-data/user-name)))
(s/def :user/title (s/with-gen (s/and string? not-empty) #(s/gen samples-data/job-title)))
(s/def :user/role string?)
(s/def :user/age nat-int?)
(s/def :user/email (s/with-gen :generic-type/email #(s/gen samples-data/email)))

(s/def :user/user (s/keys :req [:user/id
                                :user/name
                                :user/title
                                :user/email
                                :user/role
                                :user/age]))

(def non-empty-string
  (m/from-ast {:type       :string
               :properties {:min 1}}))

(def spec-id [:id uuid?])
(def spec-name [:name {:title "name parameter"
                       :description "Description for name parameter"
                       :json-schema/default "User"}
                [:and string? non-empty-string]])
(def spec-title [:title [:and string? non-empty-string]])
(def spec-email [:email {:title "User email"
                         :description ""
                         :json-schema/default "user@mail.com"}
                 [:and string? non-empty-string [:re email-regex]]])
(def spec-role [:role [:and string? non-empty-string]])
(def spec-age [:age nat-int?])

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

(defn generate-user-stub []
  (gen/generate (s/gen :user/user)))
