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
                 is-fixed
                 age
                 is-mature])

(def node-map (atom {:nodes []}))
(defrecord Node [pos settings data lifespan])
(defrecord Data [is-fixed is-end to-remove is-random velocity next-position])

(def default-settings (hash-map
                       :min-distance 3
                       :max-distance 6
                       :repulsion-radius 7
                       :max-velocity 0.01
                       :attraction-force 0.01
                       :repulsion-force 20
                       :allignment-force 0.001
                       :node-injection-interval 10
                       :brownian-motion-range 1.0
                       :fill-color nil
                       :stroke-color nil
                       :draw-edges false
                       :draw-nodes false
                       :draw-fixed-nodes true
                       :draw-all-random-injections? true
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
                    :brownian-motion-range 1.0
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
  (let [multiplier (/ 256 (count nodes))]
    ;; (if (mod 100000 @i)
    ;;   (println (mapv #(ceil (* multiplier %)) (range (count nodes)))))
    ;; (swap! i inc)
    (mapv #(round (* multiplier %)) (range (count nodes)))))

(declare getConnectedNodes)

(defn drawPath
  "draws the path according to the current settings"
  [path]
  (let [nodes (:nodes path)
        node-color (when (:bug-finder-mode? (:settings path))
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
        (when (:draw-edges (:settings path))
         (when (not= next nil)
          (line x y next-x next-y)))
        (when (:bug-finder-mode? (:settings path))
          (stroke (get node-color node-index) 360 360))
        (when (:draw-nodes (:settings path))
          (ellipse x y 2 2))
        (when (:draw-fixed-nodes (:settings path))
          (when (:is-fixed (:data node))
            (stroke 255 0 255)
            (ellipse x y 2 2)
            (stroke (get node-color node-index) 360 360)))
        (when (:draw-all-random-injections? (:settings path))
          (when (:is-random (:data node))
            (ellipse x y 2 2)))
        (when (:draw-new-random-injections? (:settings path))
          (when (and (:is-random (:data node)) (= (:lifespan node) 0))
            (stroke 255 0 255)
            (ellipse x y 2 2)
            (stroke (get node-color node-index) 360 360))))))
  ;; (Thread/sleep 1000)
  )

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
  (Path. nodes settings is-closed bounds brownian alignment node-injection-interval is-fixed 0 false))

(defn addNode
  "atomically adds a node to node-map"
  [node]
  (swap! node-map assoc-in [:nodes] (conj (@node-map :nodes) node)))

(defn buildNode
  "constructs a new node"
  [x y settings is-fixed is-end to-remove is-random]
  (Node. [x y] 
         settings
         (Data. is-fixed is-end to-remove is-random 0 {:x x :y y})
         0))

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

(defn amplifyFixed
  "removes all fixed nodes"
  [nodes]
  (mapv 
   (fn [node] (and (:is-fixed (:data node))  (not (:is-end (:data node))) (not (:hardend? (:settings node))))
     (update-in node [:settings] assoc
                ;; :attraction-force (/ (:attraction-force (:settings node)) 2)
                ;; :repulsion-force (* (:repulsion-force (:settings node)) 1.003)
                ;; :max-velocity (* (:max-velocity (:settings node)) 0.995)
                :hardend? true
                )) 
   nodes))

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
                          (not (:is-fixed (:data new-node)))
                          )
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
        node (atom (get (:nodes path) node-index))
        settings (if (:uniform-node-settings? (:settings path))
                   (:settings path)
                   (:settings @node))]
    (doseq [neighbor (radiusNN paths @node)]
      (let [x (lerp (get (:pos @node) 0)
                    (get (:pos neighbor) 0)
                    (- 0 (:repulsion-force settings)))
            y (lerp (get (:pos @node) 1)
                    (get (:pos neighbor) 1)
                    (- 0 (:repulsion-force settings)))
            ;; x (/ (+ x (:x (:next-position (:data node)))) 2)
            ;; y (/ (+ y (:y (:next-position (:data node)))) 2)
            ]
        (swap! node update-in [:data :next-position] assoc :x x :y y)))
    @node))

(defn getMidpointNode
  "retrieves the middle node between two nodes"
  [node-A node-B settings is-random is-fixed]
  (let [new-x (/ (+ (get (:pos node-A) 0) (get (:pos node-B) 0)) 2)
        new-y (/ (+ (get (:pos node-A) 1) (get (:pos node-B) 1)) 2)]
    (buildNode new-x new-y settings is-fixed false false is-random)))

(defn applyAlignment
  [path node-index]
  (let [node (get (:nodes path) node-index)
        connected-nodes (getConnectedNodes (:nodes path) node-index (:is-closed path))
        next-node (:next connected-nodes)
        previous-node (:prev connected-nodes)]
    (if (and (not= next-node nil)
             (not= previous-node nil)
             (not (:is-fixed (:data node))))
      (let [settings (if (:uniform-node-settings? (:settings path))
                       (:settings path)
                       (:settings node))
            midpoint (getMidpointNode previous-node next-node settings false false)
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

(defn applyHardening
  "depreciates attraction-force after a specified "
  [path]
  (let [age (:age path)
        start-hardening-num 300
        set-fixed-num 500
        depreciation-rate 0.99
        hard-freq 10
        nodes (:nodes path)]
    (if (or (:is-mature path) (< age start-hardening-num))
      path
      (if (>= age set-fixed-num)
        (assoc-in path [:nodes (rand-int (count (:nodes path))) :data :is-fixed] true)
        (assoc-in path [:nodes]
                  (reduce
                   (fn [new-nodes node-index]
                     (let [node (get nodes node-index)
                           connected-nodes (getConnectedNodes nodes node-index (:is-closed path))
                           next-node (:next connected-nodes)
                           prev-node (:prev connected-nodes)]
                       (if (or (= next-node nil)
                               (= prev-node nil))
                         (conj new-nodes node)
                         (if (and (= (mod hard-freq node-index) 0) (or (:is-fixed (:data prev-node)) (:is-fixed (:data next-node))))
                           (conj new-nodes (update-in node [:data] assoc :is-fixed true))
                           (conj new-nodes node)
                          ;;  (conj new-nodes
                          ;;        (update-in node [:settings] assoc
                          ;;                   :brownian-motion-range (* (:brownian-motion-range (:settings node)) depreciation-rate)
                          ;;                   :max-velocity (* (:max-velocity (:settings node)) depreciation-rate)))
                           ))))
                   []
                   (range (count nodes))))))))

(defn splitEdges
  "searches for edges that are too long and splits them"
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
                           (= prev-node nil))
                     (conj new-nodes node)
                     (let [distance (getDistance node prev-node)
                           settings (if (:uniform-node-settings? (:settings path))
                                      (:settings path)
                                      (:settings node))]
                       (if (< distance (:max-distance settings))
                         (conj new-nodes node)
                         (let [midpoint-node (getMidpointNode node prev-node settings false false)]
                           (conj new-nodes midpoint-node node)))))))
               []
               (range (count nodes))))))

