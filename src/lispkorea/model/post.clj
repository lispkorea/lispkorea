(ns lispkorea.model.post
  (:require [lispkorea.model :as model]
            [monger.collection :as mc])
  (:import [org.bson.types ObjectId]))

(defstruct post :author :title :content :created-at)
(model/defcrud "post")

;; (defn get [id]
;;   (mc/find-one-as-map "posts" {:_id (ObjectId. id)}))

;; (defn add! [p]
;;   (mc/insert "posts" p))

;; (defn edit! [p]
;;   (mc/update "posts" {:_id (:_id p)} p))

;; (defn remove! [arg]
;;   (let [id (or (:_id arg) (ObjectId. arg))]
;;     (mc/remove "posts" {:_id id})))


  