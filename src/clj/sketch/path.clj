(ns sketch.path
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

;; A namespace for all compatible data structures
;; Currently not used and exists as a start to reorganizing the app

;; Used as a global means to give unique IDs to new paths
(def pathIDCounter (atom 0))

;; Used as a global means to give unique IDs to new nodes
(def nodeIDCounter (atom 0))

;; ------------ Nodes ---------------
(defrecord Node [ID position settings data])

(defrecord NodeData [next-position age velocity])

(defrecord Position2D [x y])

(defrecord Position3D [x y z])

(def default-node-data
  "Data to be included with a node not provided with any"
  (hash-map
   :age 0
   :is-fixed true
   :is-end false
   :is-random false))

(def default-node-settings
  "default setting to be included with a node where settings is set to nil"
  (hash-map
   :fill-color nil
   :stroke-color nil
   :draw-edges true
   :draw-nodes true
   :draw-fixed-nodes false
   :draw-all-random-injections? false
   :bug-finder-mode? true
   :uniform-node-settings? false))

;; ------------ Paths ---------------
(defrecord Path [ID nodes settings data])

(defrecord PathData [is-fixed
                     is-end
                     is-closed
                     to-remove
                     is-random
                     velocity
                     next-position
                     bounds
                     brownian
                     alignment
                     node-injection-interval
                     is-mature
                     is-divide-ready])

(def default-path-data
  "Data to be included with a path not provided with any"
  (hash-map :age 0 :type "path"))

(def default-path-settings
  "Settings to be included with a path not provided with any"
  (hash-map
   :is-closed false
   :fill-color nil
   :stroke-color nil
   :draw-edges true
   :draw-nodes false
   :draw-fixed-nodes false
   :draw-all-random-injections? false
   :draw-new-random-injections? false
   :bug-finder-mode? true
   :uniform-node-settings? false))

;; --------------- Functions ---------------------

(defn incPathAge
  "increments a given path's age"
  [path]
  (update-in path [:data :age] inc))

(defn incNodeAge
  "increments a given node's age"
  [node]
  (update-in node [:data :age] inc))

(defn indexed
  "Returns a lazy sequence of [index, item] pairs, where items come
  from 's' and indexes count up from zero.

  (indexed '(a b c d))  =>  ([0 a] [1 b] [2 c] [3 d])"
  [s]
  (map vector (iterate inc 0) s))

(defn getConnectedNodes ;; remove dependency on indicies
  "retrieves all nodes connected to a given node"
  [nodes index is-closed]
  (if (or (not= index nil) (< (count nodes) 2))
    (let [length (count nodes)
          prev-node (if (= length 2)
                      (first nodes)
                      (if (= index 0)
                        (when is-closed
                          (get nodes (- length 1)))
                        (get nodes (- index 1))))
          next-node (if (= length 2)
                      (last nodes)
                      (if (= index (- length 1))
                        (when is-closed
                          (get nodes 0))
                        (get nodes (+ index 1))))]
      {:prev prev-node :next next-node})
    {:prev nil :next nil}))

(defn getDistance
  [node-A node-B]
  (if (or (= node-A nil) (= node-B nil))
    -1
    (let [xa (:x (:position node-A))
          ya (:y (:position node-A))
          xb (:x (:position node-B))
          yb (:y (:position node-B))]
      (sqrt (+ (* (- xa xb) (- xa xb)) (* (- ya yb) (- ya yb)))))))

(defn generatePathID
  "generates a new unique path id"
  []
  (reset! pathIDCounter (inc @pathIDCounter)))

(defn buildPath
  "builds a path"
  ([nodes]
   (Path. (generatePathID) nodes default-path-settings default-path-data))

  ([nodes settings]
   (Path. (generatePathID) nodes settings default-path-data))

  ([nodes settings data]
   (Path. (generatePathID) nodes settings data)))

(defn insert
  "inserts node into a specific index of a path"
  [vec pos item]
  (apply conj (subvec vec 0 pos) item (subvec vec pos)))

(defn generateNodeID
  "generates a new unique node id"
  []
  (reset! nodeIDCounter (inc @nodeIDCounter)))

(defn getPosition
  "generates 2D or 3D position based on give parameter"
  [position]
  (if (:z position)
    (Position3D. (:x position) (:y position) (:z position))
    (Position2D. (:x position) (:y position))))

(defn positions
  "Returns a lazy sequence containing the positions at which pred
   is true for items in coll."
  [pred coll]
  (first (for [[idx elt] (indexed coll) :when (pred elt)] idx)))

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

(defn buildNode
  "constructs a new node"
  ([position]
   (Node. (generateNodeID) (getPosition position) default-node-settings default-node-data))

  ([position settings]
   (Node. (generateNodeID) (getPosition position) settings default-node-data))

  ([position settings data]
   (Node. (generateNodeID) (getPosition position) settings data)))

(defn createLine
  "creates a node list containg 2 nodes"
  ([pos1 pos2]
   (vector
    (buildNode {:x (:x pos1) :y (:y pos1)})
    (buildNode {:x (:x pos2) :y (:y pos2)})))

  ([pos1 pos2 settings]
   (vector
    (buildNode {:x (:x pos1) :y (:y pos1)} settings)
    (buildNode {:x (:x pos2) :y (:y pos2)} settings)))

  ([pos1 pos2 settings data]
   (vector
    (buildNode {:x (:x pos1) :y (:y pos1)} settings data)
    (buildNode {:x (:x pos2) :y (:y pos2)} settings data))))

(defn createLinePath
  "builds a path that is made up of a line"
  [pos1 pos2]
  (buildPath (createLine pos1 pos2)))

(defn buildSubPaths
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
             (if (<= (count fixed-nodes) 1)
               (conj new-paths aged-path)
               (let [fixed-node-positions (fixedPositions #{true} nodes)]
                 (into new-paths
                       (reduce
                        (fn [sub-path-coll fixed-index]
                          (conj sub-path-coll
                                (buildPath
                                 (subvec nodes (get fixed-node-positions fixed-index) (+ (get fixed-node-positions (+ fixed-index 1)) 1))
                                 (:settings path) (:data path))))
                        []
                        (range (- (count fixed-node-positions) 1)))))))))))
   []
   (range (count paths))))

