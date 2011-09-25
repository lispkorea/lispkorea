(defproject lispkorea "1.0.0-SNAPSHOT"
  :description "lisp을 좋아하는 사람들의 그룹(http://groups.google.com/group/lisp-korea)의 website"
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [congomongo "0.1.4-SNAPSHOT"]
                 [enlive "1.0.0-SNAPSHOT"]
                 [compojure "0.6.4"]]
  :dev-dependencies [[swank-clojure "1.3.2"]
                     [org.clojure/clojure "1.2.1" :classifier "sources"]
                     [lein-ring "0.4.5"]]
  :ring {:handler lispkorea.handler/app})