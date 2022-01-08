(ns photoni.webapp.frontend.pages.user.user-db)

(def user-db
  {:list   {:loading?        false
            :users           {:rows     []
                              :total    0
                              :count    0
                              :loading? false}}
   :upsert {:status   :form.status/default
            :user-row {}}})

(def path-users [:list :users])
(def path-users-rows [:list :users :rows])
(def path-users-loading? [:list :users :loading?])

(def path-upsert-user-row [:upsert :user-row])
(def path-upsert-status [:upsert :status])