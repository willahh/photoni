(ns photoni.webapp.frontend.translations.translations
  (:require [photoni.webapp.domain.common.log :as log]
            [goog.string :as gstring]
            [goog.string.format]))

(def fr
  {:trans.user.list/title      "Titre",
   :trans.user.field/title     "Titre",
   :trans.user.insert/title    "Ajouter un utilisateur",
   :trans.user.field/email     "Email",
   :trans.user.copy/subtitle   "",
   :trans.user.field/age       "Age",
   :trans.user.insert/subtitle "",
   :trans.user.field/name      "Nom",
   :trans.user.field/role      "Rôle",
   :trans.user.edit/title      "Editer un utilisateur",
   :trans.user.field/id        "Id",
   :trans.user.field/picture   "Image",
   :trans.user.copy/title      "Copier un utilisateur",
   :trans/trans-not-found      "Traduction %s inexistante",
   :trans.user.edit/subtitle   ""})

(def en
  {:trans.user.list/title      "Title",
   :trans.user.field/title     "Title",
   :trans.user.insert/title    "Add an user",
   :trans.user.field/email     "Email",
   :trans.user.copy/subtitle   "",
   :trans.user.field/age       "Age",
   :trans.user.insert/subtitle "",
   :trans.user.field/name      "Name",
   :trans.user.field/role      "Role",
   :trans.user.edit/title      "Edit an user",
   :trans.user.field/id        "Id",
   :trans.user.field/picture   "Picture",
   :trans.user.copy/title      "Copy an user",
   :trans/trans-not-found      "Translation %s not found",
   :trans.user.edit/subtitle   ""}

  )

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
  (generate-new-translations-from-missing-translations fr)
  (generate-new-translations-from-missing-translations en)
  )