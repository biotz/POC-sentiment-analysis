(ns user
  (:require [nrepl.server :as nrepl]
            [cider.nrepl :refer (cider-nrepl-handler)]
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

(defn- delivery-fn [[path first-person-direction  second-person-direction  turn  ]   direction]
  (let [[x y] (if (= 1  turn)  first-person-direction second-person-direction)
        new-position (case  direction
                       "^" [x (inc y)]
                       "<" [(dec x) y]
                       ">" [(inc x) y]
                       "v" [x (dec y)])]

   (if  (= 1 turn)
     [(conj path new-position) new-position second-person-direction 2]
     [(conj path new-position) first-person-direction new-position  1])))



(defn pizza-delivery [starting-position  path]
  (reduce delivery-fn  [#{starting-position} starting-position  starting-position 1] path))



(pizza-delivery [2 4]  ["^" "<"])


(pizza-delivery  [2 4] ["^" "<" "v" ">"])


(pizza-delivery [2 4]  ["^" "<"  "v" ">"])




