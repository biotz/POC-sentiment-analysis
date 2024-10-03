(ns sentiment-analysis.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.params :as params]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            [reitit.ring.coercion :as coercion]
            [reitit.ring :as ring]
            [libpython-clj2.python :as py])
  (:gen-class))

(py/initialize!)
#_(initialize! ; Python executable
              :python-executable "C:\\Users\\USER\\AppData\\Local\\Continuum\\anaconda3\\python.exe"
              ; Python Library
              :library-path "C:\\Users\\USER\\AppData\\Local\\Continuum\\anaconda3\\python37.dll"
              ; Anacondas PATH environment to load native dlls of modules (numpy, etc.)
              :windows-anaconda-activate-bat "C:\\Users\\USER\\AppData\\Local\\Continuum\\anaconda3\\Scripts\\activate.bat")

:ok


(def routes
 [["/health" {:get {:handler (fn [& x ] {:status 200})}}] 
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
