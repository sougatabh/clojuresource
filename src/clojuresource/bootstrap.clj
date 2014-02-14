(ns clojuresource.bootstrap  (:require [clojure.string       :as str]
            [clojure.java.jdbc    :as sql]
            [clj-dbcp.core     :as dbcp]
            [taoensso.timbre   :as log]
            [clojure-sources.db   :as db]
            ))

(def mysql-db 
  {:datasource (dbcp/make-datasource :mysql {:host 'localhost :database 'clojuresource
                         :user :root   :password 'root})})


