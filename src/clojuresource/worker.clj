(ns clojuresource.worker
  (:require [clojure.pprint       :as pp]
            [clojure.string       :as str]
            [taoensso.timbre      :as log]
            [oauth.twitter        :as toauth]
            [clojuresource.web        :as web]
            [clojuresource.global        :as global]
            [clojuresource.db        :as db]
            [clojuresource.utils        :as utils]
            [mailaram.mailer        :as mailer]
            )
    (:import (org.mindrot.jbcrypt BCrypt)))


(defn each-project
  [each]
  (apply str 
  "<li><a href=/project?id=" (:id each)">"(:name each) "</a></li>"))

(defn sidebar-data
  [each-category]
  (let [projects (db/retrieve-projects (:id each-category))]
    ;;(apply str "<li class=\"navheader\"><a href=/category?id=" (:id each-category) ">"(:name each-category)"</a></li>" (map each-project projects))
    (apply str "<li><a  class=\"nav-menu\" href=/category?id=" (:id each-category) ">"(:name each-category)"" "</a></li>")))

(defn get-sidebar
  []
  (let [catagories (db/retrieve-categories)]
         (apply str (map  sidebar-data catagories) )))

(defn home 
  [request]
  (let [session (:session request)]
     (if(:username session)
      (web/private-render "home.html" {:sidebar (get-sidebar) :latestproject (db/retrieve-latest-projects)})
      (web/public-render "home.html" {:sidebar (get-sidebar) :latestproject (db/retrieve-latest-projects)})
      )))

(defn user
  [request]
  (web/public-render "user.html" {:sidebar (get-sidebar)}))

