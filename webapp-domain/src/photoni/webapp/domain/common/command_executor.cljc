(ns photoni.webapp.domain.common.command-executor
  (:require [photoni.webapp.domain.common.log :as log]
            [mount.core :refer [defstate]]
            [photoni.webapp.domain.common.state-WIP-TODO :as state]))

(defn register-command-handler
  [command-type command-handler]
  (swap! state/command-type->command-handler assoc command-type command-handler))

(defn execute
  [{:keys [type] :as command}]
  (if-let [command-handler (get @state/command-type->command-handler type)]
    (command-handler command)
    (log/error {:service ::execute} (str "Unknow command-handler for type : " type))))

