(ns lispkorea.views.welcome
  (:require [net.cgrand.enlive-html :as html])
  (:use [noir.core :only [defpage]]))

(html/deftemplate index "lispkorea/template/index.html"
  [ctxt])

(defpage "/" []
  (index {}))

  