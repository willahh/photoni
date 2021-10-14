(ns tailwindparser-test
  (:require [clojure.test :refer :all])
  )


(comment

  (reduce
    ((comp
       (filter even?)
       (map inc)) conj)                  ;; <- reducing fn (awesome conj)
    []                                                      ;; <- initial value
    [1 2 3 4 5])

  (transduce
    (comp
      (filter even?)
      (map inc))
    conj
    []
    [1 2 3 4 5])

  (->> [1 2 3 4 5]
       (filter even?)
       (map inc))
  )


