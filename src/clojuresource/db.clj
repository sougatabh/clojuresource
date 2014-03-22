(ns clojuresource.db
  (:require [clojure.string       :as str]
             [clojure.java.jdbc    :as sql]
             [clj-dbcp.core     :as dbcp]
             [taoensso.timbre   :as log]
             [clojuresource.global :as global])
  (:import (java.sql Date Timestamp SQLException)))


(def mysql-db 
  {:datasource (dbcp/make-datasource :mysql {:host 'localhost :database 'clojuresource
                         :user :root   :password 'secr3t})})

(defn generated-key
  [k m]
  (or (get m :generated_key)
      (get m k)))


(defn create-user
  [fullname email password salt verification-token status created-ts]
   (->>(sql/with-connection mysql-db(sql/insert-values :Users [:fullname :email :password :salt :verification_token :status :created_ts] 
                                                  [fullname email password salt  verification-token "INACTIVE" created-ts]))
                                 (generated-key :id)))


(defn retrieve-projects
  [categoryId]
  (log/debug categoryId)
  (sql/with-connection mysql-db(sql/with-query-results rs 
               [(str "select id,name,description,homepage from Project where category_id=" categoryId)]
                
                (doall rs))))


(defn project-exists
  [project-name]
  (log/debug project-name)
  (sql/with-connection mysql-db(sql/with-query-results rs 
               [(str "select name from Project where name='" project-name "'")]
                
                (doall rs))))


(defn get-user
  [email]
  (sql/with-connection mysql-db(sql/with-query-results rs 
               [(str "select email,password,salt from Users where email='" email "'" )]
                (doall rs))))

(defn get-user-by-activation-code
  [activation-code]
  (log/debug activation-code)
  (sql/with-connection mysql-db(sql/with-query-results rs 
               [(str "select email from Users where verification_token='" activation-code "'")]
                (doall rs))))


(defn activate-user
  [verification-token]
  (sql/with-connection mysql-db(sql/update-values :Users ["verification_token=?" verification-token] {:status "ACTIVE" })))

(defn user-forgot-token-update
  [verification-token email]
  (sql/with-connection mysql-db(sql/update-values :Users ["email=?" email] {:forgotpassword_token verification-token})))


(defn user-password-update
  [email password]
  (sql/with-connection mysql-db(sql/update-values :Users ["email=?" email] {:password password})))

(defn user-token-update
  [email token]
  (sql/with-connection mysql-db(sql/update-values :Users ["email=?" email] {:token token})))

(defn get-active-user
  [email]
  (sql/with-connection mysql-db(sql/with-query-results rs 
               [(str "select email,password,salt from Users where status='ACTIVE' and email='" email "'" )]
                (doall rs))))

(defn get-user-by-token
  [token]
  (sql/with-connection mysql-db(sql/with-query-results rs 
               [(str "select id,email from Users where token='" token "'")]
                (doall rs))))


(defn get-user-by-forgotpassword-token
  [activation-code]
  (log/debug activation-code)
  (sql/with-connection mysql-db(sql/with-query-results rs 
               [(str "select email from Users where forgotpassword_token='" activation-code "'")]
                (doall rs))))


(defn create-category
  [name description userId]
   (->>(sql/with-connection mysql-db(sql/insert-values :Category [:name :description :user_id] 
                                                  [name description userId]))
                                 (generated-key :id)))

(defn retrieve-licences
  []
  (sql/with-connection mysql-db(sql/with-query-results rs 
               [(str "select id,name from Licence")]
                (doall rs))))


(defn create-project
  [name description userId homepage licenseId categoryId]
   (->>(sql/with-connection mysql-db(sql/insert-values :Project [:name :description :user_id :homepage :license_id :category_id] 
                                                  [name description userId homepage licenseId categoryId]))
                                 (generated-key :id)))


(defn retrieve-categories
  []
  (sql/with-connection mysql-db(sql/with-query-results rs 
               [(str "SELECT id, name FROM Category ORDER BY name")]
                (doall rs))))

(defn retrieve-latest-projects
  []
  (sql/with-connection mysql-db(sql/with-query-results rs 
               [(str "select id,name,description,homepage from Project order by id DESC LIMIT 10")]
                (doall rs))))


(defn retrieve-project
  [id]
  (sql/with-connection mysql-db(sql/with-query-results rs 
               [(str "select id,name,description,homepage from Project where id=" id)]
                (doall rs))))

