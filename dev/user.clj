(ns user
  (:require [nrepl.server :as nrepl]
            [cider.nrepl :refer (cider-nrepl-handler)]
            [clojure.reflect :refer [reflect]]))
(defonce nrepl-server (nrepl/start-server :handler cider-nrepl-handler))

(defmacro res [s] `(requiring-resolve '~s))

(binding [*out* (java.io.PrintWriter.
                  (java.io.FileOutputStream.
                    java.io.FileDescriptor/out) true)]

  (println "Staring nrepl server on " (str (clojure.string/join  "."    (.getAddress  (.getInetAddress (:server-socket nrepl-server)))) ":" (.getLocalPort (:server-socket nrepl-server)))))

(spit "./.nrepl-port" (:port nrepl-server))