(defn prunePaths
  "removes paths that are too small"
  [paths]
  (filter (fn [path] (> (count (:nodes path)) 1)) paths))

(defn pruneNodes
  "removes nodes that are too close"
  [path]
  (let [new-path (atom path)]
    (doseq [node-index (range (count (:nodes @new-path)))]
      (let [node (get (:nodes @new-path) node-index)
            length (count @new-path)
            connected-nodes (getConnectedNodes (:nodes @new-path) node-index (:is-closed @new-path))
            prev-node (:prev connected-nodes)
            distance (getDistance node prev-node)
            settings (if (:uniform-node-settings? (:settings @new-path))
                       (:settings @new-path)
                       (:settings node))]
        (when (and (not= prev-node nil)
                  ;;  (not (:is-end (:data prev-node)))
                   (not (:is-fixed (:data prev-node)))
                   (not (:to-remove (:data prev-node)))
                   (< distance (:min-distance settings)))
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
    (buildNode x1 y1 default-settings true true false false)
    (buildNode 80 40 default-settings false false false false)
    (buildNode 50 60 default-settings false false false false)
    (buildNode 70 80 default-settings false false false false)
    (buildNode x2 y2 default-settings false false false false)
    (buildNode x3 y3 default-settings true true false false))
   path-settings false [] true 0.45 10 false))

(defn createPathWithFixedNodes
  []
  (buildPath
   (vector
    (buildNode 50 50 default-settings true false false false)
    (buildNode 150 70 default-settings false false false false)
    (buildNode 120 70 default-settings false false false false)
    (buildNode 150 900 default-settings false false false false)
    (buildNode 10 70 default-settings false false false false)
    (buildNode 30 40 default-settings false false false false)
    (buildNode 150 70 default-settings false false false false)
    (buildNode 100 50 default-settings true false false false)
    (buildNode 50 370 default-settings false false false false)
    (buildNode 179 270 default-settings false false false false)
    (buildNode 122 701 default-settings true false false false)
    (buildNode 187 222 default-settings false false false false)
    (buildNode 200 200 default-settings false false false false)
    (buildNode 75 125 default-settings true false false false))
   path-settings true [] true 0.45 10 true))

(defn createCirclePath
  [x1 y1 x2 y2 x3 y3 x4 y4]
  (buildPath
   (vector
    (buildNode x1 y1 default-settings true true false false)
    (buildNode (+ x1 (/ (- x2 x1) 2)) (+ y1 (/ (- y2 y1) 2)) default-settings false false false false)
    (buildNode x2 y2 default-settings false false false false)
    (buildNode (+ x1 (/ (- x2 x1) 2)) (+ y2 (/ (- y3 y2) 2)) default-settings false false false false)
    (buildNode x3 y3 default-settings false false false false)
    (buildNode (+ x4 (/ (- x2 x4) 2)) (+ y2 (/ (- y3 y2) 2)) default-settings false false false false)
    (buildNode x4 y4 default-settings false false false false)
    (buildNode (+ x4 (/ (- x2 x4) 2)) (+ y1 (/ (- y2 y1) 2)) default-settings true true false false))
   path-settings false [] true 0.45 10 false))


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
        distance (getDistance node previous-node)
        min-distance (if (:uniform-node-settings? (:settings path))
                       (:min-distance path)
                       (:min-distance node))
        settings (if (:uniform-node-settings? (:settings path))
                   (:settings path)
                   (:settings node))]
    (if (and (not= next-node nil)
             (not= previous-node nil)
             (> distance min-distance))
      (let [midpoint-node (getMidpointNode node previous-node settings true false)]
        (if (= node-index 0)
          (assoc-in path [:nodes] (conj nodes length midpoint-node))
          (assoc-in path [:nodes] (insert (:nodes path) node-index midpoint-node))))
      path)))

