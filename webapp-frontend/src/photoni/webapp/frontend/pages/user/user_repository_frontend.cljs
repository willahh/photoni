(ns photoni.webapp.frontend.pages.user.user-repository-frontend
  (:require [cljs-http.client :as http]
            [ajax.core :refer [GET POST]]
            [cljs.core.async :refer [<!]]
            [mount.core :refer [defstate]]
            [photoni.webapp.domain.user.user-repository :as user-repository])
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [photoni.webapp.frontend.utils.async :refer [async await]]))

(defrecord UserRepositoryFrontend []
  user-repository/UserRepository
  (find-users-by [_ query-fields]
    (prn "find-users-by" query-fields)
    )
  (get-users [_]
    (http/get "http://localhost:3000/api/users"
              {:with-credentials? false}))
  (create-user [_ user-fields]
    (http/post "http://localhost:3000/api/users"
               {:with-credentials? false
                :transit-params user-fields}))
  (get-user-by-user-id [_ user-id]
    (http/get (str "http://localhost:3000/api/users/" user-id)
              {:with-credentials? false}))
  (delete-user-by-user-id [user-repo user-id]
    (http/delete (str "http://localhost:3000/api/users/" user-id)
                 {:with-credentials? false})))

(defstate user-repository-js
  :start (->UserRepositoryFrontend))
