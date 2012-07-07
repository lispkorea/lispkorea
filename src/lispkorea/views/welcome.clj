(ns lispkorea.views.welcome
  (:require [net.cgrand.enlive-html :as html]
            [noir.session :as session])
  (:use [noir.core :only [defpage]]
        [noir.response :only [redirect]]
        [lispkorea.model.user :only [get-user-by-email]])
  (:import [org.openid4java.consumer ConsumerManager]))

(html/deftemplate index "lispkorea/template/index.html"
  [user]
  [:#username] (if user
                 (html/content (:email user))
                 (html/content "login")))

(defpage "/" []
  (let [user (session/get :logined-user)]
    (index user)))

(defpage "/login" []  
  (let [cm (ConsumerManager.)
        return-url "http://localhost:8080/auth"
        user-supplied-string "https://www.google.com/accounts/o8/id"
        discoveries (.discover cm user-supplied-string)
        discovered (.associate cm discoveries)
        auth-req (.authenticate cm discovered return-url)
        redirect-response (redirect (.getDestinationUrl auth-req true))]
    (update-in redirect-response [:headers "Location"] str
               "&openid.ns.ax=http://openid.net/srv/ax/1.0"
               "&openid.ax.mode=fetch_request"
               "&openid.ax.required=email"
               "&openid.ax.type.email=http://axschema.org/contact/email")))

(defpage "/auth" {:as req}
  (let [email (req "openid.ext1.value.email")
        user (get-user-by-email email)]
    (when user
      (session/put! :logined-user user))
    (redirect "/")))

(defpage "/logout" {:as req}
  (let [user (session/get :logined-user)]
    (when user
      (session/remove! :logined-user))
    (redirect "/")))


      
    
    
    
