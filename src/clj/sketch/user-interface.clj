(ns sketch.user-interface
  (:require [quil.core :refer :all]))

(defn setup []
  ;; (dosync (ref-set img (load-image img-url)))
  (color-mode :hsb)
  (stroke 360 0 360)
  (stroke-weight 2)
  (background 0 0 0)
  ;; (no-loop)
  )