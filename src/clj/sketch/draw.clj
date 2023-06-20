(ns sketch.draw
  (:require [quil.core :refer :all]
            [sketch.path :as path])
  (:use [clojure.pprint]))

(defn colorSpectrum
  "changes the color of node output to RGB spectrum R: @ 0 V: @ length of vector"
  [nodes]
  (let [multiplier (/ 256 (count nodes))]
    (mapv #(round (* multiplier %)) (range (count nodes)))))

(defn drawPath
  "draws the path according to the current settings from left to right"
  [path]
  (let [nodes (:nodes path)
        node-color (when (:bug-finder-mode? (:settings path))
                     (colorSpectrum nodes))]
    (doseq [node-index (range (count nodes))
            :let [connected-nodes (path/getConnectedNodes nodes node-index (:is-closed (:data path)))
                  node (get nodes node-index)
                  next (:next connected-nodes)
                  prev (:prev connected-nodes)
                  x (:x (:position node))
                  y (:y (:position node))
                  next-x (:x (:position next))
                  next-y (:y (:position next))
                  prev-x (:x (:position prev))
                  prev-y (:y (:position prev))]]
      (when (not (:is-fixed (:data path)))
        (when (:draw-edges (:settings path))
          (when (not= next nil)
            (line x y next-x next-y)))
        (when (:bug-finder-mode? (:settings path))
          (stroke (get node-color node-index) 360 360))
        (when (:draw-nodes (:settings path))
          (ellipse x y 2 2))
        (when (:draw-fixed-nodes (:settings path))
          (when (:is-fixed (:data node))
            (stroke 255 0 255)
            (ellipse x y 2 2)
            (stroke (get node-color node-index) 360 360)))
        (when (:draw-all-random-injections? (:settings path))
          (when (:is-random (:data node))
            (ellipse x y 2 2)))
        (when (:draw-new-random-injections? (:settings path))
          (when (and (:is-random (:data node)) (= (:age node) 0))
            (stroke 255 0 255)
            (ellipse x y 2 2)
            (stroke (get node-color node-index) 360 360)))))))

(defn printPosition
  [p]
  (println "Position:" (map
                        (fn [path]
                          (map
                           (fn [node] (:position node))
                           (:nodes path)))
                        p)))

(defn printNextPosition
  [p]
  (println "Next Position:" (map
                             (fn [path]
                               (map
                                (fn [node] (:next-position (:data node)))
                                (:nodes path)))
                             p)))