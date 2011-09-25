(ns lispkorea.handler
  (:use compojure.core)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [net.cgrand.enlive-html :as html]))

(html/deftemplate index "lispkorea/template/index.html"
  [ctxt])
(defroutes main-routes
  (GET "/" [] (index {}))
  (route/files "/" {:root "public"})
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (handler/site main-routes))