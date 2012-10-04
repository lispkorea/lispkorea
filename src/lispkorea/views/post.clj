(ns lispkorea.views.post
  (:require [noir.session :as session]
            [lispkorea.template :as template]
            [lispkorea.model :as model])
  (:use [noir.core :only [defpage]]
        [noir.response :only [redirect]]
        [lispkorea.global :only [*user* *flash* *request*]]))

(defpage get-posts "/posts" {type :type}
  (binding [*user* (session/get :logined-user)]
    (template/html-doc)))

(defpage get-post "/posts/:_id" {post-id :_id}
  (binding [*user* (session/get :logined-user)]
    (let [post (model/get-post-by-id post-id)]
      (template/html-doc [:p (str post)]))))

