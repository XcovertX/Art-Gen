(ns sketch.grow
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]]
            [sketch.calculations :as calc]
            [sketch.path :as path])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])
  (:import [processing.core PShape PGraphics]))

;; ------------ Growth Tools -----------------

(def path-map (atom {:paths []}))

(def node-map (atom {:nodes []}))

(def default-path-growth-data
  (hash-map
   :min-distance 3
   :max-distance 6
   :repulsion-radius 7
   :max-velocity 0.01
   :attraction-force 0.01
   :repulsion-force 20
   :allignment-force 0.001
   :node-injection-interval 10
   :brownian-motion-range 0.25))

(def default-node-growth-data
  (hash-map
   :min-distance 3
   :max-distance 6
   :repulsion-radius 7
   :max-velocity 0.01
   :attraction-force 0.01
   :repulsion-force 20
   :allignment-force 0.001
   :node-injection-interval 10
   :brownian-motion-range 0.25))

(defn applyBrownianMotion
  "simulates minor motion"
  [node]
  (if (not (:is-fixed (:data node)))
   (let [x (get (:position node) 0)
         y (get (:position node) 1)
         new-x (+ x (random (- 0 (/ (:brownian-motion-range (:settings node)) 2))
                            (/ (:brownian-motion-range (:settings node)) 2)))
         new-y (+ y (random (- 0 (/ ((:settings node) :brownian-motion-range) 2))
                            (/ (:brownian-motion-range (:settings node)) 2)))]
     (assoc node :position [new-x new-y]))
    node))

