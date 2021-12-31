(ns user
  (:require [mount.core]))

(defn go []
  (mount.core/start))

(comment
  (mount.core/start)
  (mount.core/stop)
  )