(defn splitHorizontalDistance
  "splits a given length into a vec of smaller lengths"
  [length count]
  (vec (distinct (sort (vec (take count (repeatedly #(rand-int length))))))))

(defn moveNodeXPositionRight
  "adjust's the given node's X position right by the given distance"
  [node distance]
  (update-in node [:position] assoc :x (+ (:x (:position node)) distance)))

(defn moveNodeXPositionLeft
  "adjust's the given node's X position left by the given distance"
  [node distance]
  (update-in node [:position] assoc :x (- (:x (:position node)) distance)))

(defn moveNodeYPositionUp
  "adjust's the given node's Y position up by the given distance"
  [node distance]
  (update-in node [:position] assoc :y (- (:y (:position node)) distance)))

(defn moveNodeYPositionDown
  "adjust's the given node's Y position down by the given distance"
  [node distance]
  (update-in node [:position] assoc :y (+ (:y (:position node)) distance)))

(defn getMidpointNode
  "retrieves the middle node between two nodes"
  [node-A node-B settings is-random is-fixed]
  (let [new-x (/ (+ (:x (:position node-A)) (:x (:position node-B))) 2)
        new-y (/ (+ (:y (:position node-A)) (:y (:position node-B))) 2)]
    (buildNode {:x new-x :y new-y} settings
               (assoc default-node-data
                      :is-fixed is-fixed
                      :is-random is-random))))

(defn removeFixed
  "removes all fixed nodes"
  [nodes]
  (filterv #(or (not (:is-fixed (:data %))) (:is-end (:data %))) nodes))

(defn setAllNodesToFixed
  "sets all nodes in a given path to is-fixed: true"
  [path]
  (assoc-in path [:nodes]
            (mapv (fn [node]
                    (update-in node [:data] assoc :is-fixed true))
                  (:nodes path))))

(defn removeSection
  "removes a given section of the path"
  [vec startIndex endIndex]
  (apply conj (subvec vec 0 startIndex) (subvec vec endIndex)))

(defn eject
  "remove nodes from a specific index"
  [nodes pos]
  (apply conj
         (if (= pos 1)
           (subvec nodes 0)
           (subvec nodes 0 (- pos 1)))
         (subvec nodes pos)))

(defn addNode
  "atomically adds a node to node-map"
  [node-map node]
  (swap! node-map assoc-in [:nodes] (conj (@node-map :nodes) node)))

(defn addPath
  "adds a given path to path-map"
  [path-map path]
  (swap! path-map assoc-in [:paths] (conj (:paths @path-map) path)))

(defn addPaths
  "adds a given number of paths"
  [path-map paths]
  (doseq [path paths]
    (addPath path-map path)))

(defn dividePathsOnHorizontalLine
  "divides path into new subpaths at every point that intersects with a given height"
  [path height]
  (let [nodes (:nodes path)]
    (reduce
     (fn [new-nodes node-index]
       (let [node (get nodes node-index)
             connected-nodes (getConnectedNodes nodes node-index (:is-closed (:data path)))
             next-node (:next connected-nodes)]
         (if (not= next-node nil)
           (if (or
                (and (<= (:y (:position node)) height) (>= (:y (:position next-node)) height))
                (and (>= (:y  (:position node)) height) (<= (:y (:position next-node)) height)))
             (conj new-nodes (update-in node [:data] assoc :is-fixed true :is-end true))
             (conj new-nodes node))
           (conj new-nodes node))))
     []
     (range (count nodes)))))

(defn dividePathsOnVerticalLine
  "divides path into new subpaths at every point that intersects with a given height"
  [path verticle]
  (let [nodes (:nodes path)]
    (reduce
     (fn [new-nodes node]
       (if (not= (get (:position nodes) 0) verticle)
         (conj new-nodes node)
         (conj new-nodes (update-in node [:data] assoc :is-fixed true))))
     []
     nodes)))

(defn convertPathToLines
  "takes a given path and returns a vector of lines representing each node plus the node that follows it on the path"
  [path]
  (println "converting path to lines")
  (filterv (fn [x] (not (nil? x)))
   (for [node-index (range (count (:nodes path)))]
     (when (not= node-index (- (count (:nodes path)) 1))
       (let [point-a (:position (get (:nodes path) node-index))
             point-b (:position (get (:nodes path) (+ node-index 1)))]
         {:point-a point-a :point-b point-b})))))
