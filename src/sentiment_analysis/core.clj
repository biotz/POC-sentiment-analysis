(ns sentiment-analysis.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.params :as params]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            [reitit.ring.coercion :as coercion]
            [reitit.ring :as ring]
            [libpython-clj2.python :as py]
            [libpython-clj2.require :refer [require-python]] 
            [clojure.set :refer [rename-keys]])
  (:gen-class))

(require-python '[pysentimiento :as sentiment])

(def conda (System/getenv  "CONDA_BASE_PATH"))

(def | (System/getProperty  "file.separator"))

(defn initialize-python- []
  (let [delayed-initialize (delay (py/initialize! ; Python executable
                                                  :python-executable   (str conda  | "bin" | "python")
                                                  ; Python Library
                                                  :library-path (str conda  | "bin" | "python")))]))

(def analyzer  (delay 
                 (do
                  (println "Initialize sentiment analyzer")
                  (sentiment/create_analyzer :task "sentiment" :lang "es"))))

(def sentiment-values {"NEG" "Negativo" "POS" "Positivo"  "NEU" "Neutro"})

(def routes
 [["/health" {:get {:handler (fn [& _] {:status 200})}}] 
  ["/sentiment" {:post
                 {:handler 
                  (fn [{{:strs [data] :as params} :params :as req}]
                    {:status 200
                     :body (let [output (py/py. @analyzer predict data)]
                             {:sentimiento (sentiment-values (py/get-attr output :output)) 
                              :probabilidades (update-vals (rename-keys  (into {} (py/get-attr output :probas)) sentiment-values) #(format "%.1f%s" (* 100 %) \%))})})}}]])
(def app
 (ring/ring-handler
   (ring/router
     [routes]
     {:data {:muuntaja m/instance
             :middleware [params/wrap-params
                            muuntaja/format-middleware
                            coercion/coerce-exceptions-middleware
                            coercion/coerce-request-middleware
                            coercion/coerce-response-middleware]}})
   (ring/create-default-handler)))


(defn start []
  (let [server (jetty/run-jetty #'app {:port 3000, :join? false})]
   (println "server running in port 3000")
   server))

(defn -main [& _args]
   (start))

#_ (.stop server)
(comment
  (def server (start)))
