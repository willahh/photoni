(ns photoni.webapp.frontend.components.layout)

(defn layout
  [{:view.layout/keys [go-to-home-fn
                       go-to-about-fn
                       go-to-user-fn]} component]
  [:div.min-h-screen.bg-white
   [:div
    [:div [:a {:on-click go-to-home-fn} "Home"]]
    [:div [:a {:on-click go-to-about-fn} "About"]]
    [:div [:a {:on-click go-to-user-fn} "Users"]]]
   [:nav.bg-white.border-b.border-gray-200
    [:div.max-w-7xl.mx-auto.px-4.sm:px-6.lg:px-8
     [:div.flex.justify-between.h-16
      [:div.flex
       [:div.flex-shrink-0.flex.items-center
        [:img.block.lg:hidden.h-8.w-auto {:src "https://tailwindui.com/img/logos/workflow-mark-indigo-600.svg" :alt "Workflow"}]
        [:img.hidden.lg:block.h-8.w-auto {:src "https://tailwindui.com/img/logos/workflow-logo-indigo-600-mark-gray-800-text.svg" :alt "Workflow"}]]
       [:div.hidden.sm:-my-px.sm:ml-6.sm:flex.sm:space-x-8
        [:a.border-indigo-500.text-gray-900.inline-flex.items-center.px-1.pt-1.border-b-2.text-sm.font-medium {:href "#" :aria-current "page"} "Users"]
        [:a.border-transparent.text-gray-500.hover:border-gray-300.hover:text-gray-700.inline-flex.items-center.px-1.pt-1.border-b-2.text-sm.font-medium {:href "#"} "Groups"]
        [:a.border-transparent.text-gray-500.hover:border-gray-300.hover:text-gray-700.inline-flex.items-center.px-1.pt-1.border-b-2.text-sm.font-medium {:href "#"} "Projects"]
        [:a.border-transparent.text-gray-500.hover:border-gray-300.hover:text-gray-700.inline-flex.items-center.px-1.pt-1.border-b-2.text-sm.font-medium {:href "#"} "Calendar"]]]
      [:div.hidden.sm:ml-6.sm:flex.sm:items-center
       [:button.bg-white.p-1.rounded-full.text-gray-400.hover:text-gray-500.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500 {:type "button"}
        [:span.sr-only "View notifications"]
        [:svg.h-6.w-6 {:xmlns "http://www.w3.org/2000/svg" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor" :aria-hidden "true"}
         [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"}]]]
       [:div.ml-3.relative
        [:div
         [:button#user-menu-button.max-w-xs.bg-white.flex.items-center.text-sm.rounded-full.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500 {:type "button" :aria-expanded "false" :aria-haspopup "true"}
          [:span.sr-only "Open user menu"]
          [:img.h-8.w-8.rounded-full {:src "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80" :alt ""}]]]
        [:div.origin-top-right.absolute.right-0.mt-2.w-48.rounded-md.shadow-lg.py-1.bg-white.ring-1.ring-black.ring-opacity-5.focus:outline-none {:role "menu" :aria-orientation "vertical" :aria-labelledby "user-menu-button" :tabindex "-1"}
         [:a#user-menu-item-0.block.px-4.py-2.text-sm.text-gray-700 {:href "#" :role "menuitem" :tabindex "-1"} "Your Profile"]
         [:a#user-menu-item-1.block.px-4.py-2.text-sm.text-gray-700 {:href "#" :role "menuitem" :tabindex "-1"} "Settings"]
         [:a#user-menu-item-2.block.px-4.py-2.text-sm.text-gray-700 {:href "#" :role "menuitem" :tabindex "-1"} "Sign out"]]]]
      [:div.-mr-2.flex.items-center.sm:hidden
       [:button.bg-white.inline-flex.items-center.justify-center.p-2.rounded-md.text-gray-400.hover:text-gray-500.hover:bg-gray-100.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500 {:type "button" :aria-controls "mobile-menu" :aria-expanded "false"}
        [:span.sr-only "Open main menu"]
        [:svg.block.h-6.w-6 {:xmlns "http://www.w3.org/2000/svg" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor" :aria-hidden "true"}
         [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M4 6h16M4 12h16M4 18h16"}]]
        [:svg.hidden.h-6.w-6 {:xmlns "http://www.w3.org/2000/svg" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor" :aria-hidden "true"}
         [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M6 18L18 6M6 6l12 12"}]]]]]]
    [:div#mobile-menu.sm:hidden
     [:div.pt-2.pb-3.space-y-1
      [:a.bg-indigo-50.border-indigo-500.text-indigo-700.block.pl-3.pr-4.py-2.border-l-4.text-base.font-medium {:href "#" :aria-current "page"} "Dashboard"]
      [:a.border-transparent.text-gray-600.hover:bg-gray-50.hover:border-gray-300.hover:text-gray-800.block.pl-3.pr-4.py-2.border-l-4.text-base.font-medium {:href "#"} "Team"]
      [:a.border-transparent.text-gray-600.hover:bg-gray-50.hover:border-gray-300.hover:text-gray-800.block.pl-3.pr-4.py-2.border-l-4.text-base.font-medium {:href "#"} "Projects"]
      [:a.border-transparent.text-gray-600.hover:bg-gray-50.hover:border-gray-300.hover:text-gray-800.block.pl-3.pr-4.py-2.border-l-4.text-base.font-medium {:href "#"} "Calendar"]]
     [:div.pt-4.pb-3.border-t.border-gray-200
      [:div.flex.items-center.px-4
       [:div.flex-shrink-0
        [:img.h-10.w-10.rounded-full {:src "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80" :alt ""}]]
       [:div.ml-3
        [:div.text-base.font-medium.text-gray-800 "Tom Cook"]
        [:div.text-sm.font-medium.text-gray-500 "tom@example.com"]]
       [:button.ml-auto.bg-white.flex-shrink-0.p-1.rounded-full.text-gray-400.hover:text-gray-500.focus:outline-none.focus:ring-2.focus:ring-offset-2.focus:ring-indigo-500 {:type "button"}
        [:span.sr-only "View notifications"]
        [:svg.h-6.w-6 {:xmlns "http://www.w3.org/2000/svg" :fill "none" :viewBox "0 0 24 24" :stroke "currentColor" :aria-hidden "true"}
         [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"}]]]]
      [:div.mt-3.space-y-1
       [:a.block.px-4.py-2.text-base.font-medium.text-gray-500.hover:text-gray-800.hover:bg-gray-100 {:href "#"} "Your Profile"]
       [:a.block.px-4.py-2.text-base.font-medium.text-gray-500.hover:text-gray-800.hover:bg-gray-100 {:href "#"} "Settings"]
       [:a.block.px-4.py-2.text-base.font-medium.text-gray-500.hover:text-gray-800.hover:bg-gray-100 {:href "#"} "Sign out"]]]]]
   [:div.py-10
    [:header
     [:div.max-w-7xl.mx-auto.px-4.sm:px-6.lg:px-8
      [:h1.text-3xl.font-bold.leading-tight.text-gray-900 "Dashboard"]]]
    [:main
     [:div.max-w-7xl.mx-auto.sm:px-6.lg:px-8
      [:div.px-4.py-8.sm:px-0
       [:div.border-4.border-dashed.border-gray-200.rounded-lg.h-96
        component
        #_(for [component components]
            [component])
        ]
       #_(vec (concat
                [:div.border-4.border-dashed.border-gray-200.rounded-lg.h-96]
                component))]]]]])
