(ns rss.backend.rss-test
  (:require [clojure.test :refer :all])
  (:require [rss.backend.rss :refer :all]))


(deftest http-get-memoized-atom-xml
  (testing ""
    (let [result (http-get-memoized "http://planet.clojure.in/atom.xml")]
      (-> result
          :body
          xml-string->hiccup))))




(comment
  (def site-awwwards-result (http-get-memoized "https://www.awwwards.com/feed/"))

  (xml-string->hiccup (:body site-awwwards-result)))
