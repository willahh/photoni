(ns rss.domain.rss-test
  (:require [clojure.test :refer :all])
  (:require [rss.domain.rss :refer :all]))



(s/gen :rss.author/name)