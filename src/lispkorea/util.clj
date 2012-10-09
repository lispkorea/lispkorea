(ns lispkorea.util
  (:require [cheshire.custom :as cheshire])
  (:use [noir.response :only [content-type]]
        [lispkorea.global :only [*request*]]))

(defn wants-json? []
  (let [headers (:headers *request*)
        accept (get headers "accept")]
    (boolean (re-find #"application/json" accept))))

(defn json-response [content]
  (content-type "application/json; charset=utf-8"
                (cheshire/encode content)))