(ns photoni.webapp.utils-test
  (:require [clojure.test :refer [deftest is testing]])
  (:require [photoni.webapp.utils :refer [qualified-map->underscore-map]]))

(deftest qualified-map->underscore-map-test
  (is (= {:person_name    "Kevin",
          :person_age     99,
          :person_gender  :gender_M,
          :person_address {:address_state :state_NY, :address_city "New York", :address_zip "99999"}}
         (qualified-map->underscore-map {:person/name    "Kevin"
                                         :person/age     99
                                         :person/gender  :gender/M
                                         :person/address {:address/state :state/NY
                                                          :address/city  "New York"
                                                          :address/zip   "99999"}}))))
