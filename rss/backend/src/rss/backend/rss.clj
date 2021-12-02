(ns rss.backend.rss
  (:require [clj-http.client :as client]
            [hickory.select :as s]
            [hickory.core :as hc]
            [rss.domain.rss :as rss-domain]))

(def http-get-memoized (memoize #(client/get %1)))

(defn xml-string->hiccup
  [xml-string]
  (let [result xml-string
        result-clean (-> result
                         (clojure.string/replace #"\t" "")
                         (clojure.string/replace #"\n" ""))]
    (hc/as-hiccup (hc/parse result-clean))))
