(ns sketch.core
  (:require [quil.core :as q])
  (:require [sketch.dynamic :as dynamic])
  (:gen-class))

(q/defsketch example
  :title "Sketch"
  :setup dynamic/setup
  :draw dynamic/draw
  :size [dynamic/window-height dynamic/window-width])

(defn refresh []
  (use :reload 'sketch.dynamic)
  (.loop example))

(vec [1234 23 4])

