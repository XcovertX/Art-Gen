(ns sketch.core
  (:require [quil.core :as q])
  (:require [sketch.dynamic :as dynamic])
  (:require [sketch.divider :as divider])
  (:gen-class))

(q/defsketch example
  :title "Sketch"
  :setup dynamic/setup
  ;; :update refresh
  :draw dynamic/draw
  :size [divider/window-height divider/window-width])