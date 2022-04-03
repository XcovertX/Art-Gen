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

(def path-map (atom {:paths []}))
(defrecord Path [nodes
                 settings
                 is-closed
                 bounds
                 brownian
                 alignment])

(def node-map (atom {:nodes [] }))
(defrecord Node [pos settings data])
(defrecord Data [is-fixed velocity next-position])

(def default-settings (hash-map
                       :min-distance 20
                       :max-distance 30
                       :repulsion-radius 20
                       :max-velocity 0.1
                       :attraction-force 0.001
                       :repulsion-force 500
                       :allignment-force 0.001
                       :node-injection-interval 100
                       :brownian-motion-range 0.01
                       :fill-color nil
                       :stroke-color nil))

(defn printPosition
  [p]
  (println "Position:" (map
                        (fn [path]
                          (map
                           (fn [node] (:pos node))
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
  [nodes settings is-closed bounds brownian alignment]
  (Path. nodes settings is-closed bounds brownian alignment))

(defn addNode
  "atomically adds a node to node-map"
  [node]
  (swap! node-map assoc-in [:nodes] (conj (@node-map :nodes) node)))

(defn buildNode
  "constructs a new node"
  [x y settings]
  (Node. [x y] settings
         (Data. false 0 {:x x :y y})))

(defn getConnectedNodes
  "retrieves all nodes connected to a given node"
  [nodes index is-closed]
  (let [length (count nodes)
        previous-node (if (and (= index 0) :is-closed)
                        (get nodes (- length 1))
                        (when (>= index 1)
                          (get nodes (- index 1))))
        next-node (if (and (= index (- length 1)) is-closed)
                    (get nodes 0)
                    (when (<= index (- length 1))
                      (get nodes (+ index 1))))]
    {:prev previous-node :next next-node}))

(defn getDistance
  [node-A node-B]
  (let [xa (get (:pos node-A) 0)
        ya (get (:pos node-A) 1)
        xb (get (:pos node-B) 0)
        yb (get (:pos node-B) 1)]
    (sqrt (+ (* (- xa xb) (- xa xb)) (* (- ya yb) (- ya yb))))))

(defn insert
  "inserts node into a specific index"
  [vec pos item]
  (apply conj (subvec vec 0 pos) item (subvec vec pos)))

(defn applyBrownianMotion
  "simulates minor motion"
  [nodes]
  (mapv
    (fn [node]
      (let [x (get (:pos node) 0)
            y (get (:pos node) 1)
            new-x (+ x (random (- 0 (/ (:brownian-motion-range (:settings node)) 2))
                               (/ (:brownian-motion-range (:settings node)) 2)))
            new-y (+ y (random (- 0 (/ ((:settings node) :brownian-motion-range) 2))
                               (/ (:brownian-motion-range (:settings node)) 2)))]
        (assoc node :pos [new-x new-y])))
   nodes))

(defn attract
  [node connected-node]
  (let [distance (getDistance node connected-node)
        least-min-distance (Math/min
                            (:min-distance (:settings node))
                            (:min-distance (:settings node)))]
    (if (> distance least-min-distance)
      (let [connected-x (get (:pos connected-node) 0)
            connected-y (get (:pos connected-node) 1)
            next-x (:x (:next-position (:data node)))
            next-y (:y (:next-position (:data node)))
            x (lerp next-x
                    connected-x
                    (:attraction-force (:settings node)))
            y (lerp next-y
                    connected-y
                    (:attraction-force (:settings node)))]
        (update-in node [:data :next-position] assoc :x x :y y))
      node)))

(defn applyAttraction
  "moves all given nodes closer to their connected nodes"
  [path node-index]
  (let [new-node (get (:nodes path) node-index)
        connected-nodes (getConnectedNodes (:nodes path) node-index (:is-closed path))
        next-node (:next connected-nodes)
        previous-node (:prev connected-nodes)
        new-node (if (and (not= next-node nil)
                          (not (:is-fixed new-node)))
                   (attract new-node next-node)
                   new-node)
        new-node (if (and (not= previous-node nil)
                          (not (:is-fixed new-node)))
                   (attract new-node previous-node)
                   new-node)]
    new-node))



(declare applyRepulsion)

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
  [nodes node radius]
  (take radius
        (sort-by :distance
                 (map
                  #(assoc % :distance (euclidean-distance (:pos node) (:pos %)))
                  nodes))))

(defn knn
  [nodes query k]
  (let [votes (nearest-neighbors nodes query k)
        vote-freq (frequencies (map :class votes))]
    (key (apply max-key val vote-freq))))

(defn cropNodes
  [paths node radius]
  (map
   (fn [path]
     (let [negX (- (nth (:pos node) 0) radius)
           negY (- (nth (:pos node) 1) radius)
           posX (+ (nth (:pos node) 0) radius)
           posY (+ (nth (:pos node) 1) radius)]
       (filter
        #(and (>= (nth (:pos %) 0) negX)
              (>= (nth (:pos %) 1) negY)
              (<= (nth (:pos %) 0) posX)
              (<= (nth (:pos %) 1) posY))
        (:nodes path))))
   paths))

(defn radiusNN
  [paths node]
  (let [radius (:repulsion-radius (:settings node))
        nodes (cropNodes paths node radius)]
    (flatten
     (map
      #(nearest-neighbors % node radius)
      nodes))))

(defn applyRepulsion
  "moves the indexed node away from any node within its radius"
  [paths path-index node-index]
  (let [path (get paths path-index)
        node (atom (get (:nodes path) node-index))]
        (doseq [neighbor (radiusNN paths @node)]
          (let [x (lerp (get (:pos @node) 0)
                        (get (:pos neighbor) 0)
                        (- 0 (:repulsion-force (:settings @node))))
                y (lerp (get (:pos @node) 1)
                        (get (:pos neighbor) 1)
                        (- 0 (:repulsion-force (:settings @node))))]
            (swap! node update-in [:data :next-position] assoc :x x :y y)))
    @node))

(defn getMidpointNode
  "retrieves the middle node between two nodes"
  [node-A node-B settings]
  (let [new-x (/ (+ (get (:pos node-A) 0) (get (:pos node-B) 0)) 2)
        new-y (/ (+ (get (:pos node-A) 1) (get (:pos node-B) 1)) 2)]
    (buildNode new-x new-y settings)))

(defn applyAlignment
  [path node-index]
  (let [node (get (:nodes path) node-index)
        connected-nodes (getConnectedNodes (:nodes path) node-index (:is-closed path))
        next-node (:next connected-nodes)
        previous-node (:prev connected-nodes)]
    (if (and (not= next-node nil)
             (not= previous-node nil)
             (not (:is-fixed node)))
      (let [midpoint (getMidpointNode previous-node next-node (:settings path))
            next-x (:x (:next-position (:data midpoint)))
            next-y (:y (:next-position (:data midpoint)))
            x (lerp next-x
                    (get (:pos midpoint) 0)
                    (:alignment path))
            y (lerp next-y
                    (get (:pos midpoint) 1)
                    (:alignment path))
            node (update-in node [:data :next-position] assoc :x x :y y)]
        node)
      node)))

(declare training-set-2)

(defn inBounds
  [[x y]]
  (if (and (= x 8) (= y 14))
               true
               false))

(defn applyBounds
  "prevents given node from leaving some boundry"
  [path node]
  (if (and (not= (:bounds path) nil)
           (some #(inBounds %) (:bounds path)))
    (assoc-in node [:is-fixed] true)
    node))

(defn splitEdges
  "searches for edges that are too long and splits them"
  [path]
  (let [new-path (atom path)]
    (doseq [node (:nodes path)]
      (let [index (.indexOf @new-path node)
            length (count @new-path)
            connected-nodes (getConnectedNodes index (:nodes @new-path) (:is-closed path))
            prev-node (:prev connected-nodes)
            next-node (:next connected-nodes)
            distance (getDistance node prev-node)]
        (if (and (not= next-node nil) (>= distance (:max-distance (:settings path))))
          (let [midpoint-node (getMidpointNode node prev-node (:setting path))]
            (if (= index 0)
              (swap! new-path (insert @new-path length midpoint-node))
              (swap! new-path (insert @new-path index midpoint-node)))))))
    @new-path))

(defn prunePaths
  "removes paths that are too small"
  [paths]
  (filter (fn [path] (> (count (:nodes path)) 1)) paths))

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

(defn makeCanvasBounds
  "builds bounds out of the perimeter of the canvas"
  [w h]
  (reduce
   into []
   (conj []
         (mapv vector (vec (map (fn [_] 0) (range h))) (range h))
         (mapv vector (vec (map (fn [_] (- w 1)) (range h))) (range h))
         (mapv vector (range w) (vec (map (fn [_] 0) (range w))))
         (mapv vector (range w) (vec (map (fn [_] (- h 1)) (range w)))))))

(defn training-set-2
  [w h]
  [(buildPath [(buildNode 2 12 default-settings)
               (buildNode 33 10 default-settings)
               (buildNode 20 33 default-settings)]
              default-settings false (makeCanvasBounds w h) true 0.45)
   (buildPath [(buildNode 60 32 default-settings)
               (buildNode 66 31 default-settings)
               (buildNode 59 38 default-settings)]
              default-settings false (makeCanvasBounds w h) true 0.45)])

(defn testKN
  []
  (let [query [5 0]
        k 1]
    (println query "-" (knn training-set query k))))

(defn createLine
  "creates a node list containg 2 nodes"
  [pos1 pos2]
  (list
   (buildNode (get pos1 0) (get pos1 1) default-settings)
   (buildNode (get pos2 0) (get pos2 1) default-settings)))

(defn addLinePath
  [pos1 pos2]
  (addPath
   (buildPath (createLine pos1 pos2) default-settings false [] true 0.45)))

(defn update-map-entries [m e]
  (reduce #(update-in %1 [(first %2)] (fn [_] (last %2))) m e))

(defn grow
  "moves the node to new spot"
  [node]
  (when (not (:is-fixed node))
    (let [x (get (:pos node) 0)
          y (get (:pos node) 1)
          next-x (:x (:next-position (:data node)))
          next-y (:y (:next-position (:data node)))
          max-velocity (:max-velocity (:settings node))
          new-x (lerp x next-x max-velocity)
          new-y (lerp y next-y max-velocity)]
      (assoc-in node [:pos] [new-x new-y]))))

(defn applyGrowth
  [paths]
   (let [new-paths (atom paths)]
     (doseq [path-index (range (count @new-paths))]
       (doseq [node-index (range (count (:nodes (get @new-paths path-index))))]
         (swap! new-paths update-in [path-index] assoc :nodes (applyBrownianMotion (:nodes (get @new-paths path-index))))
         (swap! new-paths assoc-in [path-index :nodes node-index] (applyAttraction (get @new-paths path-index) node-index))
         (swap! new-paths assoc-in [path-index :nodes node-index] (applyRepulsion @new-paths path-index node-index))
         (swap! new-paths assoc-in [path-index :nodes node-index] (applyAlignment (get @new-paths path-index) node-index))
         (swap! new-paths assoc-in [path-index :nodes node-index] (grow (get (:nodes (get @new-paths path-index)) node-index)))))
     @new-paths))

(defn init-growth ;;call it seed?
  "initializes growth"
  [w h]
  (let [p (training-set-2 w h)
        paths (applyGrowth p)]
    paths))
     