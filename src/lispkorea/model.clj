(ns lispkorea.model
  (:use somnium.congomongo)
  (:use [somnium.congomongo.config :only [*mongo-config*]]))

(defn split-mongo-url [url]
  "Parses mongodb url from heroku, eg. mongodb://user:pass@localhost:1234/db"
  (let [matcher (re-matcher #"^.*://(.*?):(.*?)@(.*?):(\d+)/(.*)$" url)]
    (when (.find matcher)
      (zipmap [:match :user :pass :host :port :db]
              (re-groups matcher)))))

(defn maybe-init [url]
  "Checks if connection and collection exist, otherwise initialize."
  (when (not (connection? *mongo-config*))
    (let [mongo-url (or (get (System/getenv) "MONGOHQ_URL")
                        url)
          config    (split-mongo-url mongo-url)] 
      (mongo! :db (:db config)
              :host (:host config)
              :port (Integer. (:port config))) 
      (authenticate (:user config)
                    (:pass config)) 
      (when (not (collection-exists? :user))
        (create-collection! :user)))))
