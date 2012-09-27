(ns lispkorea.template
  (:use [hiccup.core :only [html]]
        [hiccup.page :only [html5 include-css include-js]]
        [lispkorea.global :only [*user* *flash*]]))
(defmacro html-doc [& body]
  `(html5
    [:head
     [:meta {:charset "utf-8"}]
     [:title "Lisp Korea"]
     [:meta
      {:content "width=device-width, initial-scale=1.0",
       :name "viewport"}]
     [:meta {:content "", :name "description"}]
     [:meta {:content "", :name "author"}]
     (include-css "/css/bootstrap.min.css")
     (include-css "/css/bootstrap-responsive.min.css")]
    [:body {:style "padding-top: 60px"}
     [:div.navbar.navbar-inverse.navbar-fixed-top
      [:div.navbar-inner
       [:div.container
        [:a.brand {:href "#"} "Lisp Korea"]
        [:div.nav-collapse.collapse
         [:ul.nav
          [:li.active [:a {:href "#"} "Home"]]
          [:li [:a {:href "#about"} "About"]]
          [:li [:a {:href "#contact"} "Contact"]]]]
        [:div.btn-group.pull-right
         (if (and (bound? #'*user*) *user*)
           [:ul.dropdown-menu
            [:li [:a {:href "#"} "Profile"]]
            [:li.divider]
            [:li [:a {:href "/logout"} "Sign Out"]]]
           [:a#username.btn.dropdown-toggle
            {:href "/login", :data-toggle "dropdown"}
            "login"])]]]]
     (when (and (bound? #'*flash*) *flash*)
       [:div#flash.alert
        [:button.close {:data-dismiss "alert"} "×"]
        [:span  *flash*]])
     [:div.container
      ~@body]
     (include-js "/js/jquery.js")
     (include-js "/js/bootstrap.min.js")]))