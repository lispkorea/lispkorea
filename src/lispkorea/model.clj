(ns lispkorea.model
  (:require [monger.core :as mg])
  (:import [com.mongodb MongoOptions ServerAddress]))

(defn connect [& url]
  (if url
    (mg/connect-via-uri! url)
    (let [mongohq-url (get (System/getenv) "MONGOHQ_URL")]
      (if mongohq-url
        (mg/connect-via-uri! mongohq-url)
        (mg/connect!)))))

(def ^{:dynamic true} *conn* (connect))