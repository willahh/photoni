(ns dev
  (:require [mount.core :as mount]
            [clojure.tools.namespace.repl :as tn]))

(defn go
  "starts all states defined by defstate"
  []
  (mount/start)
  :ready)

(defn stop []
  (mount/stop))

(defn refresh []
  (stop)
  (tn/refresh))

(defn refresh-all []
  (stop)
  (tn/refresh-all))

(defn reset
  "stops all states defined by defstate, reloads modified source files, and restarts the states"
  []
  (stop)
  (tn/refresh :after 'dev/go))
