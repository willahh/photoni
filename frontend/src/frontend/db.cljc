(ns frontend.db
  (:require [clojure.string :as str]
            [datascript.core :as d]
            [datascript.db :as db]
            [datascript.transit :as dt]
            #?(:clj [frontend.utils.macro :refer [profile]]
               :cljs [re-frame.core :as re-frame]))
  #?(:cljs (:require-macros
             [frontend.utils.macro :refer [profile]])) )


(def default-db
  {:name "re-frame"})


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









;;;;;;;;;;;;;; localstorage
(declare render persist)
(defn reset-conn! [db]
  (prn "reset-conn!" )
  (prn "db: " db)
  (reset! conn db)
  #_(render db)


  #_(assoc reframe-db :users (conj (:users db) {:id 1}))

  #_#?(:cljs (re-frame/dispatch-sync [:frontend.events/initialize-db]))


  (persist db))




(defonce history (atom []))
(def ^:const history-limit 10)

;; logging of all transactions (prettified)
(d/listen! conn :log
           (fn [tx-report]
             (let [tx-id  (get-in tx-report [:tempids :db/current-tx])
                   datoms (:tx-data tx-report)
                   datom->str (fn [d] (str (if (:added d) "+" "−")
                                           "[" (:e d) " " (:a d) " " (pr-str (:v d)) "]"))]
               (println
                 (str/join "\n" (concat [(str "tx " tx-id ":")] (map datom->str datoms)))))))

;; history

#_(d/listen! conn :history
           (fn [tx-report]
             (let [{:keys [db-before db-after]} tx-report]
               (when (and db-before db-after)
                 (swap! history (fn [h]
                                  (-> h
                                      (u/drop-tail #(identical? % db-before))
                                      (conj db-after)
                                      (u/trim-head history-limit))))))))

;; transit serialization

(defn db->string [db]
  (profile "db serialization"
           (dt/write-transit-str db)))

(defn string->db [s]
  (profile "db deserialization"
           (dt/read-transit-str s)))

;; persisting DB between page reloads
(defn persist [db]
  (js/localStorage.setItem "datascript-todo/DB" (db->string db)))

(d/listen! conn :persistence
           (fn [tx-report] ;; FIXME do not notify with nil as db-report
             ;; FIXME do not notify if tx-data is empty
             (when-let [db (:db-after tx-report)]
               (js/setTimeout #(persist db) 0))))

#_(js/localStorage.clear)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


















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


