(ns clojure-sources.query)


(def query-templates
  {:find-jbc-salt-by-username
   {:all   "SELECT jbc_salt FROM users WHERE username=?"}
   :find-signup
   {:all   "SELECT username, jbc_salt, password, fullname FROM signups WHERE signup_id=?"
    :pgsql "SELECT username, jbc_salt, password, fullname FROM signups WHERE signup_id=?::integer"}
   :find-user-by-username
   {:all   "SELECT user_id, fullname FROM users WHERE username=?"}
   :find-pwreset-exists?
   {:all   "SELECT user_id FROM pwreset WHERE pwreset_id=? AND verify_id=? AND visited_t IS NULL"
    :pgsql "SELECT user_id FROM pwreset WHERE pwreset_id=?::integer AND verify_id=? AND visited_t IS NULL"}
   :where-item-id
   {:all   "user_id=? AND item_id=?"
    :pgsql "user_id=?::integer AND item_id=?::integer"}
   :where-user-id
   {:all   "user_id=?"
    :pgsql "user_id=?::integer"}
   :where-pwreset
   {:all   "pwreset_id=? AND verify_id=?"
    :pgsql "pwreset_id=?::integer AND verify_id=?"}
   :find-user-by-cred
   {:all   "SELECT user_id, fullname FROM users WHERE username=? AND password=? and is_active"}
   :find-items
   {:all   "SELECT item_id, item_text, item_note, position, is_done, created_t, updated_t FROM items WHERE user_id=? ORDER BY position, updated_t DESC"}
   :find-one-item
   {:all   "SELECT item_id, item_text, item_note, is_done, created_t, updated_t FROM items WHERE user_id=? AND item_id=?"}
   :duplicate-item-text?
   {:all   "SELECT item_id FROM items WHERE user_id=? AND LOWER(item_text)=?"}})