(defn send-activation-email
  [to activation-code]
  (let [body (clojure.string/replace (:activation-content global/mail) #"ACTIVATION_URL" (utils/generate-varification activation-code))]
  (log/debug body)
  (utils/sendmail to "Activation Email" body)))

(defn send-forgotpassword-email
  [to activation-code]
  (log/debug "Sending Email Please wait!!!!!!!!!!!")
  (let [body (clojure.string/replace (:forgotpassword-content global/mail) #"ACTIVATION_URL" (utils/generate-forgotpassword-url activation-code))]
  (log/debug body)
  (utils/sendmail to "Activation Email" body)))

(defn register-user
  [params]
  (let [password-salt  (BCrypt/gensalt)
      password (BCrypt/hashpw (:password params) password-salt)
          activation-code (utils/md5 (:email params))]

    (db/create-user (:fullname params) (:email params) password password-salt activation-code "INACTIVE" (utils/current-ts))
    (send-activation-email (:email params) activation-code)))


(def email-regex
  (re-pattern
    "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"))

(defn register
  [request]
  (let [params (:params request)]
   (cond
    (empty? (:email params))
      (web/public-render "home.html" {:sidebar (get-sidebar) :latestproject (db/retrieve-latest-projects) :message "Email Not Valid"})
    
    (empty? (:password params))
        (web/public-render "home.html" {:sidebar (get-sidebar) :latestproject (db/retrieve-latest-projects) :message "Password Not Valid"})  
        
        (db/get-user (:email params))
        (web/public-render "home.html" {:sidebar (get-sidebar) :latestproject (db/retrieve-latest-projects) :message "User Already Exists"})  
        
        (not (re-matches email-regex (:email params)))
        (web/public-render "home.html" {:sidebar (get-sidebar) :latestproject (db/retrieve-latest-projects):message "Email not Valid"})  
        
        :default
        (do
        (register-user params)
        (web/public-render "user.html" {:sidebar (get-sidebar) :latestproject (db/retrieve-latest-projects) :message "Email has been sent to your account Please verify"})

        ))))

(defn verify-signup
  [verification-code]
  (let [verified_user (first (db/get-user-by-activation-code verification-code))]
  (if (:email verified_user)
    (do
      (db/activate-user verification-code)
      (web/public-render "signup-result.html" {:sidebar (get-sidebar) :message  "Account verified successfully,Do sign in from the above link"}))
      (web/public-render "signup-result.html" {:sidebar (get-sidebar) :message  "Not a valid account Please sign up"} ))))


(defn signin
  [request]
  (let [params (:params request)
        existing-user (first (db/get-user (:email params)))]
    (if (:email existing-user)
        (let [password-hash (BCrypt/hashpw (:password params) (:salt existing-user))
            ]
            (if (= password-hash (:password existing-user))
              (merge (web/redirect "/submit-projectpage")
                (web/session-assoc :username   (:email existing-user)))
              (web/public-render "user.html" {:sidebar (get-sidebar) :message "Email/Password is wrong"})     
            )
         )
        (web/public-render "user.html" {:sidebar (get-sidebar) :message "User Does not Exists"})     
      )))

(defn render-project
  [request]
  (let [params (:params request)
        project (db/retrieve-project (:id params))
        session (:session request)]
    (log/debug "Going to Render the Project Page " session)
    (if(:username session)
     (web/private-render "project.html" {:sidebar (get-sidebar) :project project})
     (web/public-render "project.html" {:sidebar (get-sidebar) :project project}))))

(defn render-each
 [each-project]
  (str "<div class=\"panel panel-info\">" "<div class=\"panel-heading project-heading\"><h3>" (:name each-project) "</h3></div>" "<div class=\"project-content\"><div class='panel-body'>"
    (:description each-project)"</div>" "<a href=" (:homepage each-project) " >Go to "
    (:name each-project)"</a>" "</div></div>"))

(defn populate-projects [projects]
  (apply str (map  render-each projects)))


(defn render-category
  [request]
  (let [params (:params request)
        projects (db/retrieve-projects (:id params))
        session (:session request)]
    
    (if(:username session)
     (web/private-render "projects.html" {:sidebar (get-sidebar) :projects (populate-projects projects)})
     (web/public-render "projects.html" {:sidebar (get-sidebar) :projects (populate-projects projects)}))))

(defn logout
  [request]
    (do
      (merge (web/redirect "/")
         (web/session-dissoc :username))))

(defn about 
  [request]
  (let [latestproject (db/retrieve-latest-projects)
        session (:session request)]
    (if(:username session)
      (web/private-render "about.html" {:sidebar (get-sidebar)})
      (web/public-render "about.html" {:sidebar (get-sidebar) :latestproject (db/retrieve-latest-projects)})
      )))

(defn contact 
  [request]
  (let [latestproject (db/retrieve-latest-project)
        session (:session request)]
    (if(:username session)
      (web/private-render "contact.html" {:sidebar (get-sidebar)})
      (web/public-render "contact.html" {:sidebar (get-sidebar) :latestproject (db/retrieve-latest-projects)})
      )))

(defn submit-projectpage
  [request]
  (let [params (:params request)
        licenses (db/retrieve-licences)
        categories (db/retrieve-categories)
        session (:session request)]
    (if(:username session)
    (web/private-render "submit-project.html" {:sidebar (get-sidebar) :licenses licenses :categories categories})
    (web/redirect "/user"))))


(defn submit-project
  [request]
  (let [params (:params request)
         session (:session request)
         licenses (db/retrieve-licences)
         categories (db/retrieve-categories)
         userId (:id (first (db/get-user (:email params))))
        project-exists (first (db/project-exists (:name params)))]
      (log/debug project-exists)
     (if (:name project-exists)    
       (web/private-render "submit-project.html" {:sidebar (get-sidebar) :licenses licenses :categories categories :message "Project name exists"})
        (do (db/create-project (:name params) (:desc params) userId (:homepage params) (:license params) (:category params))
          (web/redirect "/")))))


(defn render-forgotpassword-page 
  [request]
        (web/public-render "forgot-password.html" {:sidebar (get-sidebar) :latestproject (db/retrieve-latest-projects)}))


(defn forgotpassword 
  [request]
  (let [params (:params request)
        user (first (db/get-user (:email params)))
        password-tkn (utils/md5 (:email params))]
        (log/debug "Inside forgotpassword" user)
        (if (:email user)
        (do 
             (db/user-forgot-token-update password-tkn (:email params))
             (send-forgotpassword-email (:email user) password-tkn)
             (web/public-render "forgot-password.html" {:sidebar (get-sidebar) :message "Reset password link has been sent to your email" })
          )
        (web/public-render "forgot-password.html" {:sidebar (get-sidebar) :message "User not found" }))))


(defn resetpasswordpage
  [forgot-token]
  (let [user (first (db/get-user-by-forgotpassword-token forgot-token))]
    (log/debug "The User is " user)
    (if (:email user)
      (do
        (log/debug "Token is valid")
        (web/public-render "reset-password.html" {:sidebar (get-sidebar) :email (:email user)}))
      (web/public-render "forgot-password.html" {:sidebar (get-sidebar) :message "User not found" }))
    ))

(defn resetpassword
  [request]
  (let [params (:params request)
        password-salt (:salt (first (db/get-user (:email params))))
        password (BCrypt/hashpw (:rpassword params) password-salt) ]
    (db/user-password-update (:email params) password)
    (web/public-render "home.html" {:sidebar (get-sidebar) :latestproject (db/retrieve-latest-projects) :message "Password Updated successfully"})
    ))