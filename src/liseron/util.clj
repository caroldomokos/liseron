(ns liseron.util
  (:require [cheshire.core :refer :all])
  (:require [clj-http.client :as client])
  (require [clojure.walk])
  (require [clojure.string :refer [blank?]]))

(def your_api_key
  (System/getenv "YOUR_API_KEY"))
(def base_url
 "https://www.googleapis.com/youtube/v3/")
(defn build_url
 ([type part id] (hash-map :url (str base_url type "?part=" part "&id=" id "&key=" your_api_key ), :id id))
 ([type part id id_type] (hash-map :url (str base_url type "?part=" part "&" id_type "=" id "&key=" your_api_key ), :id id))
 ([type part id id_type pageToken] (hash-map :url (str base_url type "?part=" part "&" id_type "=" id "&pageToken=" pageToken "&key=" your_api_key ), :id id)))
(defn get_key-value [url keyword]
  (-> url
        client/get
        :body
        cheshire.core/parse-string
        clojure.walk/keywordize-keys
        keyword))

(defn pList_url
 ([id] (build_url "playlistItems" "contentDetails" id "playlistId"))
 ([id token] (build_url "playlistItems" "contentDetails" id "playlistId" token)))
(defn ch_url [id]
 (build_url "channels" "contentDetails" id))
(defn vList [url]
 (map :id (get_key-value url :items)))
(defn token [url]
 (get_key-value url :nextPageToken))
(defn get_upload_id [id]
  (-> id
        ch_url
        :url
        (get_key-value :items)
        first
        :contentDetails
        :relatedPlaylists
        :uploads))

(defn bvl
 ([id] (bvl id (:url (pList_url id)) (vList (:url (pList_url id)))))
 ([id url list]
     (if (blank? (token url))
         list
     (bvl id (token url) (:url (pList_url id (token url))) (concat list (vList (:url (pList_url id (token url))))))))
 ([id pageToken url list]
     (if (blank? (token url))
         (do (println "!!!!!!!! Page Token: "pageToken "\n")
              list)
         (recur id (token url) (:url (pList_url id (token url))) (concat list (vList (:url (pList_url id (token url)))))))))


(defn load_library [path]
  (parse-stream (clojure.java.io/reader path)))

(def find_playlist_id [name cid]
  (
