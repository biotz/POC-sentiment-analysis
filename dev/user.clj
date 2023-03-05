(ns user
  (:require [nrepl.server :as nrepl]
            [cider.nrepl :refer (cider-nrepl-handler)]
            #_[clojure.reflect :refer [reflect]]
            #_[hashp.core]
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






