(ns lispkorea.server
  (:require [noir.server :as server]))

(server/load-views "src/lispkorea/views/")

(def handler (server/gen-handler {:mode :dev
                                  :ns 'lispkorea.server}))
(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (server/start port {:mode mode
                        :ns 'lispkorea})))


            