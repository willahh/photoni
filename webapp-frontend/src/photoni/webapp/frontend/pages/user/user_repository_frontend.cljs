(ns photoni.webapp.frontend.pages.user.user-repository-frontend
  (:require [cljs.core.async]
            [mount.core :refer [defstate]]
            [photoni.webapp.domain.user.user-repository :as user-repository])
  (:require-macros [cljs.core.async.macros]
                   [photoni.webapp.frontend.utils.async :refer [async await]]))

(defrecord UserRepositoryFrontend []
  user-repository/UserRepository
  (find-users-by [_ query-fields]
    (prn "find-users-by" query-fields)
    )
  (get-users [_]
    (prn "get-users")
    (async
      (let [data (atom nil)
            a (await (js/fetch "http://localhost:3000/api/users"))
            b (await (.json a))]
        (reset! data (js->clj b :keywordize-keys true))
        @data)))
  (create-user [_ user-fields]
    )
  (get-user-by-user-id [_ user-id]
    )
  (delete-user-by-user-id [user-repo user-id]
    ))

(defstate user-repository-js
  :start (->UserRepositoryFrontend))









(comment
  (mount.core/start)









  (async
    (let [data (atom nil)
          a (await (js/fetch "http://localhost:3000/api/users"))
          b (await (.json a))]
      (reset! data (js->clj b :keywordize-keys true))
      @data))



  (do (def users-response2 (atom nil))
      (async
        (let [data (atom nil)
              a (await (js/fetch "http://localhost:3000/api/users"))
              b (await (.json a))
              r (js->clj b :keywordize-keys true)
              ]
          (reset! users-response2 r)))
      users-response2)


  (let [promise (async
                  (let [data (atom nil)
                        a (await (js/fetch "http://localhost:3000/api/users"))
                        b (await (.json a))
                        r (js->clj b :keywordize-keys true)
                        ]
                    (reset! data r)
                    (prn "===> prn r:" r)
                    @data))]
    (js/Promise.resolve promise))




  (do (def users-response3 (atom nil))
      (.then (js/fetch "http://localhost:3000/api/users")
             (fn [response]
               (let [json (.json response)]
                 (.then json (fn [data]
                               (reset! users-response3 (js->clj data))
                               )))))
      users-response3)




  (async
    (let [a (await (js/Promise.resolve 1))
          b (await (js/Promise.resolve 2))]
      (prn (+ a b))))


  (def response (atom nil))
  (async
    (let [a (await (js/fetch "http://localhost:3000/api/users"))
          b (await (.json a))
          r (js->clj b :keywordize-keys true)]
      (prn "lol")
      (reset! response r)
      ))

  @response

  )
;
;
;(def users-response (atom nil))
;(.then (js/fetch "http://localhost:3000/api/users")
;       (fn [response]
;         (let [json (.json response)]
;           (.then json (fn [data]
;                         (reset! users-response (js->clj data))
;                         (prn "=>:" (js->clj data))))
;           )))
;
;(comment
;  @users-response
;  )


