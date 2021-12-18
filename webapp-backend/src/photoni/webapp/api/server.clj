(ns photoni.webapp.api.server
  (:require [reitit.ring :as ring]
            [reitit.coercion.spec]

            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.ring.coercion :as coercion]
            [reitit.dev.pretty :as pretty]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.ring.middleware.parameters :as parameters]
    ;; Uncomment to use
    ; [reitit.ring.middleware.dev :as dev]
    ; [reitit.ring.spec :as spec]
    ; [spec-tools.spell :as spell]
            [spec-tools.core :as st]
            [ring.adapter.jetty :as jetty]
            [muuntaja.core :as m]
            [clojure.spec.alpha :as s]
            [clojure.java.io :as io]



    ;;[compojure.core :refer :all]
    ;;      [compojure.route :as route]
            [expound.alpha :as expound]
            [ring.middleware.reload :refer [wrap-reload]]
            [reitit.core :as r]
            [mount.core :refer [defstate]]
            [photoni.webapp.api.routes.users-routes :as users-routes]

            )
  (:import (org.eclipse.jetty.server Server)))







(defn routes []
  [["/swagger.json"
    {:get {:no-doc  true
           :swagger {:info {:title "my-api"}}
           :handler (swagger/create-swagger-handler)}}]
   users-routes/routes-aggregate])

(def opts {;;:reitit.middleware/transform dev/print-request-diffs ;; pretty diffs
           ;;:validate spec/validate ;; enable spec validation for route data
           ;;:reitit.spec/wrap spell/closed ;; strict top-level validation
           :conflicts nil
           :exception pretty/exception
           :syntax    :bracket
           :data      {:coercion   reitit.coercion.spec/coercion
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

(def default-handler (ring/routes
                       (swagger-ui/create-swagger-ui-handler
                         {:path   "/"
                          :config {:validatorUrl     nil
                                   :operationsSorter "alpha"}})
                       (ring/create-default-handler)))
(def app
  (ring/ring-handler
    (ring/router (routes) opts)
    default-handler))

(defn start
  [dev-mode?]
  (let [sv (if dev-mode?
             (jetty/run-jetty (wrap-reload #'app) {:port 3000, :join? false})
             (jetty/run-jetty #'app {:port 3000, :join? false}))]
    (println "server running in port 3000")
    sv))

(defn stop
  [^Server server]
  (.stop server)
  (println "Server stopped"))


;; Do not stop the server in stop, the wrap-reload middleware
;; force reload and do conflicts
(defstate api-server
  :start (start true)
  ;;  :stop (stop api-server)
  )

#_(defstate api-server-production
    :start (start true)
    :stop (stop api-server))

(comment
  (mount.core/start)

  )