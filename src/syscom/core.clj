(ns syscom.core
  (:require [clojure.java.jdbc :as sql]
            [clojure.string :as string]
            [clojure.pprint :refer (pprint)]
            [clojure.edn]))

(def calvin "tiger")
(def config (:dev (clojure.edn/read-string (slurp "config.edn"))))

(def dbp 
  (let [config config
        {:keys [db-host db-port db-name user password]} config]
    {:classname "org.postgresql.Driver" ; must be in classpath
     :subprotocol "postgresql"
     :subname (str "//" db-host ":" db-port "/" db-name)
     ; Any additional keys are passed to the driver
     ; as driver-specific properties.
     :user user
     :password password}))

(defrecord Tiger [name age move])

(defrecord Tiger1 [name age move])

(defprotocol Database
  (get-db [s])
  (edit-db [s])
  (insert-db [s])
  (delete-db [s]))

(defrecord Couch [db m]
  Database
  (get-db [this] db)
  (edit-db [this] db)
  (insert-db [this] db)
  (delete-db [this] db))

(defrecord Postgres [db m]
  Database
  (get-db [this] (sql/query db ["select * from shiee"]))
  (edit-db [this] db)
  (insert-db [this] db)
  (delete-db [this] db))

(defn suck1
  [db m]
  (get-db (->Postgres db m)))

(defn maxc
  ([[a b & coll]]
     (if (> a b)
       (maxc a (rest coll))
       (maxc b (rest coll))))
  ([a coll]
     (if (seq coll)
       (if (> a (first coll))
         (maxc a (rest coll))
         (maxc (first coll) (rest coll)))
       a)))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
