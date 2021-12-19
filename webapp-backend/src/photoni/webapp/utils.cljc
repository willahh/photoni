(ns photoni.webapp.utils)

(defn qualified-map->underscore-map
  "Transform a nested map with qualified keywords into a nested map with underscores.
#:person{:name \"Kevin\",
       :age 99,
       :gender :gender/M,
       :address #:address{:state :state/NY, :city \"New York\", :zip \"99999\"}}
=>
{:person_name \"Kevin\",
 :person_age 99,
 :person_gender :gender_M,
 :person_address {:address_state :state_NY, :address_city \"New York\", :address_zip \"99999\"}}"
  [m]
  (clojure.walk/postwalk
    (fn [x]
      (if (keyword? x)
        (do
          (-> x
              str
              (clojure.string/replace #":" "")
              (clojure.string/replace #"/" "_")
              (clojure.string/replace #"-" "_")
              keyword))
        x))
    m))