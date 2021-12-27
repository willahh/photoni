(ns photoni.webapp.api.external.server
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
    ;       [reitit.ring.middleware.dev :as dev]
    ;       [reitit.ring.spec :as spec]
    ;       [spec-tools.spell :as spell]
            [ring.adapter.jetty :as jetty]
            [muuntaja.core :as m]
            [clojure.java.io :as io]
            [malli.util :as mu]

            [mount.core :refer [defstate]]
            [ring.middleware.reload :refer [wrap-reload]]
            [photoni.webapp.api.external.routes.user :as route-user])
  (:import [org.eclipse.jetty.server Server]))

(def app
  (ring/ring-handler
    (ring/router
      [["/swagger.json"
        {:get {:no-doc  true
               :swagger {:info {:title       "my-api"
                                :description "with [malli](https://github.com/metosin/malli) and reitit-ring"}}
               :handler (swagger/create-swagger-handler)}}]
       route-user/routes]

      {;;:reitit.middleware/transform dev/print-request-diffs ;; pretty diffs
       ;;:validate spec/validate ;; enable spec validation for route data
       ;;:reitit.spec/wrap spell/closed ;; strict top-level validation
       :exception pretty/exception
       :data      {:coercion   (reitit.coercion.malli/create
                                 {

                                  ;:transformers     {:body     {:default default-transformer-provider
                                  ;                              :formats {"application/json" json-transformer-provider}}
                                  ;                   :string   {:default string-transformer-provider}
                                  ;                   :response {:default default-transformer-provider
                                  ;                              :formats {"application/json" json-transformer-provider}}}


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
                   :middleware [;; swagger feature
                                swagger/swagger-feature
                                ;; query-params & form-params
                                parameters/parameters-middleware
                                ;; content-negotiation
                                muuntaja/format-negotiate-middleware
                                ;; encoding response body
                                muuntaja/format-response-middleware
                                ;; exception handling
                                exception/exception-middleware
                                ;; decoding request body
                                muuntaja/format-request-middleware
                                ;; coercing response bodys
                                coercion/coerce-response-middleware
                                ;; coercing request parameters
                                coercion/coerce-request-middleware
                                ;; multipart
                                multipart/multipart-middleware]}})
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