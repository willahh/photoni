(ns photoni.webapp.frontend.translations.translations
  (:require [photoni.webapp.domain.common.log :as log]
            [goog.string :as gstring]
            [goog.string.format]))

(def fr
  {:trans.group.list/view-grid       "View-grid",
   :trans.user.list/title            "Liste des utilisateurs",
   :trans.user.field/title           "Titre",
   :trans.group.field/name           "Name",
   :trans.user.insert/title          "Ajouter un utilisateur",
   :trans.user.field/email           "Email",
   :trans.user.copy/subtitle         "",
   :trans.user.field/age             "Age",
   :trans.user.list/add              "Ajouter un utilisateur",
   :trans.entity.list/title          "Title",
   :trans.user.insert/subtitle       "",
   :trans.user.field/name            "Nom",
   :trans.list/view-grid             "Affichage en vignette",
   :trans.group.field/id             "Id",
   :trans.group.list/add             "Ajouter un groupe",
   :trans.user.field/role            "Rôle",
   :trans.user.edit/title            "Editer un utilisateur",
   :trans.list/filter                "Filtrer",
   :trans.user.field/id              "Id",
   :trans.user.field/picture         "Image",
   :trans.user.copy/title            "Copier un utilisateur",
   :trans.group.list/title           "Liste des groupes",
   :trans.list/view-list             "Affichage en liste",
   :trans/trans-not-found            "Traduction %s inexistante",
   :trans.user.edit/subtitle         ""
   :trans.entity.list/confirm-delete "Confirmer la suppression ?"})

(def en
  {:trans.user.list/title            "Users list",
   :trans.user.field/title           "Title",
   :trans.group.field/name           "Name",
   :trans.user.insert/title          "Add an user",
   :trans.user.field/email           "Email",
   :trans.user.copy/subtitle         "",
   :trans.user.field/age             "Age",
   :trans.user.list/add              "Add an user",
   :trans.entity.list/title          "Title",
   :trans.user.insert/subtitle       "",
   :trans.user.field/name            "Name",
   :trans.list/view-grid             "Grid display",
   :trans.group.field/id             "Id",
   :trans.group.list/add             "Add a group",
   :trans.user.field/role            "Role",
   :trans.user.edit/title            "Edit an user",
   :trans.list/filter                "Filter",
   :trans.user.field/id              "Id",
   :trans.user.field/picture         "Picture",
   :trans.user.copy/title            "Copy an user",
   :trans.group.list/title           "Liste des groupes",
   :trans.list/view-list             "List view",
   :trans/trans-not-found            "Translation %s not found",
   :trans.user.edit/subtitle         ""
   :trans.entity.list/confirm-delete "Confirm the delete ?"
   })

(defonce missing-translations-keys (atom #{}))



(defn generate-new-translations-from-missing-translations
  "Generate a new local map with missing translations"
  [translation-local]
  (->> @missing-translations-keys
       (reduce (fn [acc kw]
                 (assoc acc kw (clojure.string/capitalize (str (name kw)))))
               {})
       (merge translation-local)))

(defn trans [k]
  (let [default-translation (gstring/format (get fr :trans/trans-not-found) k)
        trans (get fr k)]
    (if trans
      trans
      (do
        (log/error default-translation)
        (swap! missing-translations-keys conj k)
        default-translation))))

(comment
  (def missing-translations-keys (atom #{}))
  (generate-new-translations-from-missing-translations fr)
  (generate-new-translations-from-missing-translations en)
  )