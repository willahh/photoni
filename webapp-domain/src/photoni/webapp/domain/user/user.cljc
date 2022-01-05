(ns photoni.webapp.domain.user.user
  (:require [malli.core :as m]
            [malli.generator :as mg]
            [photoni.webapp.domain.common.command :as command]
            [photoni.webapp.domain.common.event :as event]
            [photoni.webapp.domain.common.query :as query]))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [constructor]                                                             │
;; └───────────────────────────────────────────────────────────────────────────┘
(defn ->user
  [{:keys [id name title email role age] :as fields}]
  #:user{:id    id
         :name  name
         :title title
         :email email
         :role  role
         :age   age})


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [spec]                                                                    │
;; └───────────────────────────────────────────────────────────────────────────┘

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
(def spec-title [:user/title {:title               "Title parameter"
                              :description         "Description for title parameter"
                              :json-schema/default 30}
                 [:and string? non-empty-string]])
(def spec-email [:user/email {:title               "User email"
                              :description         ""
                              :json-schema/default "user@mail.com"}
                 [:and string? non-empty-string [:re email-regex]]])
(def spec-role [:user/role {:title               "Role parameter"
                            :description         "Description for role parameter"
                            :json-schema/default "role/admin"}
                [:enum "role/admin"]])                      ;; TODO: use keyword here (need to fix api coercion before, not trivial stuff)
(def spec-age [:user/age {:title               "Age parameter"
                          :description         "Description for age parameter"
                          :json-schema/default 30}
               nat-int?])

(def spec-user
  [:map
   spec-id
   spec-name
   spec-title
   spec-email
   spec-role
   spec-age])

(defn set-spec-field-optional
  [spec-field]
  (update-in spec-field [1] #(assoc % :optional true)))



;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [find-users-by] [spec] [query] [event]                                    │
;; └───────────────────────────────────────────────────────────────────────────┘
(def find-clauses-spec
  [:cat [:enum :or :and]
   [:repeat
    [:tuple [:enum :or :and]
     [:tuple
      [:enum := :> :< :like :ilike]
      [:or keyword? string? int?]
      [:or keyword? string? int?]]]]])

(def order-by-spec [:vector {:min 1, :max 10} [:tuple keyword? [:enum :asc :desc]]])

(def find-users-by-fields-spec
  [:map
   [:fields [:vector keyword?]]
   [:clauses {:optional true} find-clauses-spec]
   [:orders {:optional true} order-by-spec]
   [:limit {:optional true} nat-int?]
   [:offset {:optional true} nat-int?]])

(def find-users-by-spec
  [:map
   [:type [:enum ::find-users-by]]
   [:fields
    find-users-by-fields-spec]])

(def find-users-by-response-spec
  [:map
   [:total int?]
   [:count int?]
   [:rows [:vector [:map
                    (set-spec-field-optional spec-id)
                    (set-spec-field-optional spec-name)
                    (set-spec-field-optional spec-title)
                    (set-spec-field-optional spec-email)
                    (set-spec-field-optional spec-role)
                    (set-spec-field-optional spec-age)]]]])

(defn find-users-by-query
  [query-fields]
  (query/->query ::find-users-by find-users-by-spec query-fields))

(defn find-users-by-event [find-users-by-query results]
  (event/->event ::find-users-by-event {:from-query find-users-by-query
                                        :results    results}))

(comment
  ;; were : [:and [:= :a [:param :baz]] [:= :b [:inline 42]]]
  (require '[malli.generator :as mg])
  (require '[malli.provider :as mp])
  (mg/generate find-users-by-spec)
  (mg/generate find-users-by-response-spec)
  (mg/generate spec-user)

  (find-users-by-query {:fields  [:user/name
                                  :user/id]
                        :clauses [:and
                                  [:or [:= :name "test"]]]
                        :orders  [[:id :asc]]
                        :limit   10,
                        :offset  1
                        })
  )

;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [query] get-users                                                         │
;; └───────────────────────────────────────────────────────────────────────────┘
(def get-users-query-spec
  [:map
   [:type [:enum ::get-users]]])

(defn get-users-query
  []
  (query/->query ::get-users get-users-query-spec))

(defn users-retrieved-event [get-users-query users]
  (event/->event ::users-retrieved-event {:from-query get-users-query
                                          :entities   users}))

;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [query] get-user-by-id                                                    │
;; └───────────────────────────────────────────────────────────────────────────┘
(def get-user-by-id-query-spec
  [:map
   [:type [:enum ::get-user-by-id-query]]
   [:fields
    [:map spec-id]]])

(defn get-user-by-id-query
  [user-id]
  (query/->query ::get-user-by-id-query
                 get-user-by-id-query-spec
                 #:user{:id user-id}))

(defn user-retrieved-event [get-user-query entity]
  (event/->event ::user-retrieved-event {:from-query get-user-query
                                         :entity     entity}))



;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [command] create-user                                                     │
;; └───────────────────────────────────────────────────────────────────────────┘
(def create-user-command-spec
  [:map
   [:type [:enum ::create-user-command]]
   [:fields
    spec-user]])

(defn create-user-command
  [{:user/keys [id name title email role age] :as fields}]
  (command/->command ::create-user-command
                     create-user-command-spec
                     #:user{:id    id
                            :name  name
                            :title title
                            :email email
                            :role  role
                            :age   age}))

(defn user-added-event [add-user-command entity]
  (event/->event ::user-added-event {:from-command add-user-command
                                     :entity       entity}))


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [command] delete-user-by-user-id                                          │
;; └───────────────────────────────────────────────────────────────────────────┘
(def delete-user-by-user-id-command-spec
  [:map
   [:type [:enum ::delete-user-by-user-id-command]]
   [:fields
    [:map
     spec-id]]])

(defn delete-user-by-user-id-command
  [user-id]
  (command/->command ::delete-user-by-user-id-command
                     delete-user-by-user-id-command-spec
                     #:user{:id user-id}))

(defn user-deleted-event [delete-user-by-user-id-command]
  (event/->event ::user-deleted-event {:from-command delete-user-by-user-id-command}))

