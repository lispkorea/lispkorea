(ns lispkorea.model
  (:require [monger.core :as mg]
            [monger.collection :as mc]))

(defn connect [& url]
  (if url
    (mg/connect-via-uri! url)
    (let [mongohq-url (get (System/getenv) "MONGOHQ_URL")]
      (if mongohq-url
        (mg/connect-via-uri! mongohq-url)
        (mg/connect!)))))

(def ^{:dynamic true} *conn* (connect))
(def ^{:dynamic true} *db* (mg/get-db *conn* "lispkorea"))
(when *db*
  (mg/set-db! *db*))

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
     