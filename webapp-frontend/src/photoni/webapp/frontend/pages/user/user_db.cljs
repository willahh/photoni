(ns photoni.webapp.frontend.pages.user.user-db
  (:require [re-frame.core :refer [reg-sub]]))

(def user-db
  {:list   {:loading? false
            :users    {:rows  []
                       :total 0
                       :count 0}}
   :upsert {:status   :form.status/default
            :errors   {}
            :user-row {}}})

(def path-users [:list :users])

(def path-users-rows [:list :users :rows])
(reg-sub :user.sub/users-rows (fn [db _] (get-in db path-users-rows)))

(def path-users-loading? [:list :loading?])
(reg-sub :user.sub/users-loading (fn [db _] (get-in db path-users-loading?)))

(def path-upsert-user-row [:upsert :user-row])
(reg-sub :user.sub/upsert-user-row (fn [db _] (get-in db path-upsert-user-row)))

(def path-upsert-status [:upsert :status])
(reg-sub :user.sub/users-upsert-status (fn [db _] (get-in db path-upsert-status)))

(def path-upsert-errors [:upsert :errors])
(reg-sub :user.sub/users-upsert-errors (fn [db _] (get-in db path-upsert-errors)))