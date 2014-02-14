(defproject clojuresource "0.1.0-SNAPSHOT"
  :description "This is clojure Open Source Indexing wesite"
  :url "http://www.clojuresource.net"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [org.mindrot/jbcrypt   "0.3m"]
                 [com.taoensso/timbre   "1.1.0"]
                 [mailaram "0.1.0"]
                 [clj-dbcp      "0.8.1"]
                 [clj-liquibase "0.4.0"]
                 [mysql/mysql-connector-java "5.1.21"]
                 [ring-jdbc-session          "0.1.0"]
                 [compojure     "1.1.5"]
                 [basil         "0.4.1"]
                 [clavatar      "0.1.0"]
                 [oauth-clj     "0.1.0"]
                 ]
  :plugins [[lein-ring "0.8.2"]]
  :ring {:handler clojuresource.core/app})
