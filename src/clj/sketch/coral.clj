(ns sketch.coral
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]]
            [sketch.calculations :as calc]
            [sketch.grow :as grow])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])
  (:import [processing.core PShape PGraphics]))

(def path-map (atom {:paths []}))
(defrecord Path [nodes
                 settings
                 is-closed
                 bounds
                 brownian
                 alignment
                 node-injection-interval
                 is-fixed
                 age
                 is-mature
                 is-divide-ready])

(def node-map (atom {:nodes []}))
(defrecord Node [pos settings data lifespan])
(defrecord Data [is-fixed is-end to-remove is-random velocity next-position])

(def default-settings (hash-map
                       :min-distance 2.1
                       :max-distance 4.5
                       :repulsion-radius 6.5
                       :max-velocity 0.01
                       :attraction-force 1
                       :repulsion-force 40
                       :allignment-force 0.3
                       :node-injection-interval 1
                       :brownian-motion-range 0.05
                       :fill-color nil
                       :stroke-color nil
                       :draw-edges true
                       :draw-nodes false
                       :draw-fixed-nodes false
                       :draw-all-random-injections? false
                       :bug-finder-mode? true
                       :uniform-node-settings? false
                       :hardend? false))

(def path-settings (hash-map
                    :min-distance 3
                    :max-distance 6
                    :repulsion-radius 7
                    :max-velocity 0.01
                    :attraction-force 0.01
                    :repulsion-force 20
                    :allignment-force 0.001
                    :node-injection-interval 10
                    :brownian-motion-range 0.25
                    :fill-color nil
                    :stroke-color nil
                    :draw-edges true
                    :draw-nodes false
                    :draw-fixed-nodes true
                    :draw-all-random-injections? false
                    :draw-new-random-injections? false
                    :bug-finder-mode? true
                    :uniform-node-settings? false
                    :hardend? false))

;; ------------ Primary Growth Functions -----------------

(defn applyCoralGrowth
  [paths width height]
  (let [new-paths (atom paths)]
    (doseq [path-index (range (count @new-paths))] 
      (doseq [node-index (range (count (:nodes (get @new-paths path-index))))]
        
        (swap! new-paths assoc-in [path-index :nodes node-index] (grow/applyBrownianMotion (get (:nodes (get @new-paths path-index)) node-index)))

        (swap! new-paths assoc-in [path-index :nodes node-index] (grow/applyAttraction (get @new-paths path-index) node-index))

        (swap! new-paths assoc-in [path-index :nodes node-index] (grow/applyRepulsion @new-paths path-index node-index))

        (swap! new-paths assoc-in [path-index :nodes node-index] (grow/applyAlignment (get @new-paths path-index) node-index))

        (swap! new-paths assoc-in [path-index :nodes node-index] (grow/applyBounds-2 (get (:nodes (get @new-paths path-index)) node-index) width height))

        (swap! new-paths assoc-in [path-index :nodes node-index] (grow/grow (get (:nodes (get @new-paths path-index)) node-index))))

       (swap! new-paths assoc-in [path-index] (grow/splitEdges (get @new-paths path-index)))

       (swap! new-paths assoc-in [path-index] (grow/pruneNodes (get @new-paths path-index)))

       (swap! new-paths assoc-in [path-index :nodes] (grow/removeFixed (:nodes (get @new-paths path-index))))

       (when (> (rand-int 100) 50)
         (swap! new-paths assoc-in [path-index] (grow/injectRandomNodeByCurvature (get @new-paths path-index))))

       (swap! new-paths update-in [path-index :age] inc))
    
    @new-paths))

(defn seed-coral
  "initializes coral growth"
  [w h]
  (let [p-1 [(grow/createTriangle 50 50 (- w 50) 50 (/ w 2) (- h 50))]
        p-2 [(grow/addLinePath [0 (/ h 2)] [w (/ h 2)])]
        p-3 [(grow/createCirclePath 150 100 200 150 150 200 100 150)]
        p-4 [(grow/addLinePath [0 0] [w h])]
        paths (applyCoralGrowth p-4 w h)]
    paths))



