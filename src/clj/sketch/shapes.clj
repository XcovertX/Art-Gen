(ns sketch.shapes
  (:require [quil.core :refer :all]
            [sketch.path :as path]
            [clojure.java.shell :refer [sh]]
            [sketch.calculations :as calc])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])
  (:import [processing.core PShape PGraphics]))

(defn addLineToPath
  "adds a line to the end of a given path"
  [path line]
  (assoc-in path [:nodes] (conj (:nodes path) (:nodes line))))

(defn createRectangle
  "builds a square or rectangle"
  ([length]
   (reset! path/nodeIDCounter 0) ;; temporary for testing 
   (let [path (path/buildPath
               [(path/buildNode {:x 0 :y length})
                (path/buildNode {:x length :y length})
                (path/buildNode {:x length :y 0})
                (path/buildNode {:x 0 :y 0})])
         path (update-in path [:data] assoc :type "shape" :is-closed true)]
     path))

  ([length width]
   (reset! path/nodeIDCounter 0) ;; temporary for testing
   (let [path (path/buildPath
               [(path/buildNode {:x 0 :y width})
                (path/buildNode {:x length :y width})
                (path/buildNode {:x length :y 0})
                (path/buildNode {:x 0 :y 0})])
         path (update-in path [:data] assoc :type "shape" :is-closed true)]
     path))

  ([length width center]
   (reset! path/nodeIDCounter 0) ;; temporary for testing
   (let [x-min (- (:x center) (/ length 2))
         x-max (+ (:x center) (/ length 2))
         y-min (- (:y center) (/ width 2))
         y-max (+ (:y center) (/ width 2))
         path (path/buildPath
               [(path/buildNode {:x x-min :y y-max})
                (path/buildNode {:x x-max :y y-max})
                (path/buildNode {:x x-max :y y-min})
                (path/buildNode {:x x-min :y y-min})])
         path (update-in path [:data] assoc :type "shape" :is-closed true :age 0)]
     path)))

(defn adjustRectangle
  "adjusts the length and width of a rectangle"
  [path length width center]
  (let [new-path (atom path)
        nodes (:nodes path)
        x-min (- (:x center) (/ length 2))
        x-max (+ (:x center) (/ length 2))
        y-min (- (:y center) (/ width 2))
        y-max (+ (:y center) (/ width 2))]
    (swap! new-path assoc-in [:nodes] [])
    (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path)
                                            (assoc-in (get nodes 0) [:position] (path/getPosition {:x x-min :y y-max}))
                                            (assoc-in (get nodes 1) [:position] (path/getPosition {:x x-max :y y-max}))
                                            (assoc-in (get nodes 2) [:position] (path/getPosition {:x x-max :y y-min}))
                                            (assoc-in (get nodes 3) [:position] (path/getPosition {:x x-min :y y-min}))))))


