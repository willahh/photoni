(ns photoni.webapp.domain.utils)

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

(defn kw->str
  "Converti un keyword qualifié ou non en string.
   - :role/admin => role/admin
   - :admin => admin"
  [kw]
  (let [key-ns (namespace kw)]
    (str (when key-ns (str key-ns "/"))
         (name kw))))

(defn filter-spec-fields
  [spec spec-fields-to-exclude]
  (reduce (fn [acc spec]
            (let [pred (if (vector? spec)
                         (not (spec-fields-to-exclude (first spec)))
                         true)]
              (if pred
                (conj acc spec)
                acc)))
          [] spec))