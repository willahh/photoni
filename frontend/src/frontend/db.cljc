(ns frontend.db
  (:require [clojure.string :as str]
            [datascript.core :as d]
            [datascript.db :as db]))

(def default-db
  {:users [#:user{:id 3,
                  :name "resolutefrayed",
                  :title "Marketing Manager",
                  :email "linuxhack@sbcglobal.net",
                  :role "J01hr9O1y974DL7oj3IvE6gaFb6MVf",
                  :age 173392}]})

(def schema {:user/id     {:db.unique :db.unique/identity}
             :user/name   {}
             :user/age    {}
             :user/parent {:db.valueType   :db.type/ref
                           :db.cardinality :db.cardinality/many}})
(def conn (d/create-conn schema))


(defn add-user
  [user]
  (prn "add-user")
  (d/transact! conn [user]))

(defn get-users-entities
  []
  (d/q '[:find ?e
         :where [?e :user/id]]
       @conn))

(defn find-all-users
  []
  (d/pull-many @conn '[*]
               (first (d/q '[:find ?e :where [?e]] @conn))))

(defn find-user-by-id
  [user-id]
  (d/pull @conn '[*] user-id))
























(comment


  (d/q '[:find ?e
         :where [?e :user/id]]
       @conn)

  (d/q '[:find (pull ?e pattern)
         :in $
         :where [?e :user/id ?name]])



  (d/q '[:find [(pull  [:name :order]) ...]
         :in   $ ]
         @conn)


  (d/pull @conn '[*] 2)

  (find-user-by-id 1)

  (add-user {:user/id   "1"
             :user/name "alice"
             :user/age  27})

  (get-users-entities)
  )



(defn add-car
  [conn car]
  (d/transact! conn car))

(defn get-cars
  []
  (d/q '[:find ?v
         :where [?e :maker/name ?v]]
       @conn))

(defn find-car-by-name
  [conn name]
  (d/q '[:find ?name
         :where
         [?e :maker/name "BMW"]
         [?c :car/maker ?e]
         [?c :car/name ?name]]
       @conn))


(comment
  (add-car conn [{:db/id         -1
                  :maker/name    "BMW"
                  :maker/country "Germany"}
                 {:car/maker  -1
                  :car/name   "i525"
                  :car/colors ["red" "green" "blue"]}])

  (add-car conn [
                 {:car/maker  -1
                  :car/name   "i525"
                  :car/colors ["red" "green" "blue"]}])

  (find-car-by-name conn "BMW")


  ;; inserts
  (d/transact! conn [{:maker/name    "Honda"
                      :maker/country "Japan"}])
  (d/transact! conn [{:db/id         -1
                      :maker/name    "BMW"
                      :maker/country "Germany"}
                     {:car/maker  -1
                      :car/name   "i525"
                      :car/colors ["red" "green" "blue"]}])

  (d/q '[:find ?name
         :where
         [?e :maker/name "BMW"]
         [?c :car/maker ?e]
         [?c :car/name ?name]]
       @conn)

  )


