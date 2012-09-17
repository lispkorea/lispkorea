(ns leiningen.lispkorea.user
  (:require [lispkorea.model :as model]))

(defn add-user [project & args]
  "Add user account using email address"
  (when-let [email (first args)]
    (model/prepare)
    (let [user (model/map->User {:email email
                                 :created-at (java.util.Date.)})]
      (model/add! user))))

(defn del-user [project & args]
  "Delete user account using email address"
  (when-let [email (first args)]
    (model/prepare)
    (let [user (model/get-user {:email email})]
      (model/remove! user))))

