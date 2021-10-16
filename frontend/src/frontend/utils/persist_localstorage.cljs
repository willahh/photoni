(ns frontend.utils.persist-localstorage
  (:require [clojure.string :as str]
            [re-frame.core :as re-frame]
            [datascript.core :as d]
            [frontend.db.db :as db]
            [datascript.transit :as dt])
  (:require-macros [frontend.utils.macro :refer [profile]]))



;;;;;;;;;;;;;; localstorage
(declare render persist)
(defn reset-conn! [db]
  (prn "reset-conn!" )

  (reset! db/conn db)
  (prn "db/conn: " db/conn)
  #_(render db)





  (persist db))




(defonce history (atom []))
(def ^:const history-limit 10)

;; logging of all transactions (prettified)
(d/listen! db/conn :log
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
  (prn "##persist")
  (js/localStorage.setItem "datascript-todo/DB" (db->string db)))

(d/listen! db/conn :persistence
           (fn [tx-report] ;; FIXME do not notify with nil as db-report
             ;; FIXME do not notify if tx-data is empty
             (prn "## d/listen! persistence")
             (when-let [db (:db-after tx-report)]
               (js/setTimeout #(persist db) 0))))

#_(js/localStorage.clear)




;; restoring once persisted DB on page load
(when-let [stored (js/localStorage.getItem "datascript-todo/DB")]
  (let [stored-db (string->db stored)]
    (prn "------------stored-db: "stored-db)
    (when (= (:schema stored-db) db/schema)                    ;; check for code update
      (reset-conn! stored-db)
      #_(swap! history conj @conn)
      true)

    (re-frame.core/dispatch [:initialise-db-with-dt stored-db])


    ))






;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


