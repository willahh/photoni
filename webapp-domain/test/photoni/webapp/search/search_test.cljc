(ns photoni.webapp.domain.search.search-test
  (:require [clojure.test :refer [deftest is testing]]
            [photoni.webapp.domain.search.search :as search]))

(deftest search-clauses-json->search-clauses-test
  (is (= [:and [:or [:= :field "value"]]]
         (search/search-clauses-json->search-clauses ["and" ["or" ["=" "field" "value"]]])))
  (is (= [:and [:or [:= :field "value"]]]
         (search/search-clauses-json->search-clauses ["and" ["or" ["like" "field" "v"]]])))
  (is (= [:and [:or
                [:= :field1 "Value 1"]
                [:= :field2 10]]]
         (search/search-clauses-json->search-clauses ["and" ["or"
                                                             ["=" "field1" "Value 1"]
                                                             ["=" "field2" 10]]])))
  (is (= [:and [:and
                [:> :field1 1]
                [:< :field2 10]]]
         (search/search-clauses-json->search-clauses ["and" ["and"
                                                             [">" "field1" 1]
                                                             ["<" "field2" 10]]])))
  (is (= [:or
          [:and
           [:= :field1 "Value 1"]
           [:= :field2 10]]
          [:and
           [:= :field3 "Value 3"]]]
         (search/search-clauses-json->search-clauses ["or"
                                                      ["and"
                                                       ["=" "field1" "Value 1"]
                                                       ["=" "field2" 10]]
                                                      ["and"
                                                       ["=" "field3" "Value 3"]]]))))


