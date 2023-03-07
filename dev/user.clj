(ns user
  (:require [nrepl.server :as nrepl]
            [cider.nrepl :refer (cider-nrepl-handler)]
            #_[clojure.reflect :refer [reflect]]
            [hashp.core]
            [clojure.tools.logging :as log]))

(log/info "starting repl")

(defonce nrepl-server (nrepl/start-server :handler cider-nrepl-handler))

(def jit requiring-resolve)

(binding [*out* (java.io.PrintWriter.
                  (java.io.FileOutputStream.
                    java.io.FileDescriptor/out) true)]
  (println "Staring nrepl server on"
           (str (clojure.string/join
                  "." (.getAddress  (.getInetAddress (:server-socket nrepl-server))))
                " port " (.getLocalPort (:server-socket nrepl-server)))))


(spit "./.nrepl-port" (:port nrepl-server))

(defn- delivery-fn [acc direction]
  (let [[path [x y]] acc
         new-position (case  direction
                        "^" [x (inc y)]
                        "<" [(dec x) y]
                        ">" [(inc x) y]
                        "v" [x (dec y)])]

    [(conj path new-position) new-position]))



(defn pizza-delivery [starting-position  path]
  (reduce delivery-fn  [#{ starting-position  } starting-position] path))



(pizza-delivery [2 4]  ["^" "<"])


(pizza-delivery  [2 4] ["^" "<" "v" ">"])


(pizza-delivery [2 4]  ["^" "<"  "v" ">"])




