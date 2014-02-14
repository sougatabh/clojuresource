(ns clojuresource.utils
  (:require [clojure.pprint  :as pp]
            [clojure.string  :as str]
            [taoensso.timbre :as log]
            [clojuresource.global :as global]
            [mailaram.core   :as mailaram])
  (:import (java.security MessageDigest)
           (java.math BigInteger)
           (java.util UUID)))


(defn echo
  ([msg x]
     (print (format "[ECHO] %s " msg))
     (pp/pprint (if (var? x) (str x) x))
     (flush)
     x)
  ([x]
     (echo "" x)))



(defn now [] (.getTime (new java.util.Date)))

(defn current-ts[]
  (new java.sql.Timestamp (now)))

(defn get-uuid
  []
  (UUID/randomUUID))

(defn generate-varification
  [verification-code]
(str global/VARIFICATION-URL-BASE verification-code))

(defn generate-forgotpassword-url
  [verification-code]
(str global/FORGOT-PASSWORD-URL-BASE verification-code))

(defn type-value
  [x]
  (str "(" (pr (type x)) ") "
       (with-out-str (pp/pprint x))))


(defmacro verify
  [pred x & xs]
  `(try (assert (~pred ~x ~@xs))
        true
        (catch AssertionError e#
          (throw (AssertionError.
                  (str/join
                   "\n"
                   (cons (.getMessage e#)
                         (map #(str "\nfound " (type-value %)) [~x ~@xs]))))))))


(defn verify-as
  [pred x]
  (verify pred x)
  x)


(defn dashify
  [x]
  (cond
    (string? x)  (apply str (replace {\_ \-} x))
    (symbol? x)  (-> (name x)
                     dashify
                     symbol)
    (keyword? x) (-> (name x)
                     dashify
                     keyword)
    :otherwise   x))


(defn dashify-map
  [m]
  (if (map? m)
    (let [ks (keys m)
          vs (vals m)]
      (zipmap (map dashify ks) vs))
    m))


(defn parse-int
  "Parse and return integer from given stringable, nil if cannot parse."
  [s]
  (try (Integer/parseInt (str s))
    (catch NumberFormatException e
      nil)))


(defn parse-boolean
  "Parse and return boolean from given stringable, nil if cannot parse."
  [s]
  (Boolean/parseBoolean (str s)))


(defn md5
  "Return MD-5 hash for given (stringable) `s`."
  [s]
  (let [st (str s)
        bs (.getBytes ^String st "UTF-8")
        md (MessageDigest/getInstance "MD5")]
    (.update ^MessageDigest md bs 0 (count st))
    (-> (BigInteger. 1 (.digest ^MessageDigest md))
        (.toString 16))))

(defn sendmail
  "This is to send mail to certain email, address using gmail"
  [^String to ^String sub ^String text]
  (mailaram/sendmail to (:from global/mail) sub text
     (:user global/mail) (:password global/mail)))