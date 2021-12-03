(ns rss.backend.parser.parser-utils
  (:require [clj-http.client :as client]
            [clojure.data.xml :as dxml]
            [com.rpl.specter :as sp]
            [rss.domain.rss :as rss-domain]))

(def http-get-memoized (memoize #(client/get %1)))

(defn xml-specter-path
  [start & path]
  (apply
    sp/comp-paths
    (concat [(sp/selected? :tag #(= % start))]
            (mapcat (fn [sub-path]
                      [:content sp/ALL
                       (sp/selected? :tag #(= % sub-path))])
                    path)
            [:content])))

(defn xml-string->xml-data
  [xml-string]
  (-> xml-string
      :body
      (clojure.string/replace #"\t" "")
      (clojure.string/replace #"\n" "")
      (clojure.string/replace #"xmlns=\"http://www.w3.org/2005/Atom\"" "") ;; remove xml namespace
      dxml/parse-str))
