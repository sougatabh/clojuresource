(ns clojuresource.core
  (:use compojure.core)
   (:require [clojure.pprint    :as pp]
            [compojure.handler :as handler]
            [compojure.route   :as route]
            [ring-jdbc-session.core  :as rjss]
            [ring.util.response      :as response]
            [ring.middleware.cookies        :only [wrap-cookies]]
            [taoensso.timbre         :as log]
            [clojuresource.worker  :as worker]
            [clojuresource.global  :as global]))

(defroutes public-routes
  (route/files "/" {:root "public"})
  (GET "/"  {:as request}  (worker/home request))
  (GET "/user"  {:as request}  (worker/user request))
  (POST "/signup"  {:as request}  (worker/register request))
  (GET "/signup/verify/:verification-code"   [verification-code]   (worker/verify-signup verification-code))
  (POST "/signin"  {:as request}  (worker/signin request))
  (GET "/project"  {:as request}  (worker/render-project request))
  (GET "/category"  {:as request}  (worker/render-category request))
  (GET "/submit-projectpage"  {:as request}  (worker/submit-projectpage request))
  (POST "/submit-project"  {:as request}  (worker/submit-project request))
  (GET "/logout"  {:as request}  (worker/logout request))
  (GET "/about"  {:as request}  (worker/about request))
  (GET "/contact"  {:as request}  (worker/contact request))
  (GET "/contact"  {:as request}  (worker/contact request))
  (GET "/forgotpassword"  {:as request}  (worker/render-forgotpassword-page request))
  (POST "/forgotpasswordlink"  {:as request}  (worker/forgotpassword request))
  (GET "/user/forgotpassword/:forgotpassword-token"  [forgotpassword-token]  (worker/resetpasswordpage forgotpassword-token))
  (POST "/resetpassword"  {:as request}  (worker/resetpassword request))
  
  )

(defroutes static-routes
  (route/resources "/"))


(defroutes not-found
  (ANY  "/*"      [] (response/redirect "/404.html"))
  (route/not-found "Not Found"))

(defroutes app-routes
  static-routes
  public-routes
  not-found)





(def app (compojure.handler/site app-routes))


