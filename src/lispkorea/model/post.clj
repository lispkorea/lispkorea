(ns lispkorea.model.post
  (:require [lispkorea.model :as model]
            [monger.collection :as mc])
  (:import [org.bson.types ObjectId]))

(defstruct post :author :title :content :created-at)
(model/defcrud "posts")
