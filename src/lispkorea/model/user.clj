(ns lispkorea.model.user
  (:require [lispkorea.model :as model]
            [monger.collection :as mc])
  (:import [org.bson.types ObjectId]))

(defstruct user :id :email :created-at)
(model/defcrud "users")
(defn get-user-by-email [email]
  (get-by {:email email}))

