(ns lispkorea.model
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [lispkorea.config :as config]))

(defn connect []
  (if-let [url config/mongodb-url]
    (mg/connect-via-uri! url)
    (do
      (mg/connect!)
      (mg/set-db!(mg/get-db "mydb")))))

(defmacro defcrud [name]
  `(do
     (intern ~*ns*
             '~'get-by-id
             (fn [~'id]
               (mc/find-one-as-map ~name {:_id (ObjectId. ~'id)})))
     (intern ~*ns*
             '~'get-by
             (fn [~'p]
               (mc/find-one-as-map ~name ~'p)))
     (intern ~*ns* '~'add! (fn [~'p]
                             (mc/insert ~name ~'p)))
     (intern ~*ns* '~'edit! (fn [~'p]
                              (mc/update ~name {:_id (:id ~'p)} ~'p)))
     (intern ~*ns* '~'remove! (fn [~'arg]
                                (let [~'id (or (:_id ~'arg) (ObjectId. ~'arg))]
                                  (mc/remove ~name {:_id ~'id}))))))


(defn prepare []
  (connect))