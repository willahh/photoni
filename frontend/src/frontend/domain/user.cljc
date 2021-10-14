(ns frontend.domain.user
  (:require [clojure.spec.alpha :as s]
            [clojure.test.check.generators]
            [clojure.spec.gen.alpha :as gen]
            [frontend.samples.data :as samples-data]))

(def email-regex #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")
(s/def :generic-type/email (s/and string? #(re-matches email-regex %)))

(s/def :user/id nat-int?)
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

(defn generate-user-stub []
  (gen/generate (s/gen :user/user)))

(comment
  "Some tests"
  (generate-user-stub)
  => #:user{:id 126,
            :name "barelyplonker",
            :title "Marketing Manager",
            :email "konit@aol.com",
            :role "KtW604pWpq2Krs730K6",
            :age 1284658}

  )