(defn addFirstVec
  "add items to the front of a vec"
  [target addition]
  (apply conj (if (vector? addition) addition [addition]) target))

(defn injectRandomNodeByCurvature
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
                           d (- (get (:pos next-node) 0) (get (:pos prev-node) 0))
                           d (if (= d 0)
                               0.01
                               d)
                           a (/ n d)
                           tan (Math/atan a)
                           rad (Math/abs tan)
                           deg (degrees rad)
                           angle (Math/round deg)
                          ;;  sas (println (get (:pos next-node) 1) (get (:pos prev-node) 1) "n" n (get (:pos next-node) 0) (get (:pos prev-node) 0) "d" d "a" a "tan" tan deg angle)
                           ]
                       (if (and (>= angle 20) (< (rand-int 100) 50))
                         (conj new-nodes node)
                         (let [settings (if (:uniform-node-settings? (:settings path))
                                          (:settings path)
                                          (:settings node))
                               next-midpoint-node (getMidpointNode node next-node settings true false)
                               prev-midpoint-node (getMidpointNode node prev-node settings true false)]

                           (if (= node-index 0)
                             (conj (addFirstVec new-nodes prev-midpoint-node) next-midpoint-node)
                             (conj new-nodes prev-midpoint-node next-midpoint-node))))))))
               []
               (range (count nodes))))))

