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
    (sture/buildNode {:x (:x pos1) :y (:y pos2)})
    (sture/buildNode {:x (:x pos1) :y (:y pos2)})))

  ([pos1 pos2 settings]
   (vector
    (sture/buildNode {:x (:x pos1) :y (:y pos2)} settings)
    (sture/buildNode {:x (:x pos1) :y (:y pos2)} settings)))

  ([pos1 pos2 settings data]
   (vector
    (sture/buildNode {:x (:x pos1) :y (:y pos2)} settings data)
    (sture/buildNode {:x (:x pos1) :y (:y pos2)} settings data))))

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
   [(sture/buildPath (into [] (concat
                              (createLine {:x 0 :y length} {:x length :y length})
                              (createLine {:x length :y length} {:x length :y 0})
                              (createLine {:x length :y 0} {:x 0 :y 0})
                              (createLine {:x 0 :y 0} {:x 0 :y length}))))])
  
  ([length width]
   ())
  
  ([length width LCposition]
   ())
  )
