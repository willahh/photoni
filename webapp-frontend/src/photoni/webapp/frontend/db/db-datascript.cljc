(ns photoni.webapp.frontend.db.db-datascript
  (:require [clojure.string :as str]
            [datascript.core :as d]
            [datascript.db :as db]))

(def schema {:user/id     {:db.unique :db.unique/identity}
             :user/name   {}
             :user/age    {}
             :user/parent {:db.valueType   :db.type/ref
                           :db.cardinality :db.cardinality/many}})
(def conn (d/create-conn schema))






























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


