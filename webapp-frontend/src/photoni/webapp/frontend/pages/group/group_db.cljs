(ns photoni.webapp.frontend.pages.group.group-db
  (:require [re-frame.core :refer [reg-sub]]))

(def group-db
  {:list   {:loading? false
            :groups    {:rows  []
                       :total 0
                       :count 0}}
   :upsert {:status   :form.status/default
            :errors   {}
            :group-row {}}})

(def path-groups [:list :groups])

(def path-groups-rows [:list :groups :rows])
(reg-sub :group.sub/groups-rows (fn [db _] (get-in db path-groups-rows)))

(def path-groups-loading? [:list :loading?])
(reg-sub :group.sub/groups-loading (fn [db _] (get-in db path-groups-loading?)))

(def path-upsert-group-row [:upsert :group-row])
(reg-sub :group.sub/upsert-group-row (fn [db _] (get-in db path-upsert-group-row)))

(def path-upsert-status [:upsert :status])
(reg-sub :group.sub/groups-upsert-status (fn [db _] (get-in db path-upsert-status)))

(def path-upsert-errors [:upsert :errors])
(reg-sub :group.sub/groups-upsert-errors (fn [db _] (get-in db path-upsert-errors)))