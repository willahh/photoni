(ns photoni.webapp.frontend.pages.common.layout-default
  (:require [re-frame.core :as re-frame :refer [subscribe dispatch]]
            [photoni.webapp.frontend.events :as events]
            [photoni.webapp.frontend.components.layout :as layout]))

(defn layout-view
  [component]
  [layout/layout component])

