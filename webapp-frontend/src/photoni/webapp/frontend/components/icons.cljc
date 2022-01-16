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

(def icon-view-list [:svg {:xmlns "http://www.w3.org/2000/svg" :className "h-6 w-6" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor"}
                     [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M4 6h16M4 10h16M4 14h16M4 18h16"}]])

(def icon-view-grid [:svg {:xmlns "http://www.w3.org/2000/svg" :className "h-6 w-6" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor"}
                     [:path {:strokeLinecap "round" :strokeLinejoin "round" :strokeWidth "{2}" :d "M4 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2V6zM14 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zM14 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z"}]])

(def icon-add [:svg {:xmlns "http://www.w3.org/2000/svg" :className "h-6 w-6" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor"}
               [:path {:strokeLinecap "round" :strokeLinejoin "round" :strokeWidth "{2}" :d "M12 9v3m0 0v3m0-3h3m-3 0H9m12 0a9 9 0 11-18 0 9 9 0 0118 0z"}]])

(def icon-duplicate [:svg {:xmlns "http://www.w3.org/2000/svg" :className "h-6 w-6" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor"}
                     [:path {:strokeLinecap "round" :strokeLinejoin "round" :strokeWidth "{2}" :d "M8 7v8a2 2 0 002 2h6M8 7V5a2 2 0 012-2h4.586a1 1 0 01.707.293l4.414 4.414a1 1 0 01.293.707V15a2 2 0 01-2 2h-2M8 7H6a2 2 0 00-2 2v10a2 2 0 002 2h8a2 2 0 002-2v-2"}]])

(def icon-filter
  [:svg {:xmlns "http://www.w3.org/2000/svg" :className "h-6 w-6" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor"}
   [:path {:strokeLinecap "round" :strokeLinejoin "round" :strokeWidth "{2}" :d "M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z"}]])