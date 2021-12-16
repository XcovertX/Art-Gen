(ns sketch.runcore
  (:require [quil.core :as q])
  (:require [sketch.dynamic :as dynamic])
  (:gen-class))

(defn -main [& args]
  (q/sketch
   :title "Big Image"
   :setup dynamic/setup
   :draw dynamic/draw
   :size [10800 10800]
   :features [:exit-on-close]))