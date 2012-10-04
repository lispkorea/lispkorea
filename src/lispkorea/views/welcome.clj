(ns lispkorea.views.welcome
  (:require [noir.session :as session]
            [lispkorea.template :as template]
            [lispkorea.model :as model])
  (:use [noir.core :only [defpage url-for]]
        [noir.response :only [redirect]]
        [lispkorea.global :only [*user* *flash* *request*]]
        [lispkorea.views.post :only [get-post]])
  (:import [org.openid4java.consumer ConsumerManager]))

(defn- build-dashboard-box [title data]
  (let [ul [:ul (map (fn [[created-at title url]]
                            [:li
                             [:span {:class "date"} created-at]
                             [:a {:href url :class "title"} title]])
                          data)]]
    [:div {:class "dashboard-box"}
     [:h1 title]
     ul]))

(defn- serialize-posts [p]
  [(:created-at p)
   (:title p)
   (url-for get-post p)])

(defpage "/" []
  (binding [*user* (session/get :logined-user)
            *flash* (session/flash-get :login-error)]
    (template/html-doc (build-dashboard-box
                        "공지사항"
                        (map serialize-posts (model/get-posts {:type "notice"})))
                       (build-dashboard-box
                        "뉴스"
                        (map serialize-posts (model/get-posts {:type "news"})))
                       (build-dashboard-box
                        "질의/응답"
                        (map serialize-posts (model/get-posts {:type "q&a"})))
                       (build-dashboard-box
                        "게시판"
                        (map serialize-posts (model/get-posts {:type "bbs"}))))))

(defpage "/login" []
  (let [server-name (:server-name *request*)
        server-port (:server-port *request*)
        server-scheme (name (:scheme *request*))
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
        user (model/get-user {:email email})]
    (if user
      (session/put! :logined-user user)
      (session/flash-put! :login-error "User doesn't exists!"))
    (redirect "/")))

(defpage "/logout" {:as req}
  (let [user (session/get :logined-user)]
    (when user
      (session/remove! :logined-user))
    (redirect "/")))

