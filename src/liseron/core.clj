(ns liseron.core
  (:require [cheshire.core :refer :all])
  (:require [clj-http.client :as client])
  (:gen-class))

(def your_api_key
  (System/getenv "YOUR_API_KEY"))

(def channels_url
  "https://www.googleapis.com/youtube/v3/channels?part=contentDetails&id=")

(def get_upload [cid]
  (-> (str channels_url your_api_key)
      (client/get )
      (:body )
      

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
