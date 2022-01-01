(ns photoni.webapp.backend.api.api-utils)

(defn ns->routes
  "Given a namespace symbol, find routes configuration containing meta
   #^{:route/method :get
      :route/path   \"/api/route\"}
   then convert to a Reitit Route format."
  [ns-symbol]
  (->> (keys (ns-interns ns-symbol))
       (reduce (fn [acc s]
                 (let [x @(resolve s)
                       m (meta x)
                       {:route/keys [method path]} m]
                   (if (and method path)
                     (conj acc {:method  method
                                :path    path
                                :handler x})
                     acc)))
               []
               )
       (group-by :path)
       (partition 2)
       first
       (mapv (fn [[path x]]
               [path (reduce (fn [acc {:keys [method path handler]}]
                               (assoc acc method handler))
                             {}
                             x)]))))

(defn set-spec-field-optional
  [spec-field]
  (update-in spec-field [1] #(assoc % :optional true)))
