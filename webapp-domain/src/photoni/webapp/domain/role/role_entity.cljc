(ns photoni.webapp.domain.role.role-entity
  (:require [malli.core :as m]
            [photoni.webapp.domain.utils :as utils]))

(def non-empty-string
  (m/from-ast {:type       :string
               :properties {:min 1}}))

(def admin :role/admin)
(def super-admin :role/super-admin)
(def validateur :role/validateur)

(def roles [admin
            super-admin
            validateur])


;; ┌───────────────────────────────────────────────────────────────────────────┐
;; │ [spec]                                                                    │
;; └───────────────────────────────────────────────────────────────────────────┘
(def spec-role [:user/role {:title               "Role parameter"
                            :description         "Description for role parameter"
                            :json-schema/default "role/admin"}
                [:and non-empty-string (into [:enum] (map utils/kw->str roles))]])
