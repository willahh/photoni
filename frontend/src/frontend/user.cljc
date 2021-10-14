(ns frontend.user
  (:require [frontend.db :as db]
            [re-frame.core :as re-frame :refer [reg-event-fx reg-event-db reg-sub]]))


;; re-frame events and db handlers
(reg-sub
  :users  ;; usage: (subscribe [:articles])
  (fn [db _]
    (prn "reg-sub users:" (:users db))
    (:users db)))

(reg-event-fx
  :add-user
  (fn [{:keys [db]} [_ user]]
    (prn "event fx user - : " user)
    (try
      (do (db/add-user user)
          {:dispatch [:add-user-success user]})
      (catch js/Error e
        {:dispatch [:add-user-fail user]}))))

(reg-event-db
  :add-user-success
  (fn [db [_ user]]
    (prn "reg event db :add-user-success user - : " user)
    (assoc db :users (conj (:users db) user))
    ))

(reg-event-db
  :add-user-failure
  (fn [db [_ user]]
    (prn "add user failure")))















;; restoring once persisted DB on page load
(when-let [stored (js/localStorage.getItem "datascript-todo/DB")]
  (let [stored-db (frontend.db/string->db stored)]
    (prn "stored-db: "stored-db)
    (when (= (:schema stored-db) frontend.db/schema)                    ;; check for code update
      (frontend.db/reset-conn! stored-db)
      #_(swap! history conj @conn)
      true)))



(comment
  (assoc db :users (conj (:users db) {:id 1}))
  (do {:schema {:user/id {:db.unique :db.unique/identity}, :user/name {}, :user/age {}, :user/parent {:db.valueType :db.type/ref, :db.cardinality :db.cardinality/many}}, :datoms [[1 :user/age 216 536870913] [1 :user/email "scarlet@icloud.com" 536870913] [1 :user/id 108866 536870913] [1 :user/name "passivechase" 536870913] [1 :user/role "8an0aUi1x56K5fg3HinAJDR4Dzi" 536870913] [1 :user/title "Executive Assistant" 536870913] [2 :user/age 5 536870914] [2 :user/email "konit@aol.com" 536870914] [2 :user/id 681271 536870914] [2 :user/name "lingerielength" 536870914] [2 :user/role "51kHQ8939YMPj9x63K8cCp2d" 536870914] [2 :user/title "Nurse Practitioner" 536870914] [3 :user/age 4033682 536870915] [3 :user/email "dimensio@me.com" 536870915] [3 :user/id 8 536870915] [3 :user/name "suspendsuccinct" 536870915] [3 :user/role "M" 536870915] [3 :user/title "Customer Service Representative" 536870915] [4 :user/age 7596269 536870916] [4 :user/email "fwiles@me.com" 536870916] [4 :user/id 3 536870916] [4 :user/name "befittingforearm" 536870916] [4 :user/role "y" 536870916] [4 :user/title "Marketing Manager" 536870916] [5 :user/age 19 536870917] [5 :user/email "scarlet@icloud.com" 536870917] [5 :user/id 5138987 536870917] [5 :user/name "electionbreast" 536870917] [5 :user/role "c64p8tfYyToK336LKgN06jiE" 536870917] [5 :user/title "Sales Manager" 536870917] [6 :user/age 4397161 536870918] [6 :user/email "nelson@yahoo.ca" 536870918] [6 :user/id 1106 536870918] [6 :user/name "armpitregretful" 536870918] [6 :user/role "A8V56QwoHbo" 536870918] [6 :user/title "Executive Assistant" 536870918] [7 :user/age 852177 536870919] [7 :user/email "sblack@verizon.net" 536870919] [7 :user/id 21234 536870919] [7 :user/name "mechanickey" 536870919] [7 :user/role "N5rdeQVICFzhJLt5DZHUH0e9BFH31" 536870919] [7 :user/title "Executive Assistant" 536870919] [8 :user/age 950 536870920] [8 :user/email "horrocks@aol.com" 536870920] [8 :user/id 2 536870920] [8 :user/name "piglinbullfinche" 536870920] [8 :user/role "n9VQY0kCRf6UiUP5jCBz0m" 536870920] [8 :user/title "Data Entry Clerk" 536870920] [9 :user/age 3582321 536870921] [9 :user/email "uqmcolyv@comcast.net" 536870921] [9 :user/id 1685 536870921] [9 :user/name "capablebriefs" 536870921] [9 :user/role "Ed208Bz763C0r" 536870921] [9 :user/title "Customer Service Representative" 536870921]]}))