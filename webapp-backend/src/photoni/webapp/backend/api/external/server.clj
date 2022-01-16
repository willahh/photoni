(ns photoni.webapp.backend.api.external.server
  (:require [reitit.ring :as ring]
            [reitit.coercion.malli]
            [reitit.ring.malli]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.ring.coercion :as coercion]
            [reitit.dev.pretty :as pretty]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.ring.middleware.parameters :as parameters]
            [ring.adapter.jetty :as jetty]
            [muuntaja.core :as m]
            [malli.util :as mu]

            [mount.core :refer [defstate]]
            [ring.middleware.reload :refer [wrap-reload]]
            [expound.alpha :as expound]
    ;[ring.middleware.defaults :refer [wrap-defaults site-defaults]]
    ;[ring.middleware.json :refer [wrap-json-response wrap-json-body]]
    ;[ring.middleware.cors :refer [wrap-cors]]




            [photoni.webapp.domain.common.log :as log]
            [photoni.webapp.backend.api.external.routes.user :as route-user]
            [photoni.webapp.backend.api.external.routes.group :as route-group]
            [photoni.webapp.backend.api.api-utils :as api-utils])
  (:import [org.eclipse.jetty.server Server]))

(def cors-middleware
  "Wrap the server response in a Control-Allow-Origin Header to
  allow connections from the web app."
  {:name
   ::wrap-cors
   :description
   "Middleware for CORS"
   :wrap
   (fn [handler]
     (fn [request]
       (prn "request3!")
       (def request request)
       (let [response (handler request)]
         (-> response
             (assoc-in [:headers "Access-Control-Allow-Origin"] "http://localhost:8280")
             ;;(assoc-in [:headers "Access-Control-Allow-Origin"] "*")
             (assoc-in [:headers "Access-Control-Allow-Methods"] "POST, GET, OPTIONS, DELETE")
             (assoc-in [:headers "Access-Control-Allow-Headers"] "Content-Type, Accept")
             (assoc-in [:headers "Access-Control-Allow-Credentials"] "true")


             ;;(assoc-in [:headers "Access-Control-Allow-Origin"] "http://localhost:8280") ;; TODO: Variablize
             ;;(assoc-in [:headers "Access-Control-Allow-Headers"] "*")
             ;;(assoc-in [:headers "Sec-Fetch-Site"] "*")
             ;;(assoc-in [:headers "Sec-Fetch-Mode"] "*")
             ))))})



(defn coercion-error-handler [status]
  (let [printer (expound/custom-printer {:theme :figwheel-theme, :print-specs? false})
        handler (exception/create-coercion-handler status)]
    (fn [exception request]
      (printer (-> exception ex-data :problems))
      (handler exception request))))

(def exception-middleware4
  (exception/create-exception-middleware
    (merge
      exception/default-handlers
      {::error             (coercion-error-handler 500)
       ::exception         (coercion-error-handler 500)
       ::exception/default (coercion-error-handler 500)
       ::exception/wrap    (fn exception-wrap
                             [handler e request]
                             {:status 500
                              :body   {:message "error-name"
                                       :data    (.getMessage e)
                                       :uri     (:uri request)}})
       ;:reitit.coercion/request-coercion  (coercion-error-handler 400)
       ;:reitit.coercion/response-coercion (coercion-error-handler 500)
       })))







(defn- hidden-method
  [request]
  (some-> (or (get-in request [:form-params "_method"])     ;; look for "_method" field in :form-params
              (get-in request [:multipart-params "_method"])) ;; or in :multipart-params
          clojure.string/lower-case
          keyword))

(def wrap-hidden-method
  {:name ::wrap-hidden-method
   :wrap (fn [handler]
           (fn [request]
             (if-let [fm (and (= :post (:request-method request)) ;; if this is a :post request
                              (hidden-method request))]     ;; and there is a "_method" field
               (handler (assoc request :request-method fm)) ;; replace :request-method
               (handler request))))})






(def app
  (ring/ring-handler
    (ring/router
      [["/swagger.json"
        {:get {:no-doc  true
               :swagger {:info {:title       "my-api"
                                :description "with [malli](https://github.com/metosin/malli) and reitit-ring"}}
               :handler (swagger/create-swagger-handler)}}]
       route-user/routes
       route-group/routes
       ]

      {;;:reitit.middleware/transform dev/print-request-diffs ;; pretty diffs
       ;;:validate spec/validate ;; enable spec validation for route data
       ;;:reitit.spec/wrap spell/closed ;; strict top-level validation
       :exception pretty/exception
       :data      {

                   :coercion   (reitit.coercion.malli/create
                                 {

                                  ;; set of keys to include in error messages
                                  :error-keys       #{#_:type :coercion :in :schema :value :errors :humanized #_:transformed}

                                  ;; schema identity function (default: close all map schemas)
                                  :compile          mu/closed-schema
                                  ;; strip-extra-keys (effects only predefined transformers)
                                  :strip-extra-keys true
                                  ;; add/set default values
                                  :default-values   true
                                  ;; malli options
                                  :options          nil})

                   :muuntaja   m/instance
                   :middleware [

                                ;; swagger feature
                                swagger/swagger-feature

                                ;; query-params & form-params
                                parameters/parameters-middleware

                                ;; content-negotiation
                                muuntaja/format-negotiate-middleware

                                ;; encoding response body
                                muuntaja/format-response-middleware

                                ;; decoding request body
                                muuntaja/format-request-middleware

                                ;; exception-middleware4 (order is important)
                                exception-middleware4



                                ;; coercing response bodys
                                coercion/coerce-response-middleware

                                ;; coercing request parameters
                                ;; => Important, used to parse the requested parameters
                                coercion/coerce-request-middleware

                                ;; multipart
                                multipart/multipart-middleware

                                ;; wrap-hidden-method
                                wrap-hidden-method



                                ;; exception handling
                                ;; exception/exception-middleware
                                ;;exception-middleware3
                                ;; exception-middleware
                                cors-middleware

                                ]}})
    (ring/routes
      (swagger-ui/create-swagger-ui-handler
        {:path   "/"
         :config {:validatorUrl     nil
                  :operationsSorter "alpha"}})
      (ring/create-default-handler))))

(defn start []
  (let [sv (if (let [dev-mode? true]
                 dev-mode?)
             (jetty/run-jetty (wrap-reload #'app) {:port 3000, :join? false})
             (jetty/run-jetty #'app {:port 3000, :join? false}))]
    (println "server running in port 3000 started")
    sv))

(defn stop [^Server server]
  (prn "stop" server)
  (.stop server)
  (println "external api server running in port 3000 stopped"))

(defstate external-api-server
  :start (start)
  ;;:stop (stop external-api-server) ;; defstate does not work well with wrap-reload
  )


(comment
  (mount.core/start)
  (mount.core/stop)

  )