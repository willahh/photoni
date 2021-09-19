(ns tailwindparser
  (:require [clojure.java.io :as io])
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
                          (str "(ns frontend.tailwind-styles)\n\n"))]
    (spit "resources/tailwind_styles.cljc" file-content)))


(comment
  (generate-file)
  )



(defn hiccup->hiccup-with-class
  "Transforme une structure hiccup (html) avec des class en inline vers une structure
  avec des classes contenues dans un vecteur préfixées par styles/"
  [hiccup]
  (let [seq (-> hiccup first str
                (clojure.string/split #"\."))
        class-name (-> seq first symbol)]
    [class-name {:class (as-> hiccup x
                              seq
                              (rest x)
                              (mapv #(symbol (str "styles/" %)) x))}]))


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

(deftest hiccup->hiccup-with-class test
                                   (is (= '[:div {:class [styles/flex-1 styles/min-w-0]}]
                                          (hiccup->hiccup-with-class [:div.flex-1.min-w-0]))))

(deftest todo-name-test
  (testing "Transform a hiccup data structure with classes to a new one with classes with styles/"
  (is (= '(:div {:class [styles/md:flex styles/md:items-center styles/md:justify-between]}
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

