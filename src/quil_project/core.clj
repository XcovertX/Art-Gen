(ns quil-project.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn setup []
  ; Set frame rate to 30 frames per second.
  (q/frame-rate 1)
  (q/background 200))

(defn draw[]
  (q/stroke (q/random 255))
  (q/stroke-weight (q/random 10))
  (q/fill (q/random 255))

  (let[diam (q/random 100)
       x (q/random (q/width))
       y (q/random (q/height))]
   (q/ellipse x y diam diam)))


(q/defsketch example
  :title "Tons of cirlces"
  :settings #(q/smooth 2)
  :size [1000 500]
  ; setup function called only once, during sketch initialization.
  :setup setup
  ; update-state is called on each iteration before draw-state.
  
  :draw draw)

(defn -main
  [& args])

