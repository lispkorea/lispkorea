(defproject lispkorea "1.0.0-SNAPSHOT"
  :description "lisp을 좋아하는 사람들의 그룹(http://groups.google.com/group/lisp-korea)의 웹사이트입니다."
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [congomongo "0.1.4-SNAPSHOT"]
                 [noir "1.2.2"]
                 [enlive "1.0.0"]
                 [lein-swank "1.4.4"]]
  :plugins [[lein-swank "1.4.4"]]
  :main lispkorea.server)

