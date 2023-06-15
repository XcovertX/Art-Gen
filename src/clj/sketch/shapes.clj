(ns sketch.shapes
  (:require [quil.core :refer :all]
            [sketch.structures :as sture]
            [clojure.java.shell :refer [sh]]
            [sketch.calculations :as calc])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])

  (:import [processing.core PShape PGraphics]))

(defn createLine
  "creates a node list containg 2 nodes"
  ([pos1 pos2]
   (vector
    (sture/buildNode {:x (:x pos1) :y (:y pos1)})
    (sture/buildNode {:x (:x pos2) :y (:y pos2)})))

  ([pos1 pos2 settings]
   (vector
    (sture/buildNode {:x (:x pos1) :y (:y pos1)} settings)
    (sture/buildNode {:x (:x pos2) :y (:y pos2)} settings)))

  ([pos1 pos2 settings data]
   (vector
    (sture/buildNode {:x (:x pos1) :y (:y pos1)} settings data)
    (sture/buildNode {:x (:x pos2) :y (:y pos2)} settings data))))

(defn createLinePath
  "builds a path that is made up of a line"
  [pos1 pos2]
  (sture/buildPath (createLine pos1 pos2)))

(defn addLineToPath
  "adds a line to the end of a given path"
  [path line]
  (assoc-in path [:nodes] (conj (:nodes path) (:nodes line))))

(defn createRectangle
  "builds a square or rectangle"
  ([length] 
   (reset! sture/nodeIDCounter 0) ;; temporary for testing
   [(sture/buildPath [(sture/buildNode {:x 0 :y length})
                      (sture/buildNode {:x length :y length})
                      (sture/buildNode {:x length :y 0})
                      (sture/buildNode {:x 0 :y 0})])])
  
  ([length width]
   (reset! sture/nodeIDCounter 0) ;; temporary for testing
   [(sture/buildPath [(sture/buildNode {:x 0 :y width})
                      (sture/buildNode {:x length :y width})
                      (sture/buildNode {:x length :y 0})
                      (sture/buildNode {:x 0 :y 0})])])
  
  ([length width center]
   (reset! sture/nodeIDCounter 0) ;; temporary for testing
   [(sture/buildPath (let [x-min (- (:x center) (/ length 2))
                           x-max (+ (:x center) (/ length 2))
                           y-min (- (:y center) (/ width 2))
                           y-max (+ (:y center) (/ width 2))]
                       [(sture/buildNode {:x x-min :y y-max})
                        (sture/buildNode {:x x-max :y y-max})
                        (sture/buildNode {:x x-max :y y-min})
                        (sture/buildNode {:x x-min :y y-min})]))])
  )

(defn adjustRectangle
  "adjusts the length and width of a rectangle" 
  [path length width]
  (let [new-path (atom path)
        nodes (:nodes path)
        center {:x (/ length 2) :y (/ width 2)}
        x-min (- (:x center) (/ length 2))
        x-max (+ (:x center) (/ length 2))
        y-min (- (:y center) (/ width 2))
        y-max (+ (:y center) (/ width 2))]
    ;; (println length width center x-min x-max y-min)
    (swap! new-path assoc-in [:nodes] [])
    (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path)
                                            (assoc-in (get nodes 0) [:position] (sture/getPosition {:x x-min :y y-max}))
                                            (assoc-in (get nodes 1) [:position] (sture/getPosition {:x x-max :y y-max}))
                                            (assoc-in (get nodes 2) [:position] (sture/getPosition {:x x-max :y y-min}))
                                            (assoc-in (get nodes 3) [:position] (sture/getPosition {:x x-min :y y-min}))))))
