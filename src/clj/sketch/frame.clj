(ns sketch.frame
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]]
            [sketch.calculations :as calc]
            [sketch.grow :as grow]
            [sketch.structures :as sture])
(:use [incanter.core :only [$=]])
(:use [clojure.math.combinatorics :only [combinations cartesian-product]])
(:use [clojure.pprint])
(:use [clojure.set :only [union]])
(:use [clojure.contrib.map-utils :only [deep-merge-with]])
(:import [org.apache.commons.math3.distribution ParetoDistribution])
(:import [processing.core PShape PGraphics]))

(defn applyGrowth
  [paths width height]
  (let [new-paths (atom paths)]
    (doseq [path-index (range (count @new-paths))]
      (doseq [node-index (range (count (:nodes (get @new-paths path-index))))]

        (swap! new-paths assoc-in [path-index :nodes node-index] (applyBrownianMotion (get (:nodes (get @new-paths path-index)) node-index))))

      (swap! new-paths assoc-in [path-index] (splitEdges (get @new-paths path-index))))

    @new-paths))

(defn init-growth 
  "initializes growth"
  [w h]
  (let [p-1 [(grow/createTriangle 50 50 (- w 50) 50 (/ w 2) (- h 50))]
        p-2 [(grow/addLinePath [0 (/ h 2)] [w (/ h 2)])]
        p-3 [(grow/createCirclePath 150 100 200 150 150 200 100 150)]
        p-4 [(grow/addLinePath [0 0] [w h])]
        paths (applyGrowth p-4 w h)]
    paths))