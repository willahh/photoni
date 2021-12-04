(ns photoni.tailwindparser.tailwindparser
  (:require [clojure.java.io :as io]
            [clojure.walk :as walk])
  (:use [clojure.test]))

(def css (slurp "resources/tailwind.css"))
(def rows (clojure.string/split css #"\n"))

(defn generate-file []
  (let [file-content (->> rows
                          #_(drop 999)
                          #_(take 100)
                          (keep #(second (re-find #"\.(.*) \{" %)))
                          (keep #(clojure.string/replace %1 #"\\" ""))
                          (keep #(clojure.string/replace %1 #"/" "-")) ; Replace '/' contained in some css class name which are not a valid Clojure symbol
                          (keep #(clojure.string/replace %1 #" > :.*" "")) ; remove > :
                          (keep #(clojure.string/replace %1 #"::placeholder$" "")) ;; remove ::placeholder
                          (filter #(nil? (re-find #"^.* .*$" %))) ; Filter two css rules separated with a space
                          (distinct)
                          (map (fn [v]
                                 `(def ~(symbol (-> v
                                                    (clojure.string/replace #"/" "-")
                                                    (clojure.string/replace #"\." "dot")
                                                    (clojure.string/replace #"^(\d+)" "size-$1")
                                                    (clojure.string/replace #"^filter" "filter-"))
                                                ) ~(keyword v))))

                          (clojure.string/join "\n")
                          (str "(ns frontend.utils.tailwind-styles)\n\n"))]
    (spit "resources/tailwind_styles.cljc" file-content)))

(defn recursive-hiccup-w-class
  [hiccup]
  (def hiccup hiccup)
  (walk/walk (fn [v]
               (prn "v" v (type v))
               (cond
                 (= (type v) clojure.lang.Keyword)
                 (hiccup->hiccup-with-class [v])

                 (= (type v) clojure.lang.PersistentVector)
                 (hiccup->hiccup-with-class v)
                 (= (type v) clojure.lang.PersistentArrayMap)
                 (hiccup->hiccup-with-class v)
                 )
               )
             identity
              hiccup))

(comment
  (generate-file)
  (walk/walk #(* 2 %) #(apply + %) [1 2 3 4 5])
  (walk/walk #(* 2 %) identity [1 2 3 4 5])
  (walk/walk (fn [v]
               (prn "v" v)
               (hiccup->hiccup-with-class [v])) identity [:div.flex-1.min-w-0])



  (def hiccup [:div.flex-1.min-w-0
               [:div "Level 2-1"]
               [:div "Level 2-2"]
               [:div "Level 2-3"
                [:div.classa.classb "Level 3-1 with class and text"]
                [:div "Level 3-1"]]
               "Level 1"])
  (def hiccup [:div
               [:div "Level 2-1"]
               [:div "Level 2-2"]])


  (recursive-hiccup-w-class hiccup)
  (hiccup->hiccup-with-class hiccup)

  )



(defn hiccup->hiccup-with-class
  "Transforme une structure hiccup (html) avec des class en inline vers une structure
  avec des classes contenues dans un vecteur préfixées par styles/"
  [hiccup]
  (prn "hiccup->hiccup-with-class" hiccup)
  (let [tag-name (-> hiccup first str
                     (clojure.string/split #"\.")
                     first symbol)
        hiccup-rest (rest hiccup)
        class-list (-> hiccup first str
                       (clojure.string/split #"\."))
        classes (rest class-list)]
    (into []
          (concat (cond-> [tag-name]
                          (seq classes) (conj {:class (mapv #(symbol (str "styles/" %)) classes)}))
                  (when (seq hiccup-rest)
                    (hiccup->hiccup-with-class hiccup-rest))))))

(hiccup->hiccup-with-class [:div.class-a {:scope "col"} "Text"
                            [:div.item
                             [:a.ml-5 ]]])

(hiccup->hiccup-with-class [:div.class-a {:scope "col"} "Text"
                           [:div.item
                            [:a.ml-5 {:href "url"}]]])


(deftest test-some-rules
  (testing "Test some rules"
    (is (= '((def size-32xl:backdrop-blur :32xl:backdrop-blur)
             (def size-32xl:backdrop-blur-test :32xl:backdrop-blur.test)
             (def filter- :filter)
             (def backdrop-filter :backdrop-filter))
           (distinct (map (fn [v]
                            `(def ~(symbol (-> v
                                               (clojure.string/replace #"/" "--")
                                               (clojure.string/replace #"\." "-")
                                               (clojure.string/replace #"^(\d+)" "size-$1")
                                               (clojure.string/replace #"^filter" "filter-"))
                                           ) ~(keyword v)))
                          [
                           "32xl:backdrop-blur"
                           "32xl:backdrop-blur.test"
                           "filter"
                           "backdrop-filter"]))))))

(deftest hiccup->hiccup-with-class-recursive-test
  (testing "One div without attributes and childrens"
    (is (= '[:div]
           (recursive-hiccup-w-class [:div]))))
  (testing "One div without attributes with one children"
    (is (= '[:div [:div]]
           (recursive-hiccup-w-class [:div
                                      [:div]]))))
  (testing "One div without attributes with nested childrens"
    (is (= '[:div [:div [:div] [:div] [:div]]]
           (recursive-hiccup-w-class [:div
                                      [:div
                                       [:div]
                                       [:div]
                                       [:div]]]))))
  (testing "One div with class"
    (is (= '[:div {:class [styles/class-a]}]
           (recursive-hiccup-w-class [:div.class-a]))))
  (testing "One div with class and attributes"
    (is (= '[:div {:class [styles/class-a]} {:scope "col"}]
           (recursive-hiccup-w-class [:div.class-a {:scope "col"}]))))
  (testing "One div + class + attributes + text node"
    (is (= '[:div {:class [styles/class-a]} {:scope "col"} "Text"]
           (recursive-hiccup-w-class [:div.class-a {:scope "col"} "Text"]))))
  (testing "Nested div + class + attributes + text node"
    (is (= '[:div {:class [styles/class-a]} {:scope "col"} "Text"
             [:div {:class [styles/item]}
              [:a {:class [styles/ml-5]} {:href "url"}]]]
           (recursive-hiccup-w-class [:div.class-a {:scope "col"} "Text"
                                      [:div.item
                                       [:a.ml-5 {:href "url"}]]]))))
  )


(deftest hiccup->hiccup-with-class-test
  (testing
    "Simple hiccup div without attributes"
    (is (= '[:div]
           (hiccup->hiccup-with-class [:div]))))
  (testing
    "Simple hiccup div with 2 class"
    (is (= '[:div {:class [styles/flex-1 styles/min-w-0]}]
           (hiccup->hiccup-with-class [:div.flex-1.min-w-0]))))
  (testing
    "Hiccup with inline class + attributes (scope) + text"
    (is (= '[:th {:class [styles/px-6 styles/py-3 styles/text-left styles/text-xs]} {:scope "col"} "Name"]
           (hiccup->hiccup-with-class [:th.px-6.py-3.text-left.text-xs {:scope "col"} "Name"])))))





(deftest todo-name-test
  (testing "Transform a hiccup data structure with classes to a new one with classes with styles/"
    (is (= '(:div.md:flex.md:items-center.md:justify-between
              [:div.flex-1.min-w-0
               [:h2.text-2xl.font-bold.leading-7.text-gray-900.sm:text-3xl.sm:truncate "Back End Developer"]]
              [:div.mt-4.flex.md:mt-0.md:ml-4
               [:button.inline-flex.items-center.px-4.py-2.border.border-gray-300.rounded-md.shadow-sm.text-sm.font-medium.text-gray-700.bg-white.hover:bg-gray-50.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500 {:type "button"} "Edit"]
               [:button.ml-3.inline-flex.items-center.px-4.py-2.border.border-transparent.rounded-md.shadow-sm.text-sm.font-medium.text-white.bg-indigo-600.hover:bg-indigo-700.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500 {:type "button"} "Publish"]])
           [:div {:class [:md:flex :md:items-center :md:justify-between]}
            [:div.flex-1.min-w-0
             [:h2.text-2xl.font-bold.leading-7.text-gray-900.sm:text-3xl.sm:truncate "Back End Developer"]]
            [:div.mt-4.flex.md:mt-0.md:ml-4
             [:button.inline-flex.items-center.px-4.py-2.border.border-gray-300.rounded-md.shadow-sm.text-sm.font-medium.text-gray-700.bg-white.hover:bg-gray-50.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500 {:type "button"} "Edit"]
             [:button.ml-3.inline-flex.items-center.px-4.py-2.border.border-transparent.rounded-md.shadow-sm.text-sm.font-medium.text-white.bg-indigo-600.hover:bg-indigo-700.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500 {:type "button"} "Publish"]]]))))