(defn testAngle
  [n d]
  (let [
        a (/ n d)
        tan (Math/atan a)
        rad (Math/abs tan)
        deg (degrees rad)
        angle (Math/round deg)]
    angle))

(defn incPathAge
  "increments a given path's age"
  [path]
  (update-in path [:age] inc))

(defn indexFixed
  "Returns a lazy sequence of [index, item] pairs, where items come
  from 's' and indexes count up from zero.

  (indexed '(a b c d))  =>  ([0 a] [1 b] [2 c] [3 d])"
  [nodes]
  (map vector (iterate inc 0) (reduce conj [] (mapv #(:is-fixed (:data %)) nodes))))

(defn fixedPositions
  "Returns a lazy sequence containing the positions at which pred
   is true for items in coll."
  [pred coll]
  (into [] (for [[idx elt] (indexFixed coll) :when (pred elt)] idx)))

;; (positions #{2} [1 2 3 4 1 2 3 4]) => (1 5)

(defn splitPaths
  "divides branches into new, individual paths"
  [paths]
  (reduce
   (fn [new-paths path-index]
     (let [path (get paths path-index)
           start-degrade-age 0]
       (if (< (:age path) start-degrade-age)
         (conj new-paths (incPathAge path))
         (let [aged-path (incPathAge path)
               nodes (:nodes aged-path)
               fixed-nodes (filterv #(:is-fixed (:data %)) nodes)]
           (if (= (count nodes) (count fixed-nodes))
             (conj new-paths (assoc-in aged-path [:is-mature] true))
             (if (or (<= (count fixed-nodes) 1) (even? (count fixed-nodes)))
               (conj new-paths aged-path)
               (let [fixed-node-positions (fixedPositions #{true} nodes)]
                 (into new-paths
                       (reduce
                        (fn [sub-path-coll fixed-index]
                          (conj sub-path-coll
                                (buildPath
                                 (subvec nodes (get fixed-node-positions fixed-index) (+ (get fixed-node-positions (+ fixed-index 1)) 1))
                                 path-settings (:is-closed path) (:bounds path) (:brownian path) (:alignment path) (:node-injection-time path) (:is-fixed path))))
                        []
                        (range (- (count fixed-node-positions) 1)))))))))))
   []
   (range (count paths))))

(defn testSplitPaths
  []
  (splitPaths [(createPathWithFixedNodes)]))

(defn incrementLifespan
  "increments the lifespan of a given node"
  [node]
  (update-in node [:lifespan] inc))



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
      (incrementLifespan (assoc-in node [:pos] [new-x new-y])))
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

      (swap! new-paths assoc-in [path-index] (applyHardening (get @new-paths path-index)))

      (swap! new-paths assoc-in [path-index] (splitEdges (get @new-paths path-index)))

      (swap! new-paths assoc-in [path-index] (pruneNodes (get @new-paths path-index)))

      ;; (swap! new-paths assoc-in [path-index :nodes] (removeFixed (:nodes (get @new-paths path-index))))

      (when (> (rand-int 100) 50)
        (swap! new-paths assoc-in [path-index] (injectRandomNodeByCurvature (get @new-paths path-index))))

      (swap! new-paths update-in [path-index :age] inc)
      (reset! new-paths (splitPaths @new-paths))
      ;; (doseq [path @new-paths]
      ;;   (println path)
      ;;   (println " "))
      )
    ;; (println (count @new-paths))
    ;; (Thread/sleep 5000)
    @new-paths))

(defn init-growth ;;call it seed?
  "initializes growth"
  [w h] 
  (let [p-1 [(createTriangle 50 50 (- w 50) 50 (/ w 2) (- h 50))]
        p-2 [(addLinePath [0 (/ h 2)] [w (/ h 2)])]
        p-3 [(createCirclePath 150 100 200 150 150 200 100 150)]
        paths (applyGrowth p-3 w h)]
    (println p-3)
    paths))
