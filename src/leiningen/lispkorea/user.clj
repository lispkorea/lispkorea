(ns leiningen.lispkorea.user
  (:use [leiningen.core.eval :only (eval-in-project)]))

(defn add-user [project & args]
  "Add user account using email address"
  (when-let [email (first args)]
    (eval-in-project
     project
     `(do
        (model/prepare)
        (let [user# (lispkorea.model.User/create
                     {:email ~email
                      :created-at (java.util.Date.)})]
          (model/add! user#)))
     '(require '[lispkorea.model :as model]))))

(defn del-user [project & args]
  "Delete user account using email address"
  (when-let [email (first args)]
    (eval-in-project
     project
     `(do
        (model/prepare)
        (let [user# (model/get-user {:email ~email})]
          (model/remove! user#)))
     '(require '[lispkorea.model :as model]))))

