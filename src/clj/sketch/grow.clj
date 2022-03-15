(ns sketch.grow
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]]
            [sketch.calculations :as calc]
            [sketch.growth-path :as path])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])
  (:import [processing.core PShape PGraphics]))

;; ------------ Growth Tools -----------------
(def path-map (atom {:paths []}))
(defrecord Path [nodes
                 settings
                 is-closed
                 bounds
                 fill-color
                 stroke-color])

(def node-map (atom {:nodes [] :is-closed false}))
(defrecord Node [x y is-fixed])

(def default-settings (vector
               :min-distance 1
               :max-distance 5
               :repultion-radius 10
               :attraction-force 0.2
               :repulsion-force 0.5
               :allignment-force 0.45
               :node-injection-interval 10
               :brownian-motion-range 0.01))

(defn addPath
  "adds a given path to path-map"
  [path]
  (swap! path-map assoc-in [:paths] (conj (:paths @path-map) path)))

(defn addPaths
  "adds a given number of paths"
  [paths]
  (doseq [path paths]
    (addPath path)))

(defn buildPath
  "builds a path"
  [nodes settings is-closed bounds fill-color stoke-color]
  (Path.
   nodes settings is-closed bounds fill-color stoke-color))

(defn prunePaths
  "removes paths that are too small"
  []
  (filter (fn [x]
            (> (count (:nodes x)) 1)) (:paths @path-map)))

(defn addNode
  "atomically adds a node to node-map"
  [node]
  (swap! node-map assoc-in [:nodes] (conj (@node-map :nodes) node)))

(defn buildNode
  "constructs a new node"
  [x y]
  (Node.
   x y (:min-distance default-settings) (:repultion-radius default-settings)
   false [:x nil :y nil]))

(defn getConnectedNodes
  "retrieves all nodes connected to a given node"
  [index]
  (let [length (count (@node-map :nodes))
        previous-node (if (and (= index 0) (@node-map :is-closed))
                        (get (@node-map :nodes) (- length 1))
                        (if (>= index 1)
                          (get (@node-map :nodes) (- index 1))))
        next-node (if (and (= index (- length 1)) (@node-map :is-closed))
                    (get (@node-map :nodes) 0)
                    (if (<= index (- length 1))
                      (get (@node-map :nodes) (+ index 1))))]
    (vector :prev previous-node :next next-node)))

(defn getDistance 
  [node-A node-B]
  (let [xa (:x node-A)
        ya (:y node-A)
        xb (:x node-B)
        yb (:y node-B)]
    (sqrt (+ (* (- xa xb) (- xa xb)) (* (- ya yb) (- ya yb))))))

(defn insert 
  "inserts node into a specific index"
  [vec pos item]
  (apply conj (subvec vec 0 pos) item (subvec vec pos)))

(defn getMidpointNode
  "retrieves the middle node between two nodes"
  [node-A node-B]
  (let [newx (/ (+ (:x node-A) (:x node-B)) 2)
        newy (/ (+ (:y node-A) (:y node-B)) 2)]
    (Node.
     newx newy default-settings false)))

(defn splitEdges
  "searches for edges that are too long and splits them"
  [path]
  (let [new-path (atom path)]
   (doseq [node (:nodes path)]
    (let [index (.indexOf @new-path node)
          length (count @new-path)
          con-nodes (getConnectedNodes index)
          prev-node (:prev con-nodes)
          next-node (:next con-nodes)
          distance (getDistance node prev-node)]
      (if (and (not= next-node nil) (>= distance (:max-distance (:settings path))))
        (let [midpoint-node (getMidpointNode node prev-node)]
          (if (= index 0)
            (swap! new-path (insert @new-path length midpoint-node))
            (swap! new-path (insert @new-path index midpoint-node)))))))))


(defn knn
  "nearest neighbor search"
  [x k data]
  (let [cmp (fn [u v] (< (distance x u) (distance x v)))
        rdr (fn [ys y] (take k (sort cmp (cons y ys))))]
    (->> (keys data)
         (reduce rdr [])
         (map data)
         frequencies
         (apply max-key val)
         first)))

(defn applyBrownianMotion
  "simulates minor motion"
  [node]
  (let [x (:x node) y (:y node)
        newx (+ x (random (- 0 (/ (:brownian-motion-range default-settings) 2))
                          (/ (:brownian-motion-range default-settings) 2)))
        newy (+ y (random (- 0 (/ (:brownian-motion-range default-settings) 2))
                          (/ (:brownian-motion-range default-settings) 2)))]
    (vector :x x :y y)))

(defn applyAttraction
  "moves node closed to its connected nodes"
  [index]
  (let [connected-nodes (getConnectedNodes index)]
    (if (and
         (not= (connected-nodes :next) nil)
         (not (:is-fixed (index (:nodes @node-map)))))
      (let [distance (index (:nodes @node-map))

            least-min-distance (Math/min
                                (:min-distance (index (:nodes @node-map)))
                                (:min-distance (:prev connected-nodes)))]
        (if (> distance least-min-distance)
          (let [x (lerp (:x (:next-pos (index (:nodes @node-map))))
                        (:x (:prev connected-nodes))
                        (:attraction-force default-settings))
                y (lerp (:y (:next-pos (index (:nodes @node-map))))
                        (:y (:prev connected-nodes))
                        (:attraction-force default-settings))]
            (vector :x x :y y)))))))

(defn applyRepulsion
  ""
  [index r-tree]
  (let [x (:x (index (:nodes @node-map)))
        y (:y (index (:nodes @node-map)))
        ]))

(defn iterate-tree
  "iterates one whole step of the cell growth"
  [r-tree]
  (doseq [n (range (count (@node-map :nodes)))]
    (let [index (.indexOf n)
          bm-node (update-in n [:x :y] (applyBrownianMotion n))
          attract-node (update-in (:next-pos bm-node) [:x :y] (applyAttraction index))])))
