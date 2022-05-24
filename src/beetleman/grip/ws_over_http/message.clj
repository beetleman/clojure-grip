(ns beetleman.grip.ws-over-http.message
  (:require [clojure.string :as str]
            [jsonista.core :as json]))

(defn encode
  [{:keys [event content]
    :or   {content ""}}]
  (let [size (->> (.getBytes content)
                  count
                  (format "%x")
                  str/upper-case)]
    (str event " " size "\r\n" content "\r\n")))


(defn decode
  [s]
  (let [[event-with-size raw-content] (str/split s #"\r\n")
        [event size-hex]              (when event-with-size
                                        (str/split event-with-size #" "))
        size                          (when (seq size-hex)
                                        (Integer/parseInt size-hex 16))]
    (if (and size raw-content)
      {:event   event
       :content (->> (.getBytes raw-content)
                     (take size)
                     byte-array
                     (String.))}
      {:event event})))


(defn encode-json
  [data]
  (encode {:event   "TEXT"
           :content (json/write-value-as-string data)}))
