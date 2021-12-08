(ns photoni.webapp.domain.common.event)

(defn ->event [name command entity]
  #:event{:name         name
          :created-at   (java.util.Date.)
          :from-command command
          :entity       entity})