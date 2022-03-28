(ns sketch.grow
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]]
            [sketch.calculations :as calc])
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
                 bounds])

(def node-map (atom {:nodes [] }))
(defrecord Node [pos settings data])
(defrecord Data [is-fixed velocity next-position settings])

(def default-settings (hash-map
                       :min-distance 1
                       :max-distance 5
                       :repulsion-radius 10
                       :attraction-force 0.2
                       :repulsion-force 0.5
                       :allignment-force 0.45
                       :node-injection-interval 10
                       :brownian-motion-range 0.01
                       :fill-color nil
                       :stroke-color nil))

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
  [nodes settings is-closed bounds]
  (atom
   (Path. nodes settings is-closed bounds)))

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
  (Node. [x y] default-settings
         (Data. false 0 [:x nil :y nil] default-settings)))

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
    {:prev previous-node :next next-node}))

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
    (buildNode newx newy)))

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

(defn applyBrownianMotion
  "simulates minor motion"
  [node]
  (let [x (get (:pos node) 0) y (get (:pos node) 1)
        newx (+ x (random (- 0 (/ ((:settings node) :brownian-motion-range) 2))
                          (/ ((:settings node) :brownian-motion-range) 2)))
        newy (+ y (random (- 0 (/ ((:settings node) :brownian-motion-range) 2))
                          (/ ((:settings node ) :brownian-motion-range) 2)))]
    (assoc node :pos [newx newy])))

(defn applyAttraction
  "moves node closer to its connected nodes"
  [nodes index]
  (let [node (get nodes index)
        connected-nodes (getConnectedNodes nodes index)]
    
    ;; move towards next positions if exists
    (if (and
         (not= (:next connected-nodes) nil)
         (not (:is-fixed node)))
      (let [distance (get nodes index)
            least-min-distance (Math/min
                                (:min-distance node)
                                (:min-distance (:next connected-nodes)))]
        (if (> distance least-min-distance)
          (let [x (lerp (get (:pos (:next-position node)) 0)
                        (get (:pos (:next connected-nodes)) 0)
                        (:attraction-force (:settings node)))
                y (lerp (get (:pos (:next-position node)) 1)
                        (get (:pos (:next connected-nodes)) 1)
                        (:attraction-force (:settings node)))
                node (update-in node [:data :next-position :x] x)
                node (update-in node [:data :next-position :y] y)]
            node))))
    
    ;; move towards previos position if exists
    (if (and
         (not= (:prev connected-nodes) nil)
         (not (:is-fixed node)))
      (let [distance (get nodes index)
            least-min-distance (Math/min
                                (:min-distance node)
                                (:min-distance (:prev connected-nodes)))]
        (if (> distance least-min-distance)
          (let [x (lerp (get (:pos (:next-position node)) 0)
                        (get (:pos (:prev connected-nodes)) 0)
                        (:attraction-force (:settings node)))
                y (lerp (get (:pos (:next-position node)) 1)
                        (get (:pos (:prev connected-nodes)) 1)
                        (:attraction-force (:settings node)))
                node (update-in node [:data :next-position :x] x)
                node (update-in node [:data :next-position :y] y)]
            node))))))

(defn applyRepulsion
  ""
  [index r-tree]
  (let [x (:x (index (:nodes @node-map)))
        y (:y (index (:nodes @node-map)))]))

(defn grow
  "iterates one whole step of the cell growth"
  [path]
  (let [new-path (atom path)]
   (doseq [node path]
     (swap! new-path assoc-in [:nodes node] (applyBrownianMotion node))
     (swap! new-path assoc-in (applyAttraction path (.indexOf path node))))))

(defn test-reduce
  [paths]
  (vec
   (mapcat (fn [path]
             (:nodes path))
           paths)))

(defn euclidean-distance
  [vec1 vec2]
  (Math/sqrt
   (reduce + (map #(Math/pow (- %1 %2) 2) vec1 vec2))))

(defn nearest-neighbors
  [nodes query radius]
  (take radius
        (sort-by :distance
                 (map
                  #(assoc % :distance (euclidean-distance (:pos query) (:pos %)))
                  nodes))))

(defn knn
  [nodes query k]
  (let [votes (nearest-neighbors nodes query k)
        vote-freq (frequencies (map :class votes))]
    (key (apply max-key val vote-freq))))

(defn cropNodes
  [paths query radius]
  (map
   (fn [path] (let [negX (- (nth (:pos query) 0) radius)
                    negY (- (nth (:pos query) 1) radius)
                    posX (+ (nth (:pos query) 0) radius)
                    posY (+ (nth (:pos query) 1) radius)]
                (filter
                 #(and (>= (nth (:pos %) 0) negX)
                       (>= (nth (:pos %) 1) negY)
                       (<= (nth (:pos %) 0) posX)
                       (<= (nth (:pos %) 1) posY))
                 (:nodes @path))))
   paths))

(defn radiusNN
  [paths query radius]
  (let [nodes (cropNodes paths query radius)]
    (map
     #(nearest-neighbors % query radius)
     nodes)))

(def training-set
  [{:pos [5  5] :class "a"}
   {:pos [5  4] :class "b"}
   {:pos [5  3] :class "c"}
   {:pos [5  2] :class "d"}
   {:pos [7  1] :class "e"}
   {:pos [5  0] :class "f"}
   {:pos [5 -1] :class "g"}
   {:pos [5 -2] :class "h"}
   {:pos [5 -3] :class "i"}
   {:pos [5 -4] :class "j"}])

(def training-set-2
  (let [paths (vector (buildPath [(buildNode 7 2)
                                  (buildNode 3 1)
                                  (buildNode 50 13)]
                                 "settings" false "bounds")
                      (buildPath [(buildNode 7 3)
                                  (buildNode 6 1)
                                  (buildNode 5 2)]
                                 "settings" false "bounds"))]
    paths))

(defn testKNN
  []
  (let [query [5 0]
        k 1]
    (println query "-" (knn training-set query k))))

(defn createLine
  "creates a node list containg 2 nodes"
  [pos1 pos2]
  (list
   (buildNode (get pos1 0) (get pos1 1))
   (buildNode (get pos2 0) (get pos2 1))))

(defn addLinePath
  [pos1 pos2]
  (addPath
   (buildPath (createLine pos1 pos2) default-settings false nil)))

(defn init-growth
  "initializes growth"
  []
  (let [paths training-set-2
        path (get paths 0)
        nodes (:nodes @path)]
    (applyAttraction nodes 0)))