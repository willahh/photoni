(ns rss.domain.rss
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]))


(s/def :rss.entry/title string?)
(s/def :rss.entry/id string?)
(s/def :rss.entry/link string?)
(s/def :rss.entry/updated-date string?)


(s/def :rss.author/name string?)
(s/def :rss.author/uri string?)
(s/def :rss.author/author (s/keys :req [:rss.author/name]
                                  :opt [:rss.author/uri]))
(s/def :rss.entry/authors (s/coll-of :rss.author/author))


(s/def ::entry (s/keys :req [:rss.entry/title
                             :rss.entry/id
                             :rss.entry/link
                             :rss.entry/updated-date
                             :rss.entry/authors]))

(comment
  (gen/generate (s/gen ::author))
  (gen/generate (s/gen ::entry))

  )
