(ns liseron.core
  (:require [cheshire.core :refer :all])
  (:require [clj-http.client :as client])
  (require [clojure.walk])
  (require [clojure.string :refer [blank?]])
  (require [liseron.util :refer :all])
  (:gen-class))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
