(ns photoni.webapp.frontend.utils.async
  "Async and await macros.
  Source: https://www.reddit.com/r/Clojure/comments/clqcm7/comment/ew6jflk/?utm_source=share&utm_medium=web2x&context=3"
  (:refer-clojure :exclude [doseq await]))

(defmacro ->chan
  [promise]
  `(let [ch# (cljs.core.async/chan)]
     (.then ~promise
            #(cljs.core.async.macros/go (cljs.core.async/>! ch# %)))
     ch#))

(defmacro async
  [& body]
  `(~'js/Promise.
     (fn [resolve#]
       (cljs.core.async.macros/go
         (resolve# (let []
                     ~@body))))))

(defmacro await
  [promise]
  `(cljs.core.async/<! (->chan ~promise)))