(ns beetleman.grip.ws-over-http.message-test
  (:require [beetleman.grip.ws-over-http.message :as sut]
            [clojure.test :as t]))


(t/deftest decode-encode-test
  (t/testing "event `OPEN`"
             (let [event {:event "OPEN"}]
               (t/is (= (-> event
                            sut/encode
                            sut/decode)
                        event))))
  (t/testing "event `TEXT`"
             (let [event {:event "TEXT" :content "I 💜 🐐"}]
               (t/is (= (-> event
                            sut/encode
                            sut/decode)
                        event)))))
