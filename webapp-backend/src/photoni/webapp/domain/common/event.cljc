(ns photoni.webapp.domain.common.event)

(defn ->event [{:keys [name entity]}]
  #:event{:name       name
          :created-at (java.util.Date.)
          :entity     entity})