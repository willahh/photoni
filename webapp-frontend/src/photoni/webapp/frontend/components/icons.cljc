(ns photoni.webapp.frontend.components.icons)

(defn icon-arrow-up
  ([w h]
   [:svg {:xmlns "http://www.w3.org/2000/svg" :className (str "h-" h " " "w-" w) :fill "none" :viewBox "0 0 24 24" :stroke "currentColor"}
    [:path {:strokeLinecap "round" :strokeLinejoin "round" :strokeWidth "{2}" :d "M19 14l-7 7m0 0l-7-7m7 7V3"}]])
  ([]
   [icon-arrow-up 6 6]))

(def icon-edit [:svg {:xmlns "http://www.w3.org/2000/svg" :className "h-6 w-6" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor"}
                [:path {:strokeLinecap "round" :strokeLinejoin "round" :strokeWidth "{2}" :d "M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z"}]])

(def icon-delete [:svg {:xmlns "http://www.w3.org/2000/svg" :className "h-6 w-6" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor"}
                  [:path {:strokeLinecap "round" :strokeLinejoin "round" :strokeWidth "{2}" :d "M12 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2M3 12l6.414 6.414a2 2 0 001.414.586H19a2 2 0 002-2V7a2 2 0 00-2-2h-8.172a2 2 0 00-1.414.586L3 12z"}]])