;; needs work -- not finished
(defn removeOutOfBoundsNodes
  "removes nodes that exits a given boundry"
  [path left-boundary right-boundary top-boundary]
  (let [nodes (mapv (fn [node]
                      (if ()
                        (update-in node [:data] assoc :is-fixed true)
                        ()))
                    (filterv #(<= (get (:position %) 0) right-boundary) (:nodes path)))
        right-node (filterv #(<= (get (:position %) 0) right-boundary) (:nodes path))
        top-nodes  (filterv #(>= (get (:position %) 0) top-boundary) (:nodes path))]))


(defn amplifyFixed
  "amplifies all fixed nodes"
  [nodes]
  (mapv 
   (fn [node] (and (:is-fixed (:data node))  (not (:is-end (:data node))) (not (:hardend? (:settings node))))
     (update-in node [:settings] assoc :hardend? true)) 
   nodes))

(defn attract
  [node connected-node]
  (let [distance (path/getDistance node connected-node)
        least-min-distance (Math/min
                            (:min-distance (:data node))
                            (:min-distance (:data connected-node)))]
    (if (> distance least-min-distance)
      (let [connected-x (get (:position connected-node) 0)
            connected-y (get (:position connected-node) 1)
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
  (if (not (:is-fixed (:data (get (:nodes path) node-index))))
    (let [new-node (get (:nodes path) node-index)

          connected-nodes (path/getConnectedNodes (:nodes path) node-index (:is-closed path))
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
      new-node)
    (get (:nodes path) node-index)))


(declare applyRepulsion)

(defn test-reduce
  [paths]
  (vec
   (mapcat (fn [path]
             (:nodes path))
           paths)))

(defn nearest-neighbors
  [nodes node radius]
  (take radius
        (sort-by :distance
                 (map
                  #(assoc % :distance (calc/euclidean-distance (:position node) (:position %)))
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
     (let [negX (- (nth (:position node) 0) radius)
           negY (- (nth (:position node) 1) radius)
           posX (+ (nth (:position node) 0) radius)
           posY (+ (nth (:position node) 1) radius)]
       (filter
        #(and (>= (nth (:position %) 0) negX)
              (>= (nth (:position %) 1) negY)
              (<= (nth (:position %) 0) posX)
              (<= (nth (:position %) 1) posY))
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
      (let [x (lerp (:x (:position @node))
                    (:x (:position neighbor))
                    (- 0 (:repulsion-force settings)))
            y (lerp (:y (:position @node))
                    (:y (:position neighbor))
                    (- 0 (:repulsion-force settings)))]
        (swap! node update-in [:data :next-position] assoc :x x :y y)))
    @node))

(defn applyAlignment
  [path node-index]
  (let [node (get (:nodes path) node-index)
        connected-nodes (path/getConnectedNodes (:nodes path) node-index (:is-closed path))
        next-node (:next connected-nodes)
        previous-node (:prev connected-nodes)]
    (if (and (not= next-node nil)
             (not= previous-node nil)
             (not (:is-fixed (:data node))))
      (let [settings (if (:uniform-node-settings? (:settings path))
                       (:settings path)
                       (:settings node))
            midpoint (path/getMidpointNode previous-node next-node settings false false)
            next-x (:x (:next-position (:data node)))
            next-y (:y (:next-position (:data node)))
            x (lerp next-x
                    (get (:position midpoint) 0)
                    (:alignment path))
            y (lerp next-y
                    (get (:position midpoint) 1)
                    (:alignment path))
            node (update-in node [:data :next-position] assoc :x x :y y)]
        node)
      node)))

(defn inBounds
  [[x y] node]
  (if (and (= x (get (:position node) 0)) (= y (get (:position node) 1)))
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
  (let [x (get (:position node) 0)
        y (get (:position node) 1)]
    (if (or
         (< x 10)
         (< y 10)
         (> x (- w 10))
         (> y (- h 10)))
      (assoc-in node [:data :is-fixed] true)
      node)))

(def counter (atom {:i 0}))

(defn applyHardening
  "depreciates attraction-force after a specified "
  [path]
  (swap! counter update-in [:i] inc)
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
                           connected-nodes (path/getConnectedNodes nodes node-index (:is-closed path))
                           next-node (:next connected-nodes)
                           prev-node (:prev connected-nodes)]
                       (if (or (= next-node nil)
                               (= prev-node nil))
                         (conj new-nodes node)
                         (if (and (= (mod hard-freq (:i @counter)) 0) (or (:is-fixed (:data prev-node)) (:is-fixed (:data next-node))))
                           (conj new-nodes (update-in node [:data] assoc :is-fixed true))
                           (conj new-nodes node)))))
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
                       connected-nodes (path/getConnectedNodes nodes node-index (:is-closed path))
                       next-node (:next connected-nodes)
                       prev-node (:prev connected-nodes)]
                   (if (or (= next-node nil)
                           (= prev-node nil))
                     (conj new-nodes node)
                     (let [distance (path/getDistance node prev-node)
                           settings (if (:uniform-node-settings? (:settings path))
                                      (:settings path)
                                      (:settings node))]
                       (if (< distance (:max-distance settings))
                         (conj new-nodes node)
                         (let [midpoint-node (path/getMidpointNode node prev-node settings false false)]
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
            connected-nodes (path/getConnectedNodes (:nodes @new-path) node-index (:is-closed @new-path))
            prev-node (:prev connected-nodes)
            distance (path/getDistance node prev-node)
            data (if (:uniform-node-settings? (:settings @new-path))
                       (:data @new-path)
                       (:data node))]
        (when (and (not= prev-node nil)
                   (not (:is-end (:data prev-node)))
                   (not (:is-fixed (:data prev-node)))
                   (not (:to-remove (:data prev-node)))
                   (< distance (:min-distance data)))
          (if (and (:is-closed (:data @new-path))
                   (= node-index 0))
            (swap! new-path assoc-in [:nodes (- length 1) :data :to-remove] true)
            (swap! new-path assoc-in [:nodes (- node-index 1) :data :to-remove] true)))))
    (assoc-in @new-path [:nodes] (into [] (remove #(:to-remove (:data %)) (:nodes @new-path))))))

(defn makeCanvasBounds
  "builds bounds on the perimeter of the canvas"
  [w h]
  (println (reduce
            into []
            (conj []
                  (mapv vector (vec (map (fn [_] 1) (range (- h 10)))) (range (- h 10)))
                  (mapv vector (vec (map (fn [_] (- w 1)) (range (- h 10)))) (range (- h 10)))
                  (mapv vector (range (- w 10)) (vec (map (fn [_] 0) (range (- w 10)))))
                  (mapv vector (range (- w 10)) (vec (map (fn [_] (- h 1)) (range (- w 10)))))))))

;; (defn createLine
;;   "creates a node list containg 2 nodes"
;;   [pos1 pos2]
;;   (vector
;;    (buildNode (get pos1 0) (get pos1 1) default-settings true true false false)
;;    (buildNode (get pos2 0) (get pos2 1) default-settings true true false false)))

;; (defn addLinePath
;;   [pos1 pos2]
;;   (buildPath (createLine pos1 pos2) path-settings false [] true 0.45 10 false))


;; (defn createTriangle
;;   [x1 y1 x2 y2 x3 y3]
;;   (buildPath
;;    (vector
;;     (buildNode x2 y2 default-settings false false false false)
;;     (buildNode x3 y3 default-settings true true false false))
;;    path-settings false [] true 0.45 10 false))

;; (defn createPathWithFixedNodes
;;   []
;;   (buildPath
;;    (vector
;;     (buildNode 50 50 default-settings true false false false)
;;     (buildNode 150 70 default-settings false false false false)
;;     (buildNode 120 70 default-settings false false false false)
;;     (buildNode 150 900 default-settings false false false false)
;;     (buildNode 10 70 default-settings false false false false)
;;     (buildNode 30 40 default-settings false false false false)
;;     (buildNode 150 70 default-settings false false false false)
;;     (buildNode 100 50 default-settings true false false false)
;;     (buildNode 50 370 default-settings false false false false)
;;     (buildNode 179 270 default-settings false false false false)
;;     (buildNode 122 701 default-settings true false false false)
;;     (buildNode 187 222 default-settings false false false false)
;;     (buildNode 200 200 default-settings false false false false)
;;     (buildNode 75 125 default-settings true false false false))
;;    path-settings true [] true 0.45 10 true))

;; (defn createCirclePath
;;   [x1 y1 x2 y2 x3 y3 x4 y4]
;;   (buildPath
;;    (vector
;;     (buildNode x1 y1 default-settings true true false false)
;;     (buildNode (+ x1 (/ (- x2 x1) 2)) (+ y1 (/ (- y2 y1) 2)) default-settings false false false false)
;;     (buildNode x2 y2 default-settings false false false false)
;;     (buildNode (+ x1 (/ (- x2 x1) 2)) (+ y2 (/ (- y3 y2) 2)) default-settings false false false false)
;;     (buildNode x3 y3 default-settings false false false false)
;;     (buildNode (+ x4 (/ (- x2 x4) 2)) (+ y2 (/ (- y3 y2) 2)) default-settings false false false false)
;;     (buildNode x4 y4 default-settings false false false false)
;;     (buildNode (+ x4 (/ (- x2 x4) 2)) (+ y1 (/ (- y2 y1) 2)) default-settings true true false false))
;;    path-settings false [] true 0.45 10 false))


;; (defn createTriangle-2
;;   [w h]
;;   (buildPath
;;    (vector
;;     (buildNode 100 100 default-settings false false false false)
;;     (buildNode 250 250 default-settings false false false false)
;;     (buildNode 50 250 default-settings false false false false))
;;    path-settings true [] true 0.45 10 false))

;; (defn angleTestCase
;;   []
;;   (vector
;;    (buildNode 200 700 default-settings true true false false)
;;    (buildNode 500 700 default-settings false false false false)
;;    (buildNode 350 200 default-settings true true false false)))

;; (defn createAngleTestCase
;;   "creates a node list containg 2 nodes"
;;   []
;;   (buildPath (angleTestCase) default-settings false [] true 0.45 10 false))

(defn update-map-entries [m e]
  (reduce #(update-in %1 [(first %2)] (fn [_] (last %2))) m e))

(def node-injection-time (atom 0))

(defn injectRandomNode
  [path]
  (let [nodes (:nodes path)
        node-index (rand-int (count nodes))
        node (get nodes node-index)
        length (- (count nodes) 1)
        connected-nodes (path/getConnectedNodes nodes node-index (:is-closed (:data path)))
        next-node (:next connected-nodes)
        previous-node (:prev connected-nodes)
        distance (path/getDistance node previous-node)
        min-distance (if (:uniform-node-settings? (:settings path))
                       (:min-distance (:data path))
                       (:min-distance (:data node)))
        settings (if (:uniform-node-settings? (:settings path))
                   (:settings path)
                   (:settings node))]
    (if (and (not= next-node nil)
             (not= previous-node nil)
             (> distance min-distance))
      (let [midpoint-node (path/getMidpointNode node previous-node settings true false)]
        (if (= node-index 0)
          (assoc-in path [:nodes] (conj nodes length midpoint-node))
          (assoc-in path [:nodes] (path/insert (:nodes path) node-index midpoint-node))))
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
                       connected-nodes (path/getConnectedNodes nodes node-index (:is-closed (:data path)))
                       next-node (:next connected-nodes)
                       prev-node (:prev connected-nodes)]
                   (if (or (= next-node nil)
                           (= prev-node nil)
                           (:is-fixed (:data node)))
                     (conj new-nodes node)
                     (let [n (- (:y (:position next-node)) (:y (:position prev-node)))
                           d (- (:x (:position next-node)) (:x (:position prev-node)))
                           d (if (= d 0)
                               0.01
                               d)
                           a (/ n d)
                           tan (Math/atan a)
                           rad (Math/abs tan)
                           deg (degrees rad)
                           angle (Math/round deg)]
                       (if (and (>= angle 20) (< (rand-int 100) 50))
                         (conj new-nodes node)
                         (let [settings (if (:uniform-node-settings? (:settings path))
                                          (:settings path)
                                          (:settings node))
                               next-midpoint-node (path/getMidpointNode node next-node settings true false)
                               prev-midpoint-node (path/getMidpointNode node prev-node settings true false)]

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

(defn incGrowthCount
  "increments a given node's growth count"
  [node]
  (update-in node [:data :growth-iteration-count] inc))

(defn resetGrowthCount
  "resets a given node's growth count"
  [node]
  (update-in node [:data] assoc :growth-iteration-count 0))

(defn grow
  "moves the node to new spot"
  [node]
  (if (not (:is-fixed (:data node)))
    (let [x (get (:position node) 0)
          y (get (:position node) 1)
          next-x (:x (:next-position (:data node)))
          next-y (:y (:next-position (:data node)))
          max-velocity (:max-velocity (:settings node))
          new-x (Math/round (lerp x next-x max-velocity))
          new-y (Math/round (lerp y next-y max-velocity))
          new-node (path/incNodeAge node)
          new-node (assoc-in new-node [:position] [new-x new-y])]
      new-node)
    node))

(def div-complete (atom {:div false}))

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

       (swap! new-paths assoc-in [path-index] (pruneNodes (get @new-paths path-index)))

       (swap! new-paths assoc-in [path-index :nodes] (path/removeFixed (:nodes (get @new-paths path-index))))

       (when (> (rand-int 100) 50)
         (swap! new-paths assoc-in [path-index] (injectRandomNodeByCurvature (get @new-paths path-index))))

       (swap! new-paths update-in [path-index :age] inc)
      ;;  (when (and not :div @div-complete (= (:age (get @new-paths path-index)) 100))
      ;;    (swap! new-paths assoc-in [path-index :nodes] (dividePathsOnHorizontalLine (get @new-paths path-index) (random width)))
      ;;    (reset! new-paths (buildSubPaths @new-paths))
      ;;    (swap! div-complete assoc-in [:div] true))
       
         )

    @new-paths))

;; (defn init-growth ;;call it seed?
;;   "initializes growth"
;;   [w h] 
;;   (let [p-1 [(createTriangle 50 50 (- w 50) 50 (/ w 2) (- h 50))]
;;         p-2 [(addLinePath [0 (/ h 2)] [w (/ h 2)])]
;;         p-3 [(createCirclePath 150 100 200 150 150 200 100 150)]
;;         p-4 [(addLinePath [0 0] [w h])]
;;         paths (applyGrowth p-4 w h)]
;;     paths))

