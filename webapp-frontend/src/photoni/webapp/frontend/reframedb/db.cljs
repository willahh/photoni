(ns photoni.webapp.frontend.reframedb.db
  (:require [photoni.webapp.frontend.pages.user.user-db :refer [user-db]]))

(def default-db
  (merge {:active-panel 1}
         user-db))
