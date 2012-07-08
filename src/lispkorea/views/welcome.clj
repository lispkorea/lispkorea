(ns lispkorea.views.welcome
  (:require [net.cgrand.enlive-html :as html]
            [noir.session :as session])
  (:use [noir.core :only [defpage]]
        [noir.request :only [ring-request]]
        [noir.response :only [redirect]]
        [lispkorea.model.user :only [get-user-by-email]])
  (:import [org.openid4java.consumer ConsumerManager]))

(html/deftemplate index "lispkorea/template/index.html"
  [ctx]
  [:#username] (if (:user ctx)
                 (html/do->
                   (html/set-attr :href "#")
                   (html/content (:email (:user ctx))))
                 (html/content "login"))
  [:#flash] (when (:flash ctx)
              identity)
  [:#flash :> html/text-node] (constantly (:flash ctx)))

(defpage "/" []
  (let [user (session/get :logined-user)
        flash (session/flash-get)]
    (index {:user user
            :flash flash})))

(defpage "/login" []
  (let [request (ring-request)
        server-name (:server-name request)
        server-port (:server-port request)
        server-scheme (name (:scheme request))
        cm (ConsumerManager.)
        return-url (str server-scheme "://" server-name ":"
                        server-port "/auth")
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
    (if user
      (session/put! :logined-user user)
      (session/flash-put! "User doesn't exists!"))
    (redirect "/")))

(defpage "/logout" {:as req}
  (let [user (session/get :logined-user)]
    (when user
      (session/remove! :logined-user))
    (redirect "/")))

