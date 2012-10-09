(defproject lispkorea "1.0.0-SNAPSHOT"
  :description "lisp을 좋아하는 사람들의 그룹(http://groups.google.com/group/lisp-korea)의 웹사이트입니다."
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [com.novemberain/monger "1.2.0"]
                 [noir "1.3.0-beta10"]
                 [org.openid4java/openid4java "0.9.5"]
                 [cheshire "4.0.3"]]
  :plugins [[lein-beanstalk "0.2.2"]
            [lein-ring "0.7.1"]]
  :main lispkorea.server
  :ring {:handler lispkorea.server/handler
         :init lispkorea.model/prepare})
