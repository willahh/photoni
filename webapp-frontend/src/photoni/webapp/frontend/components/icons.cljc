(ns photoni.webapp.frontend.components.icons)

(defn icon-arrow-up
  ([w h]
   [:svg {:xmlns "http://www.w3.org/2000/svg" :className (str "h-" h " " "w-" w) :fill "none" :viewBox "0 0 24 24" :stroke "currentColor"}
    [:path {:strokeLinecap "round" :strokeLinejoin "round" :strokeWidth "{2}" :d "M19 14l-7 7m0 0l-7-7m7 7V3"}]])
  ([]
   [icon-arrow-up 6 6]))
