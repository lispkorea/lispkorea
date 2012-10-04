(ns lispkorea.server
  (:require [noir.server :as server]
            [lispkorea.model :as model])
  (:use [lispkorea.global :only [*request*]]))

(server/load-views "src/lispkorea/views/")
(server/add-middleware
 (fn [handler]
   (fn [request]
     (binding [*request* request]
       (handler request)))))

(def handler (server/gen-handler {:mode :dev
                                  :ns 'lispkorea.server}))
(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (model/prepare)
    (server/start port {:mode mode
                        :ns 'lispkorea})))


            