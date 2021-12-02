(ns frontend.pages.user.user-list
  (:require ["@headlessui/react" :refer (Transition)]
            [reagent.core :as r]
            [frontend.db.db :as db]
            [frontend.db.user :as db-user]
            [frontend.components.components :as components]
            [frontend.domain.user :as domain-user]
            [re-frame.core :as re-frame :refer [subscribe dispatch reg-event-fx reg-event-db reg-sub]]
            [frontend.utils.tailwind-styles :as styles]))


;;;;;;;;;;;;;; TODO cljs interop https://stackoverflow.com/questions/58977738/javascript-to-clojurescript-interop-with-react-spring
(prn "Transition: "Transition)
(defn user-page
  []
  (let [edit? true
        users @(subscribe [:users])

        showing? (r/atom true)
        setIsShowing (fn []
                       (reset! showing? (not @showing?)))]

    (fn []
      [:div
       (let []
         [:div
          (str "showing?: " @showing?)
          [:button {:onClick setIsShowing} "button"]
          [:transition {:show      showing?
                        :enter     "transition-opacity duration-75"
                        :enterFrom "opacity-0"
                        :enterTo   "opacity-100"
                        :leave     "transition-opacity duration-150"
                        :leaveFrom "opacity-100"
                        :leaveTo   "opacity-0"}
           "I will fade in and out"]])



       [:div {:class [styles/relative styles/md:flex styles/md:flex-row styles/h-screen styles/bg-white]}
        [:div {:class [styles/md:flex styles/md:flex-shrink-0 styles/md:overflow-x-auto styles/md:flex-1]}
         (components/user-list users
                               {:add-user-fn
                                (fn []
                                  (let [user (domain-user/generate-user-stub)]
                                    (dispatch [:user.event/add-user user])))
                                :delete-user-fn
                                (fn [user-id]
                                  (dispatch [:user.event/delete-user user-id]))})]
        [:div {:class [styles/md:flex styles/md:flex-col styles/md:min-w-0 styles/md:flex-1]}
         [components/user-edit]]]])))



;; re-frame events and db handlers
(reg-sub
  :users
  (fn [db _]
    (:users db)))

;; add user
(reg-event-fx
  :user.event/add-user
  (fn [{:keys [db]} [_ user]]
    (try
      (do (db-user/add-user user)
          {:dispatch [:user.event/add-user-success user]})
      (catch js/Error e
        {:dispatch [:user.event/add-user-failure user]}))))

(reg-event-db
  :user.event/add-user-success
  (fn [db [_ user]]
    (assoc db :users (conj (:users db) user))))

(reg-event-db
  :user.event/add-user-failure
  (fn [db [_ user-id]]
    (prn "add user failure" user-id)))


;; delete user
(reg-event-fx
  :user.event/delete-user
  (fn [{:keys [db]} [_ user-id]]
    (try
      (do (db-user/delete-user-by-user-id user-id)
          {:dispatch [:user.event/delete-user-success user-id]})
      (catch js/Error e
        {:dispatch [:user.event/delete-user-failure user-id]}))))

(reg-event-db
  :user.event/delete-user-success
  (fn [db [_ user-id]]
    (let [users (->> (:users db)
                     (filter #(not= (:user/id %) user-id)))]
      (assoc db :users users))))

(reg-event-db
  :user.event/delete-user-failure
  (fn [db [_ user-id]]
    (prn "delete user failure" user-id)))




