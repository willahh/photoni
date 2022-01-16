(ns photoni.webapp.frontend.pages.group.group-repository-frontend
  (:require [cljs-http.client :as http]
            [ajax.core :refer [GET POST]]
            [cljs.core.async :refer [<!]]
            [mount.core :refer [defstate]]
            [photoni.webapp.domain.group.group-repository :as group-repository])
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [photoni.webapp.frontend.utils.async :refer [async await]]))

(defrecord groupRepositoryFrontend []
  group-repository/groupRepository
  (find-groups-by [_ query-fields]
    (prn "find-groups-by" query-fields)
    )
  (get-groups [_]
    (http/get "http://localhost:3000/api/groups"
              {:with-credentials? false}))
  (create-group [_ group-fields]
    (http/post "http://localhost:3000/api/groups"
               {:with-credentials? false
                :transit-params group-fields}))
  (get-group-by-group-id [_ group-id]
    (http/get (str "http://localhost:3000/api/groups/" group-id)
              {:with-credentials? false}))
  (delete-group-by-group-id [group-repo group-id]
    (http/delete (str "http://localhost:3000/api/groups/" group-id)
                 {:with-credentials? false})))

(defstate group-repository-js
  :start (->groupRepositoryFrontend))
