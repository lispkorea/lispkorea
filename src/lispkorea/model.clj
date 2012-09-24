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
        fn-get (symbol (str "get-" name))
        fn-gets (symbol (str "get-" name "s"))]
    `(do
       (defrecord ~type ~args
         ~@body)
       (defn ~fn-get [p#]
         (let [m# (mc/find-one-as-map ~name p#)]
           (when m#
             (~constructor m#))))
       (defn ~fn-gets [p#]
         (let [ms# (mc/find-maps ~name p#)]
           (map (fn [m#] (~constructor m#)) ms#)))
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
