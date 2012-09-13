(ns lispkorea.model
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [lispkorea.config :as config])
  (:use [clojure.string :only [lower-case split]])
  (:import [org.bson.types ObjectId]))

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

(defprotocol IDocumentable
  "Representable protocol for mongodb's document name"
  (docname [e]))

(defprotocol IEntity
  "Protocol of whole entity."
  (add! [e])
  (edit! [e])
  (remove! [e]))

(extend-protocol IDocumentable
  java.lang.String
  (docname [e]
    (lower-case e))
  clojure.lang.Symbol
  (docname [e]
    (docname (str e)))
  java.lang.Class
  (docname [e]
    (let [fullname (.getName e)]
      (docname (last (split fullname #"\."))))))

(def ^:private entity-fns
  {:add! (fn [e]
           (mc/insert-and-return (docname (type e)) e))
   :edit! (fn [e]
            (mc/update (docname (type e)) {:_id (:_id e)} e))
   :remove! (fn [e]
              (let [id (or (:_id e)
                           (ObjectId. e))]
                (mc/remove-by-id (docname (type e)) id)))})

(defmacro defentity [type args & body]
  (let [name (docname type)
        constructor (symbol (str type "/create"))
        fn-get-by-id (symbol (str "get-" name "-by-id"))
        fn-get (symbol (str "get-" name))]
    `(do
       (defrecord ~type ~args
         ~@body)
       (defn ~fn-get [p#]
         (let [m# (mc/find-one-as-map ~name p#)]
           (when m#
             (~constructor m#))))
       (defn ~fn-get-by-id [id#]
         (try
           (~fn-get {:_id (ObjectId. id#)})
           (catch java.lang.IllegalArgumentException iae#
             nil))))))

(defentity User [_id
                 email
                 created-at]
  Object
  (toString [_]
            (format "User<_id=%s, email=%s>" _id email)))

(extend User
  IEntity
  entity-fns)

(defn init-index []
  (mc/ensure-index (docname User) {:email 1} {:unique true}))

(defn prepare []
  (connect)
  (init-index))
