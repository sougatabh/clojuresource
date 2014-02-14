(ns clojuresource.web
  (:require [basil.public   :as basil]
             [basil.lib      :as balib]
             [clavatar.core  :as clav]
             [clojure.string :as str]
             [clojure.pprint :as pp]
             [ring.util.response   :as response]
             [taoensso.timbre      :as log]
             [clojuresource.utils      :as utils]
             [clojuresource.global      :as global]))



(def tpl (basil/make-group-from-classpath :prefix "templates/"))

(defn map-or-nil?
  [m]
  (or (nil? m) (map? m)))

(defn redirect
  [app-url]
  (response/redirect (str (:context global/*http-request*) app-url)))



(defn merge-concat
  [m1 m2] {:pre [(utils/verify map-or-nil? m1)
                 (utils/verify map-or-nil? m2)]}
  (let [ks (set (concat (keys m1) (keys m2)))]
    (reduce (fn [m k]
              (let [v1 (get m1 k)
                    v2 (get m2 k)]
                (merge m {k (cond (and (nil? v1) (nil? v2)) nil
                                  (and (map? v1) (map? v2)) (merge-concat v1 v2)
                                  (and (map? v1) (nil? v2)) v1
                                  (and (nil? v1) (map? v2)) v2
                                  (and (coll? v1) (coll? v2)) (concat v1 v2)
                                  (and (coll? v1) (nil?  v2)) v1
                                  (and (nil?  v1) (coll? v2)) v2
                                  :otherwise
                                  (throw
                                   (IllegalArgumentException.
                                    (str "Expected values to be map/coll, found "
                                         (utils/type-value v1)
                                         " and "
                                         (utils/type-value v2)))))})))
            {} ks)))

(defn flash-assoc
  ([category message]
     (flash-assoc category message
                    (select-keys global/*http-request* [:flash])))
  ([category message m] {:pre [(not (nil? category))
                               (string? message)
                               (or (nil? m) (map? m))]}
     (merge-with merge-concat m {:flash {category [message]}})))


(def flash-success (partial flash-assoc :success))
(def flash-error   (partial flash-assoc :error))
(def flash-notice  (partial flash-assoc :notice))


(defn session-get
  ([k]
     (get (:session global/*http-request*) k))
  ([k default]
     (get (:session global/*http-request*) k default)))


(defn session-assoc
  [k v & kvs] {:pre [(even? (count kvs))]}
  (merge-with merge
              (select-keys global/*http-request* [:session])
              {:session (apply array-map k v kvs)}))


(defn session-dissoc
  [k & ks]
  (merge (select-keys global/*http-request* [:session])
         {:session (apply dissoc (:session global/*http-request*) k ks)}))

(defn naked-render
  [^String page-name]
  (basil/render-by-name tpl page-name [{:message "" }]))

(defn public-render
  [page-name pagedata]
  (basil/render-by-name tpl "public-base.html" [{:body page-name :pagedata pagedata}]))

(defn private-render
  [page-name pagedata]
   (basil/render-by-name tpl "private-base.html" [{:body page-name :pagedata pagedata}]))

(defn private-render-submit-page
  [page-name sidebar errormsg licenses categories]
 (basil/render-by-name tpl "private-base.html" [{:body page-name :sidebar sidebar :errormsg errormsg :licenses licenses :categories categories}]))

(defn email-verification 
  [fullname url] 
  (println "The Full Name and URL are here ," fullname "  " url)
  (basil/render-by-name tpl "verification-template.txt" [{:fullname fullname :url url}]))

