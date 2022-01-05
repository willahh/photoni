(ns photoni.webapp.domain.common.command
  (:require [photoni.webapp.domain.common.validation :as validation]))

(defn ->command
  [command-type command-spec command-fields]
  {:post [(validation/valid-spec command-spec %)]}
  (do
    (def command-type command-type)
    (def command-spec command-spec)
    (def command-fields command-fields)
    )
  {:type   command-type
   :fields command-fields})
