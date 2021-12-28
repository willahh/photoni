(ns photoni.webapp.infra.postgres.user.user-postgres-repo
  "User adapter - Postgres repository"
  (:require [mount.core :refer [defstate]]
            [next.jdbc :as jdbc]
            [honey.sql :as sql]
            [honey.sql.helpers :as sqlh]
            [photoni.webapp.infra.postgres.crud.crud :as crud]
            [photoni.webapp.domain.user.user-repository-protocol :as user-repository-protocol :refer [UserRepositoryProtocol]]
            [photoni.webapp.infra.postgres.db-postgres :refer [db]]))

(def user-db->user-domain-fields
  {:puser/id         :user/id
   :puser/name       :user/name
   :puser/title      :user/title
   :puser/email      :user/email
   :puser/role       :user/role
   :puser/age        :user/age
   :puser/updated_by :user/updated-by
   :puser/updated_at :user/updated-at})

(defn user-domain-fields->user-db
  [user-domain]
  (merge (clojure.set/rename-keys user-domain (clojure.set/map-invert user-db->user-domain-fields))
         {:updated_by "TODO-user-name"}))

(defn user-db->user-domain
  [user-db]
  (clojure.set/rename-keys user-db user-db->user-domain-fields))


;; TODO: Renommer le get-users en find-users et l'exposer en API
;; avec toutes les options possibles (champs, tris, clauses, limit, offset)
(defrecord UserPostgresRepository []
  UserRepositoryProtocol
  (find-users-by [_ {:keys [fields clauses orders limit offset]}]
    (let [rows (->> (crud/find-by (cond-> {:table :puser}
                                          fields (assoc :fields fields)
                                          clauses (assoc :clauses clauses)
                                          orders (assoc :orders orders)
                                          limit (assoc :limit limit)
                                          offset (assoc :offset offset))))
          rows-count (count rows)
          [{:keys [count]}] (->> (-> (sqlh/select [[:count :*]])
                                     (sqlh/from :puser)
                                     sql/format)
                                 (jdbc/execute! db))]
      {:total count
       :count rows-count
       :rows  (mapv user-db->user-domain rows)}))
  (get-users [user-repo]
    (->> (crud/find-by {:table  :puser
                        :fields [:*]
                        ;;:orders [[:id :asc] [:title :desc]]
                        ;;:limit 1
                        ;;:offset 3
                        })
         (map user-db->user-domain)))
  (create-user [user-repo user-fields]
    (let [[row]
          (->> (-> (sqlh/insert-into :puser)
                   (sqlh/values [user-fields])
                   (sqlh/upsert (-> (sqlh/on-conflict :id)
                                    (sqlh/do-update-set :user/name
                                                        :user/title :user/email :user/role :user/age
                                                        :user/updated-by)))
                   (sqlh/returning :*)
                   sql/format)
               (jdbc/execute! db)
               (map user-db->user-domain))]
      row))
  (get-user-by-user-id [user-repo user-id]
    (let [[user-db]
          (->> (-> (sqlh/select :*)
                   (sqlh/from :puser)
                   (sqlh/where [:= :id user-id])
                   sql/format)
               (jdbc/execute! db)
               (map user-db->user-domain))]
      user-db))
  (delete-user-by-user-id [user-repo user-id]
    (let [[user-db]
          (->> (-> (sqlh/delete-from :puser)
                   (sqlh/where [:= :id user-id])
                   (sqlh/returning :*)
                   sql/format)
               (jdbc/execute! db))]
      (not-empty user-db))))

(defstate user-postgres-repository
  :start (->UserPostgresRepository))

(comment
  (user-repository-protocol/find-users-by
    user-postgres-repository
    {:fields [:*]
     :orders [[:id :asc]]
     :limit  1
     ;;:offset 3
     })

  )
