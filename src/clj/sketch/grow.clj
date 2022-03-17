(ns sketch.grow
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]]
            [sketch.calculations :as calc]
            [sketch.r-tree :as tree])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])
  (:import [processing.core PShape PGraphics]))

;; ------------ Growth Tools -----------------

(def tree (atom []))

(def path-map (atom {:paths []}))
(defrecord Path [nodes
                 settings
                 is-closed
                 bounds
                 fill-color
                 stroke-color])

(def node-map (atom {:nodes [] :is-closed false}))
;; (defrecord Node [x y is-fixed])
(defrecord Data [is-fixed velocity next-position settings])

(def default-settings (vector
                       :min-distance 1
                       :max-distance 5
                       :repulsion-radius 10
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
  [paths]
  (filter (fn [x]
            (> (count (:nodes x)) 1)) paths))

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
  [nodes index]
  (let [length (count nodes)
        previous-node (if (and (= index 0) (:isClosed nodes))
                        (get nodes (- length 1))
                        (if (>= index 1)
                          (get nodes (- index 1))))
        next-node (if (and (= index (- length 1)) (:is-closed nodes))
                    (get nodes 0)
                    (if (<= index (- length 1))
                      (get nodes (+ index 1))))]
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
            connected-nodes (getConnectedNodes index (:nodes @new-path))
            prev-node (:prev connected-nodes)
            next-node (:next connected-nodes)
            distance (getDistance node prev-node)]
        (if (and (not= next-node nil) (>= distance (:max-distance (:settings path))))
          (let [midpoint-node (getMidpointNode node prev-node)]
            (if (= index 0)
              (swap! new-path (insert @new-path length midpoint-node))
              (swap! new-path (insert @new-path index midpoint-node)))))))
    @new-path))



(defn knn
  "nearest neighbor search"
  [x k data]
  (let [cmp (fn [u v] (< (getDistance x u) (getDistance x v)))
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
    (update-in node [:x :y] [newx newy])))

(defn applyAttraction
  "moves node closer to its connected nodes"
  [nodes index]
  (let [node (index nodes)
        connected-nodes (getConnectedNodes nodes index)]
    (if (and
         (not= (:next connected-nodes) nil)
         (not (:is-fixed node)))
      (let [distance (index nodes)
            least-min-distance (Math/min
                                (:min-distance node)
                                (:min-distance (:prev connected-nodes)))]
        (if (> distance least-min-distance)
          (let [x (lerp (:x (:next-pos node))
                        (:x (:prev connected-nodes))
                        (:attraction-force default-settings))
                y (lerp (:y (:next-pos node))
                        (:y (:prev connected-nodes))
                        (:attraction-force default-settings))
                node (update-in node [:x] x)
                node (update-in node [:y] y)]
            node)
          (node)))
      (node))))

(defn applyRepulsion
  ""
  [index r-tree]
  (let [x (:x (index (:nodes @node-map)))
        y (:y (index (:nodes @node-map)))]))

(defn grow
  "iterates one whole step of the cell growth"
  [path r-tree]
  (let [new-path (atom path)]
   (doseq [node path]
     (swap! new-path assoc-in [:nodes node] (applyBrownianMotion node))
     (swap! new-path assoc-in (applyAttraction path (.indexOf path node))))))


(defn init-growth
  "initializes growth"
  []
  )
