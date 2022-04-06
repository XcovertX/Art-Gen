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
                 alignment
                 node-injection-interval])

(def node-map (atom {:nodes []}))
(defrecord Node [pos settings data])
(defrecord Data [is-fixed is-end to-remove velocity next-position])

(def default-settings (hash-map
                       :min-distance 2
                       :max-distance 5
                       :repulsion-radius 10
                       :max-velocity 0.3
                       :attraction-force 0.6
                       :repulsion-force 0.21
                       :allignment-force 0.35
                       :node-injection-interval 10
                       :brownian-motion-range 1
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
  [nodes settings is-closed bounds brownian alignment node-injection-interval]
  (Path. nodes settings is-closed bounds brownian alignment node-injection-interval))

(defn addNode
  "atomically adds a node to node-map"
  [node]
  (swap! node-map assoc-in [:nodes] (conj (@node-map :nodes) node)))

(defn buildNode
  "constructs a new node"
  [x y settings is-fixed is-end to-remove]
  (Node. [x y] settings
         (Data. is-fixed is-end to-remove 0 {:x x :y y})))

(defn getConnectedNodes ;; remove dependency on indicies
  "retrieves all nodes connected to a given node"
  [nodes index is-closed]
  (if (not= index nil)
    (let [length (count nodes)
          previous-node (if (= index 0)
                          (when is-closed
                            (get nodes (- length 1)))
                          (get nodes (- index 1)))
          next-node (if (= index (- length 1))
                      (when is-closed
                        (get nodes 0))
                      (get nodes (+ index 1)))]
      {:prev previous-node :next next-node})
    {:prev nil :next nil}))


(defn getDistance
  [node-A node-B]
  (if (or (= node-A nil) (= node-B nil))
    -1
    (let [xa (get (:pos node-A) 0)
          ya (get (:pos node-A) 1)
          xb (get (:pos node-B) 0)
          yb (get (:pos node-B) 1)]
      (sqrt (+ (* (- xa xb) (- xa xb)) (* (- ya yb) (- ya yb)))))))

(defn insert
  "inserts node into a specific index"
  [vec pos item]
  (apply conj (subvec vec 0 pos) item (subvec vec pos)))

(defn eject
  "remove nodes into a specific index"
  [vec pos]
  (apply conj
         (if (= pos 1)
           (subvec vec 0)
           (subvec vec 0 (- pos 1)))
         (subvec vec pos)))

(defn applyBrownianMotion
  "simulates minor motion"
  [node]
  (if (not (:is-fixed (:data node)))
   (let [x (get (:pos node) 0)
        y (get (:pos node) 1)
        new-x (+ x (random (- 0 (/ (:brownian-motion-range (:settings node)) 2))
                           (/ (:brownian-motion-range (:settings node)) 2)))
        new-y (+ y (random (- 0 (/ ((:settings node) :brownian-motion-range) 2))
                           (/ (:brownian-motion-range (:settings node)) 2)))]
    (assoc node :pos [new-x new-y]))
    node))

(defn attract
  [node connected-node]
  (let [distance (getDistance node connected-node)
        least-min-distance (Math/min
                            (:min-distance (:settings node))
                            (:min-distance (:settings connected-node)))]
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
                          (not (:is-fixed (:data new-node))))
                   (attract new-node next-node)
                   new-node)
        new-node (if (and (not= previous-node nil)
                          (not (:is-fixed (:data new-node))))
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
    (buildNode new-x new-y settings false false false)))

