(ns rss.backend.parser.atom-test
  "Parse rss xml atom format into rss Domain model"
  (:require [clojure.test :refer :all]
            [rss.backend.parser.parser-utils :as parser-utils]
            [com.rpl.specter :as sp]))

(defn entry-xml-struct->rss-entry
  [entry-xml-struct]
  (let [title (sp/select-first [(parser-utils/xml-specter-path :entry :title) sp/FIRST] entry-xml-struct)
        id (sp/select-first [(parser-utils/xml-specter-path :entry :id) sp/FIRST] entry-xml-struct)
        link (sp/select-first [(parser-utils/xml-specter-path :entry) sp/ALL #(= :link (:tag %)) :attrs :href] entry-xml-struct)
        updated (sp/select-first [(parser-utils/xml-specter-path :entry :updated) sp/FIRST] entry-xml-struct)
        ]
    #:rss.entry{:title        title
                :id           id
                :link         link
                :updated-date updated
                :authors      [#:rss.author{:uri "YdB9Z9GZf693IzWPa", :name "n5zY1P953U4R"}]}))

(defn rss-atom-xml->rss-atom-domain
  [rss-atom-xml]
  (let [xml-struct (parser-utils/xml-string->xml-data rss-atom-xml)
        title (sp/select-first [(parser-utils/xml-specter-path :feed :title) sp/FIRST] xml-struct)
        link (sp/select-first [(parser-utils/xml-specter-path :feed) sp/ALL #(= :link (:tag %)) :attrs :href] xml-struct)
        updated-date (sp/select-first [(parser-utils/xml-specter-path :feed :updated) sp/FIRST] xml-struct)
        generator-uri (sp/select-first [(parser-utils/xml-specter-path :feed :generator) sp/FIRST] xml-struct)
        id (sp/select-first [(parser-utils/xml-specter-path :feed :id) sp/FIRST] xml-struct)
        entries (sp/select [#(= :feed (:tag %)) :content sp/ALL #(= :entry (:tag %))] xml-struct)]
    #:rss.feed{:title         title
               :link          link
               :id            id
               :updated-date  updated-date
               :generator-uri generator-uri
               :entries       (map entry-xml-struct->rss-entry entries)}))


(comment

  (let [xml-string (parser-utils/http-get-memoized "http://planet.clojure.in/atom.xml")]
    (rss-atom-xml->rss-atom-domain xml-string))

  )




(deftest http-get-memoized-atom-xml
  (testing ""
    (let [result (http-get-memoized "http://planet.clojure.in/atom.xml")]
      (-> result
          :body
          xml-string->hiccup))))
