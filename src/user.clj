(ns user
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]))


(defn handler
  [_request]
  (response/response "Hello!"))


(defonce server (atom nil))


(swap! server
  (fn [s]
    (when s
      (.stop s))
    (jetty/run-jetty #(handler %)
                     {:port  3000
                      :join? false})))
