(ns clojuresource.global)

;;; ----- Static stuff -----

(def VARIFICATION-URL-BASE "http://192.241.190.252:3000/signup/verify/")
(def FORGOT-PASSWORD-URL-BASE "http://192.241.190.252:3000/user/forgotpassword/")



(def ^{:doc "DB flavor"}
  dbflavor nil)


(def ^{:doc "DB connection map"}
  db nil)


(def ^{:doc "Template group"}
  templates [{:source :classpath
                          :caches 0         ;; seconds# to cache for, 0 = none
                          :prefix "templates/"}])


(def ^{:doc "Email configuration"}
  mail {:user             "clojuresource@gmail.com"
                          :password "sougata123"
                          :host     "smtp.gmail.com"
                          :port     465
                          :ssl      true
                          :from "clojuresource@gmail.com"
                          :activation-content "Please click Below Link to activate your account ACTIVATION_URL"
                          :forgotpassword-content "Please click Below Link to Change your password ACTIVATION_URL"})





(def ^{:doc "Signup function"}
  signup (fn [username password fullname]
  	       (throw (RuntimeException. "Not initialized"))))



(def ^{:doc "Twitter login configuration"}
  twitter-login nil)


;;; ----- Dynamic state -----


(def ^{:dynamic true
       :doc "HTTP Ring request map. Rebound in the `core` namespace."}
  *http-request* nil)