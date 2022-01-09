(ns sketch.core
  (:require [quil.core :as q])
  (:require [sketch.dynamic :as dynamic])
  (:require [sketch.divider :as divider])
  (:gen-class))

(q/defsketch example
  :title "Sketch"
  :setup dynamic/setup
  :draw dynamic/draw
  :size [dynamic/window-width dynamic/window-height])

(defn refresh []
  (use :reload 'sketch.dynamic)
  (.loop example))