(defn applyAlignment
  [path node-index]
  (let [node (get (:nodes path) node-index)
        connected-nodes (getConnectedNodes (:nodes path) node-index (:is-closed path))
        next-node (:next connected-nodes)
        previous-node (:prev connected-nodes)]
    (if (and (not= next-node nil)
             (not= previous-node nil)
             (not (:is-fixed (:data node))))
      (let [midpoint (getMidpointNode previous-node next-node (:settings path))
            next-x (:x (:next-position (:data node)))
            next-y (:y (:next-position (:data node)))
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
  [[x y] node]
  (if (and (= x (get (:pos node) 0)) (= y (get (:pos node) 1)))
    true
    false))

(defn applyBounds
  "prevents given node from leaving some boundry"
  [path node]
  (if (and (not= (:bounds path) nil)
           (some #(inBounds % node) (:bounds path)))
    (assoc-in node [:is-fixed] true)
    node))

(defn applyBounds-2
  [node w h]
  (let [x (get (:pos node) 0)
        y (get (:pos node) 1)]
    (if (or
         (< x 10)
         (< y 10)
         (> x (- w 10))
         (> y (- h 10)))
      (assoc-in node [:data :is-fixed] true)
      node)))

(defn indexed
  "Returns a lazy sequence of [index, item] pairs, where items come
  from 's' and indexes count up from zero.

  (indexed '(a b c d))  =>  ([0 a] [1 b] [2 c] [3 d])"
  [s]
  (map vector (iterate inc 0) s))

(defn positions
  "Returns a lazy sequence containing the positions at which pred
   is true for items in coll."
  [pred coll]
  (first (for [[idx elt] (indexed coll) :when (pred elt)] idx)))

;; (positions #{2} [1 2 3 4 1 2 3 4]) => (1 5)

(defn splitEdges
  "searches for edges that are too long and splits them"
  [path]
  (let [new-path (atom path)]
    (doseq [node (:nodes path)]
      (let [index (positions #{node} (:nodes @new-path))
            length (count @new-path)
            connected-nodes (getConnectedNodes (:nodes @new-path) index (:is-closed @new-path))
            prev-node (:prev connected-nodes)
            next-node (:next connected-nodes)
            distance (getDistance node prev-node)]
        (when (and (not= prev-node nil) 
                   (>= distance (:max-distance (:settings @new-path))))
          (let [midpoint-node (getMidpointNode node prev-node (:settings @new-path))]
            (if (= index 0)
              (swap! new-path assoc-in [:nodes] (insert (:nodes @new-path) length midpoint-node))
              (swap! new-path assoc-in [:nodes] (insert (:nodes @new-path) index midpoint-node)))))))
    @new-path))

(defn prunePaths
  "removes paths that are too small"
  [paths]
  (filter (fn [path] (> (count (:nodes path)) 1)) paths))

(defn pruneNodes
  "removes nodes that are too close"
   [path]
(let [new-path (atom path)]
  (doseq [node (:nodes path)]
    (let [index (positions #{node} (:nodes @new-path))
          length (count @new-path)
          connected-nodes (getConnectedNodes (:nodes @new-path) index (:is-closed @new-path))
          prev-node (:prev connected-nodes)
          next-node (:next connected-nodes)
          distance (getDistance node prev-node)]
      (when (and (not= prev-node nil)
                 (< distance (:min-distance (:settings @new-path))))
        (if (= index 0)
          (when (not (:is-fixed (:data (get (:nodes @new-path) (- length 1)))))
            (swap! new-path assoc-in [:nodes] (eject (:nodes @new-path) (- length 1))))
          (when (not (:is-fixed (:data (get (:nodes @new-path) (- index 1)))))
            (swap! new-path assoc-in [:nodes] (eject (:nodes @new-path) (- index 1))))))))
  @new-path))

(defn pruneNodes-2
  "removes nodes that are too close"
  [path]
  (let [new-path (atom path)]
    (doseq [node-index (range (count (:nodes @new-path)))]
      (let [node (get (:nodes @new-path) node-index)
            length (count @new-path)
            connected-nodes (getConnectedNodes (:nodes @new-path) node-index (:is-closed @new-path))
            prev-node (:prev connected-nodes)
            next-node (:next connected-nodes)
            distance (getDistance node prev-node)]
        (when (and (not= prev-node nil)
                   (not (:is-end (:data prev-node)))
                   (not (:is-fixed (:data prev-node)))
                   (not (:to-remove (:data prev-node)))
                   (< distance (:min-distance (:settings @new-path))))
          (swap! new-path assoc-in [:nodes (- node-index 1) :data :to-remove] true))))
    (assoc-in @new-path [:nodes] (into [] (remove #(:to-remove (:data %)) (:nodes @new-path))))))

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
  [(buildPath [(buildNode 500 502 default-settings true true false)
               (buildNode 520 497 default-settings false false false)
               (buildNode 527 500 default-settings false false false)
               (buildNode 500 532 default-settings false false false)
               (buildNode 510 530 default-settings false false false)
               (buildNode 530 498 default-settings false false false)]
              default-settings false (makeCanvasBounds w h) true 0.45 10)
   (buildPath [(buildNode 560 502 default-settings true true false)
               (buildNode 550 570 default-settings false false false)
               (buildNode 567 500 default-settings false false false)
               (buildNode 530 570 default-settings false false false)
               (buildNode 490 430 default-settings false false false)
               (buildNode 590 563 default-settings false false false)]
              default-settings false (makeCanvasBounds w h) true 0.45 10)])

(defn createLine
  "creates a node list containg 2 nodes"
  [pos1 pos2]
  (vector
   (buildNode (get pos1 0) (get pos1 1) default-settings true true false)
   (buildNode 580 170 default-settings false false false)
   (buildNode 30 270 default-settings false false false)
   (buildNode 540 70 default-settings false false false)
   (buildNode 72 20 default-settings false false false)
   (buildNode (get pos2 0) (get pos2 1) default-settings true true false)))

(defn addLinePath
  [pos1 pos2]
  (buildPath (createLine pos1 pos2) default-settings false [] true 0.45 10))

(defn update-map-entries [m e]
  (reduce #(update-in %1 [(first %2)] (fn [_] (last %2))) m e))

(def node-injection-time (atom 0))

(defn injectRandomNode
  [path]
  (let [nodes (:nodes path)
        node-index (rand-int (count nodes))
        node (get nodes node-index)
        length (count nodes)
        connected-nodes (getConnectedNodes nodes node-index (:is-closed path))
        next-node (:next connected-nodes)
        previous-node (:prev connected-nodes)
        distance (getDistance node previous-node)]
    (if (and (not= next-node nil)
             (not= previous-node nil)
             (> distance (:min-distance (:settings path))))
      (let [midpoint-node (getMidpointNode node previous-node (:settings path))]
        (assoc-in path [:nodes] (insert (:nodes path) node-index midpoint-node)))
      path)))

(defn grow
  "moves the node to new spot"
  [node]
  (if (not (:is-fixed (:data node)))
    (let [x (get (:pos node) 0)
          y (get (:pos node) 1)
          next-x (:x (:next-position (:data node)))
          next-y (:y (:next-position (:data node)))
          max-velocity (:max-velocity (:settings node))
          new-x (Math/round (lerp x next-x max-velocity))
          new-y (Math/round (lerp y next-y max-velocity))]
      (assoc-in node [:pos] [new-x new-y]))
    node))

(defn applyGrowth
  [paths width height]
  (let [new-paths (atom paths)]
    (doseq [path-index (range (count @new-paths))]
      (doseq [node-index (range (count (:nodes (get @new-paths path-index))))]
        ;; (printPosition @new-paths)
        (swap! new-paths assoc-in [path-index :nodes node-index] (applyBrownianMotion (get (:nodes (get @new-paths path-index)) node-index)))
      ;; (printPosition @new-paths)
        (swap! new-paths assoc-in [path-index :nodes node-index] (applyAttraction (get @new-paths path-index) node-index))
      ;;  (printPosition @new-paths)
        (swap! new-paths assoc-in [path-index :nodes node-index] (applyRepulsion @new-paths path-index node-index))
      ;;  (printPosition @new-paths)
        (swap! new-paths assoc-in [path-index :nodes node-index] (applyAlignment (get @new-paths path-index) node-index))
      ;;  (printPosition @new-paths)
        (swap! new-paths assoc-in [path-index :nodes node-index] (applyBounds-2 (get (:nodes (get @new-paths path-index)) node-index) width height))
      ;;  (printPosition @new-paths)
        (swap! new-paths assoc-in [path-index :nodes node-index] (grow (get (:nodes (get @new-paths path-index)) node-index))))
      ;; (printPosition @new-paths)
      (swap! new-paths assoc-in [path-index] (splitEdges (get @new-paths path-index)))
      ;; (printPosition @new-paths)
      (swap! new-paths assoc-in [path-index] (pruneNodes-2 (get @new-paths path-index)))
      ;; (printPosition @new-paths)
      (when (> (rand-int 100) 40)
       (swap! new-paths assoc-in [path-index] (injectRandomNode (get @new-paths path-index))))
      )
    ;; (Thread/sleep 10000)
    @new-paths))

(defn init-growth ;;call it seed?
  "initializes growth"
  [w h]
  (let [p (training-set-2 w h)
        p-2 [(addLinePath [10 (/ h 2)] [(- w 10) (/ h 2)])]
        paths (applyGrowth p-2 w h)]
    paths))
