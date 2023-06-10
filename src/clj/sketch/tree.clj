(ns sketch.tree
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
                       :draw-nodes true
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

(defn injectSeeds
  "adds a given count of seeds randomly dispersed"
  [path seed-count]

  (let [new-path (atom path)
        nodes (:nodes path)
        node-first (get nodes 0)
        length (- (count nodes) 1)
        node-last (get nodes length)
        distance (grow/getDistance node-first node-last)
        seeds (vec (distinct (sort (vec (take seed-count (repeatedly #(rand-int distance)))))))
        settings (if (:uniform-node-settings? (:settings path))
                   (:settings path)
                   (:settings node-first))]
    (doseq [node-index (range (+ (count seeds) 2))]
      (when (and (not= node-index 0)
                 (not= node-index (+ (count seeds) 1)))
        (let [connected-nodes (grow/getConnectedNodes (:nodes @new-path) node-index (:is-closed @new-path))
              next-node (:next connected-nodes)
              previous-node (:prev connected-nodes)
              new-y (get (:pos previous-node) 1)
              new-x (get seeds (- node-index 1))
              new-node (grow/buildNode new-x new-y settings false false false false)]
          (swap! new-path assoc-in [:nodes] (grow/insert (:nodes @new-path) node-index new-node)))))
    @new-path))
      



(defn applyTreeGrowth
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

(defn seed-tree
  "initializes tree growth"
  [w h seed-count]
  (let [p-1 [(grow/createTriangle 50 50 (- w 50) 50 (/ w 2) (- h 50))]
        p-2 [(grow/addLinePath [0 (/ h 2)] [w (/ h 2)])]
        p-3 [(grow/createCirclePath 150 100 200 150 150 200 100 150)]
        p-4 [(grow/addLinePath [0 0] [w h])]
        path (injectSeeds (get p-2 0) seed-count)]
    [path]))
