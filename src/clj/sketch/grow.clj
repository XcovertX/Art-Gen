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
                 node-injection-interval
                 is-fixed])

(def node-map (atom {:nodes []}))
(defrecord Node [pos settings data])
(defrecord Data [is-fixed is-end to-remove is-random velocity next-position])

(def default-settings (hash-map
                       :min-distance 20
                       :max-distance 30
                       :repulsion-radius 14
                       :max-velocity 0.1
                       :attraction-force 0.3
                       :repulsion-force 2.5
                       :allignment-force 0.05
                       :node-injection-interval 10
                       :brownian-motion-range 0.6
                       :fill-color nil
                       :stroke-color nil
                       :draw-nodes false
                       :draw-random-injections false))

(def path-settings (hash-map
                    :min-distance 7
                    :max-distance 15
                    :repulsion-radius 15
                    :max-velocity 0.05
                    :attraction-force 0.1
                    :repulsion-force 2.5
                    :allignment-force 0.45
                    :node-injection-interval 10
                    :brownian-motion-range 0.5
                    :fill-color nil
                    :stroke-color nil
                    :draw-nodes false
                    :draw-random-injections true
                    :bug-finder-mode true))

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

(def i (atom 1))

(defn colorSpectrum
  "changes the color of node output to RGB spectrum R: @ 0 V: @ length of vector"
  [nodes]
  (let [multiplier (/ 360 (count nodes))]
    (if (mod 100000 @i)
    ;;   (println (mapv #(ceil (* multiplier %)) (range (count nodes)))))
    ;; (swap! i inc)
    (mapv #(ceil (* multiplier %)) (range (count nodes))))))

(declare getConnectedNodes)

(defn drawPath
 "draws the path according to the current settings"
 [path]
 (let [nodes (:nodes path)
       node-color (when (:bug-finder-mode (:settings path))
                   (colorSpectrum nodes))]
      (doseq [node-index (range (count nodes))
              :let [connected-nodes (getConnectedNodes nodes node-index (:is-closed path))
                    node (get nodes node-index)
                    next (:next connected-nodes)
                    prev (:prev connected-nodes)
                    x (get (:pos node) 0)
                    y (get (:pos node) 1)
                    next-x (get (:pos next) 0)
                    next-y (get (:pos next) 1)
                    prev-x (get (:pos prev) 0)
                    prev-y (get (:pos prev) 1)]]
        (when (not (:is-fixed path))
          (when (:bug-finder-mode (:settings path))
            (stroke (get node-color node-index) 360 360))
          (when (:draw-nodes (:settings path))
            (ellipse x y 2 2))
          (when (:draw-random-injections (:settings path))
            (when (:is-random (:data node))
              (ellipse x y 2 2)))
          (when (not= next nil)
            (line x y next-x next-y))))))

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
  [nodes settings is-closed bounds brownian alignment node-injection-interval is-fixed]
  (Path. nodes settings is-closed bounds brownian alignment node-injection-interval is-fixed))

(defn addNode
  "atomically adds a node to node-map"
  [node]
  (swap! node-map assoc-in [:nodes] (conj (@node-map :nodes) node)))

(defn buildNode
  "constructs a new node"
  [x y settings is-fixed is-end to-remove is-random]
  (Node. [x y] settings
         (Data. is-fixed is-end to-remove is-random 0 {:x x :y y})))

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

(defn removeFixed
  "removes all fixed nodes"
  [nodes]
  (filterv #(or (not (:is-fixed (:data %)))  (:is-end (:data %))) nodes))

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
  [node-A node-B settings rand-inject is-fixed]
  (let [new-x (/ (+ (get (:pos node-A) 0) (get (:pos node-B) 0)) 2)
        new-y (/ (+ (get (:pos node-A) 1) (get (:pos node-B) 1)) 2)]
    (buildNode new-x new-y settings is-fixed false false rand-inject)))

(defn applyAlignment
  [path node-index]
  (let [node (get (:nodes path) node-index)
        connected-nodes (getConnectedNodes (:nodes path) node-index (:is-closed path))
        next-node (:next connected-nodes)
        previous-node (:prev connected-nodes)]
    (if (and (not= next-node nil)
             (not= previous-node nil)
             (not (:is-fixed (:data node))))
      (let [midpoint (getMidpointNode previous-node next-node (:settings path) false false)
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
          (let [midpoint-node (getMidpointNode node prev-node (:settings @new-path) false false)]
            (if (= index 0)
              (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path)  midpoint-node))
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
          (if (and (:is-closed @new-path)
                   (= node-index 0))
            (swap! new-path assoc-in [:nodes (- length 1) :data :to-remove] true)
            (swap! new-path assoc-in [:nodes (- node-index 1) :data :to-remove] true)))))
    (assoc-in @new-path [:nodes] (into [] (remove #(:to-remove (:data %)) (:nodes @new-path))))))

(defn makeCanvasBounds
  "builds bounds out of the perimeter of the canvas"
  [w h]
  (println (reduce
            into []
            (conj []
                  (mapv vector (vec (map (fn [_] 1) (range (- h 10)))) (range (- h 10)))
                  (mapv vector (vec (map (fn [_] (- w 1)) (range (- h 10)))) (range (- h 10)))
                  (mapv vector (range (- w 10)) (vec (map (fn [_] 0) (range (- w 10)))))
                  (mapv vector (range (- w 10)) (vec (map (fn [_] (- h 1)) (range (- w 10)))))))))

(defn createLine
  "creates a node list containg 2 nodes"
  [pos1 pos2]
  (vector
   (buildNode (get pos1 0) (get pos1 1) default-settings true true false false)
   (buildNode (get pos2 0) (get pos2 1) default-settings true true false false)))

(defn addLinePath
  [pos1 pos2]
  (buildPath (createLine pos1 pos2) path-settings false [] true 0.45 10 false))


(defn createTriangle
  [x1 y1 x2 y2 x3 y3]
  (buildPath
   (vector
    (buildNode x1 y1 default-settings false true false false)
    (buildNode x2 y2 default-settings false false false false)
    (buildNode x3 y3 default-settings false true false false))
   path-settings true [] true 0.45 10 false))

(defn createFixedTriangle
  []
  (buildPath
   (vector
    (buildNode 600 300 default-settings false true false false)
    (buildNode 550 400 default-settings false false false false)
    (buildNode 650 600 default-settings false true false false))
   path-settings true [] true 0.45 10 true))


(defn createTriangle-2
  [w h]
  (buildPath
   (vector
    (buildNode 100 100 default-settings false false false false)
    (buildNode 250 250 default-settings false false false false)
    (buildNode 50 250 default-settings false false false false))
   path-settings true [] true 0.45 10 false))

(defn angleTestCase
  []
  (vector
   (buildNode 200 700 default-settings true true false false)
   (buildNode 500 700 default-settings false false false false)
   (buildNode 350 200 default-settings true true false false)))

(defn createAngleTestCase
  "creates a node list containg 2 nodes"
  []
  (buildPath (angleTestCase) default-settings false [] true 0.45 10 false))

(defn update-map-entries [m e]
  (reduce #(update-in %1 [(first %2)] (fn [_] (last %2))) m e))

(def node-injection-time (atom 0))

(defn injectRandomNode
  [path]
  (let [nodes (:nodes path)
        node-index (rand-int (count nodes))
        node (get nodes node-index)
        length (- (count nodes) 1)
        connected-nodes (getConnectedNodes nodes node-index (:is-closed path))
        next-node (:next connected-nodes)
        previous-node (:prev connected-nodes)
        distance (getDistance node previous-node)]
    (if (and (not= next-node nil)
             (not= previous-node nil)
             (> distance (:min-distance (:settings path))))
      (let [midpoint-node (getMidpointNode node previous-node (:settings path) true false)]
        (if (= node-index 0)
          (assoc-in path [:nodes] (conj nodes length midpoint-node))
          (assoc-in path [:nodes] (insert (:nodes path) node-index midpoint-node))))
      path)))

(defn addFirstVec
  "add items to the front of a vec"
  [target addition]
  (apply conj (if (vector? addition) addition [addition]) target))

(defn injectRandomNodeByCurvature-2
  [path]
  (let [nodes (:nodes path)]
    (assoc-in path [:nodes]
              (reduce
               (fn [new-nodes node-index]
                 (let [node (get nodes node-index)
                       connected-nodes (getConnectedNodes nodes node-index (:is-closed path))
                       next-node (:next connected-nodes)
                       prev-node (:prev connected-nodes)]
                   (if (or (= next-node nil)
                           (= prev-node nil)
                           (:is-fixed (:data node)))
                     (conj new-nodes node)
                     (let [n (- (get (:pos next-node) 1) (get (:pos prev-node) 1))
                           d (- (get (:pos next-node) 0) (get (:pos prev-node) 0))]
                       (if (= d 0)
                         (conj new-nodes node)
                         (let [a (/ n d)
                               tan (Math/atan a)
                               rad (Math/abs tan)
                               deg (degrees rad)
                               angle (Math/round deg)]
                           (if (<= angle 15)
                             (conj new-nodes node)
                             (let [next-midpoint-node (getMidpointNode node next-node (:settings path) true false)
                                   prev-midpoint-node (getMidpointNode node prev-node (:settings path) true false)]
                               (if (= node-index 0)
                                 (conj (addFirstVec new-nodes prev-midpoint-node) node next-midpoint-node)
                                 (conj new-nodes prev-midpoint-node next-midpoint-node))))))))))
               []
               (range (count nodes))))))

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

        (swap! new-paths assoc-in [path-index :nodes node-index] (applyBrownianMotion (get (:nodes (get @new-paths path-index)) node-index)))

        (swap! new-paths assoc-in [path-index :nodes node-index] (applyAttraction (get @new-paths path-index) node-index))

        (swap! new-paths assoc-in [path-index :nodes node-index] (applyRepulsion @new-paths path-index node-index))

        (swap! new-paths assoc-in [path-index :nodes node-index] (applyAlignment (get @new-paths path-index) node-index))

        (swap! new-paths assoc-in [path-index :nodes node-index] (applyBounds-2 (get (:nodes (get @new-paths path-index)) node-index) width height))

        (swap! new-paths assoc-in [path-index :nodes node-index] (grow (get (:nodes (get @new-paths path-index)) node-index))))
      (swap! new-paths assoc-in [path-index] (splitEdges (get @new-paths path-index)))
      (swap! new-paths assoc-in [path-index] (pruneNodes-2 (get @new-paths path-index)))
      (swap! new-paths assoc-in [path-index :nodes] (removeFixed (:nodes (get @new-paths path-index))))
      (when (> (rand-int 100) 70)
       (if (> (rand-int 100) 50)
        (swap! new-paths assoc-in [path-index] (injectRandomNodeByCurvature-2 (get @new-paths path-index)))
        (swap! new-paths assoc-in [path-index] (injectRandomNode (get @new-paths path-index))))))
    @new-paths))

(defn init-growth ;;call it seed?
  "initializes growth"
  [w h]
  (let [p-1 [(createTriangle 50 50 150 250 250 50)]
        p-2 [(addLinePath [0 (/ h 2)] [w (/ h 2)])]
        paths (applyGrowth p-1 w h)]
    paths))
