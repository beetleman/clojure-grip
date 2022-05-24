(ns user
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :as response]
            [vlaaad.reveal :as r]
            [clojure.string :as str]
            [beetleman.grip.ws-over-http.message :as message]))


;; impl

(def grip-hold "Grip-hold")
(def grip-channel "Grip-Channel")
(def grip-timeout "Grip-Timeout")
(def grip-keep-alive "Grip-Keep-Alive")

(defn open?
  [request]
  (str/starts-with? (:body request) "OPEN\r\n"))

;; dev

(comment
 (let [ui (r/ui)]
   (add-tap ui))
)



(defn handler
  [request]
  (let [request (update request :body slurp)]
    (tap> request)
    (tap> (open? request))
    (if (open? request)
      (-> (response/response "OPEN\r\n")
          (response/content-type "application/websocket-events")
          (response/header grip-hold "response")
          (response/header grip-channel "1"))
      (-> (response/response (message/encode-json {:message "hello"}))
          (response/content-type "application/websocket-events")))))


(defonce server (atom nil))


(swap! server
  (fn [s]
    (when s
      (.stop s))
    (jetty/run-jetty #(handler %)
                     {:port  3000
                      :join? false})))
