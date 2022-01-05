(ns photoni.webapp.frontend.pages.user.user-repository-frontend
  (:require [cljs-http.client :as http]
            [ajax.core :refer [GET POST]]
            [cljs.core.async :refer [<!]]
            [mount.core :refer [defstate]]
            [photoni.webapp.domain.user.user-repository :as user-repository])
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [photoni.webapp.frontend.utils.async :refer [async await]]))

(defrecord UserRepositoryFrontend []
  user-repository/UserRepository
  (find-users-by [_ query-fields]
    (prn "find-users-by" query-fields)
    )
  (get-users [_]
    (http/get "http://localhost:3000/api/users"
              {:with-credentials? false}))
  (create-user [_ user-fields]
    (http/post "http://localhost:3000/api/users"
               {:transit-params user-fields}))
  (get-user-by-user-id [_ user-id]
    )
  (delete-user-by-user-id [user-repo user-id]
    (go (let [response (<! (http/delete (str "http://localhost:3000/api/users/" user-id)
                                        {:with-credentials? false}))]
          (:body response)))))

(defstate user-repository-js
  :start (->UserRepositoryFrontend))


(comment
  ;; TODO: Fix CORS issues ... !!!
  ;; This request works !
  (.then (.fetch js/window "http://localhost:3000/api/users"
                 (clj->js {:method  "POST"
                           :headers {"Content-Type" "application/json"}
                           :body    "{\"user/name\":\"New A\",\"user/title\":\"string\",\"user/email\":\"user@mail.com\",\"user/role\":\"role/admin\",\"user/age\":10}"}))
         (fn [x]
           (prn "response x")))

  ;; But the same with a bad parameter (name2) returns a CORS error
  ;; ... this should instead returns an error message from the server
  ;; This might be a server-side problem
  ;; ===> Error thrown on reitit server coercion
  ;; ===> No, the error don't pass the preflight request
  ;; We could very that, but i think this is a protocol security to not reveal server output
  ;; when a bad POST/PUT request is submit
  (.then (.fetch js/window "http://localhost:3000/api/users"
                 (clj->js {:method  "POST"
                           ;;:mode "no-cors"
                           :headers {"Content-Type" "application/json"}
                           :body    "{\"user/name2\":\"New A\",\"user/title\":\"string\",\"user/email\":\"user@mail.com\",\"user/role\":\"role/admin\",\"user/age\":10}"}))
         (fn [x]
           (prn "response x")))

  (go (let [response (<! (http/post "http://localhost:3000/api/users"
                                    {:with-credentials? false
                                     ;;:request-method    :jsonp
                                     ;:headers           {"Accept"                      "http://localhost:8280"
                                     ;                    "Access-Control-Allow-Origin" "http://localhost:8280"}
                                     :transit-params    #:user{:id      #uuid"8350d0df-da16-42f4-b4e9-370b260beedc",
                                                               :picture "TODO",
                                                               :name    "name",
                                                               :title   "title",
                                                               :age     2,
                                                               :email   "email@a.co",
                                                               :role    "role/admin"}}))
            body (:body response)]
        (reset! todo-response-test response)
        body))



  ;; This works (valid inputs)
  (POST "http://localhost:3000/api/users"
        {
         :response-format :json
         :headers         {"Content-Type" "application/json"}
         :params          #:user{:id      #uuid"8350d0df-da16-42f4-b4e9-370b260beedc",
                                 :picture "TODO",
                                 :name    "name",
                                 :title   "title",
                                 :age     2,
                                 :email   "email@a.co",
                                 :role    "role/admin"}
         :handler         (fn [x]
                            (prn "x" x))
         :error-handler   (fn [x]
                            (prn "error x" x))})







  ;fetch("http://localhost:3000/api/users", {
  ;                                          method: "POST",
  ;
  ;                                          headers: {'Content-Type': 'application/json',
  ;                                                    'Access-Control-Allow-Origin': '*/*',
  ;
  ;                                                    },
  ;                                          body: JSON.stringify({
  ;                                                                "user/name": "New A",
  ;                                                                           "user/title": "string",
  ;                                                                "user/email": "user@mail.com",
  ;                                                                "user/role": "role/admin",
  ;                                                                "user/age": 10
  ;                                                                })
  ;                                          }).then(res => {
  ;                                                          console.log("Request complete! response:", res);
  ;                                                          });



  (defonce todo-response-test (atom nil))
  (go (let [response (<! (http/post "http://localhost:3000/api/users"
                                    {:with-credentials? false
                                     :request-method    :jsonp
                                     :headers           {"Accept"                      "http://localhost:8280"
                                                         "Access-Control-Allow-Origin" "http://localhost:8280"}
                                     :json-params       {:name  "Name"
                                                         :title "Title"
                                                         :email "user@email.com"
                                                         :role  "role"
                                                         :age   24}}))
            body (:body response)]
        (reset! todo-response-test response)
        body))

  (go (let [response (<! (http/delete "http://localhost:3000/api/users/9013a970-30d8-4333-89c2-184f04fb077d"
                                      {:with-credentials? false}))]
        (:body response)))

  (def data (atom nil))
  (def go-response (go (let [response (<! (http/get "http://localhost:3000/api/users"
                                                    {:with-credentials? false
                                                     :query-params      {"since" 135}}))]
                         (reset! data response)
                         (:body response))))

  (go

    (prn "--->" (<! go-response)))

  (async/<!! go-response)
  )









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


