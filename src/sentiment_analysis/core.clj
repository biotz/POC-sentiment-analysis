(ns sentiment-analysis.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.params :as params]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            [reitit.ring.coercion :as coercion]
            [reitit.ring :as ring]
            [libpython-clj2.python :as py]
            [libpython-clj2.require :refer [require-python]]) 
  (:gen-class))

(require-python '[pysentimiento :as sentiment])

(def conda (System/getenv  "CONDA_BASE_PATH"))

(def | (System/getProperty  "file.separator"))

(defn initialize-python- []
  (let [delayed-initialize (delay (py/initialize! ; Python executable
                                                  :python-executable   (str conda  | "bin" | "python")
                                                  ; Python Library
                                                  :library-path (str conda  | "bin" | "python")))]))


(def analyzer  (delay (sentiment/create_analyzer :task "sentiment" :lang "es")))


(def routes
 [["/health" {:get {:handler (fn [& x ] {:status 200})}}] 
  ["/sentiment" {:post
                 {:handler 
                  (fn [{{:keys [data]} :body-params :as req}]
                    {:status 200
                     :body {:sentiment  req #_(py/py. @analyzer predict data)}})}}]

  ["/plain" ["/plus" {:get
                      {:handler
                       (fn [{{:strs [x y]} :query-params :as req}]
                         {:status 200
                          :body {:total (+ (Long/parseLong x) (Long/parseLong y))}})}
                      :post
                      {:handler 
                       (fn [{{:keys [x y]} :body-params}]
                         {:status 200
                          :body {:total (+ x y)}})}}]]])
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

;; (.stop server)
(comment
  (def server (start)))
