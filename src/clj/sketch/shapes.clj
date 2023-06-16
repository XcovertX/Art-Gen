(ns sketch.shapes
  (:require [quil.core :refer :all]
            [sketch.structures :as sture]
            [clojure.java.shell :refer [sh]]
            [sketch.calculations :as calc])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])
  (:import  [sketch.structures Path])
  (:import  [sketch.structures Node])
  (:import  [sketch.structures Position2D])
  (:import  [sketch.structures Position3D])
  (:import [processing.core PShape PGraphics]))

(defn generatePathID
  "generates a new unique path id"
  []
  (reset! sture/pathIDCounter (inc @sture/pathIDCounter)))

(defn buildPath
  "builds a path"
  ([nodes]
   (sture/Path. (generatePathID) nodes sture/default-path-settings sture/default-path-data))

  ([nodes settings]
   (sture/Path. (generatePathID) nodes settings sture/default-path-data))

  ([nodes settings data]
   (sture/Path. (generatePathID) nodes settings data)))



(defn insert
  "inserts node into a specific index of a path"
  [vec pos item]
  (apply conj (subvec vec 0 pos) item (subvec vec pos)))

(defn generateNodeID
  "generates a new unique node id"
  []
  (reset! sture/nodeIDCounter (inc @sture/nodeIDCounter)))

(defn getPosition
  "generates 2D or 3D position based on give parameter"
  [position]
  (if (:z position)
    (sture/Position3D. (:x position) (:y position) (:z position))
    (sture/Position2D. (:x position) (:y position))))

(defn buildNode
  "constructs a new node"
  ([position]
   (sture/Node. (generateNodeID) (getPosition position) sture/default-node-settings sture/default-node-data))

  ([position settings]
   (sture/Node. (generateNodeID) (getPosition position) settings sture/default-node-data))

  ([position settings data]
   (sture/Node. (generateNodeID) (getPosition position) settings data)))

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

(defn addLineToPath
  "adds a line to the end of a given path"
  [path line]
  (assoc-in path [:nodes] (conj (:nodes path) (:nodes line))))

(defn createRectangle
  "builds a square or rectangle"
  ([length]
   (reset! sture/nodeIDCounter 0) ;; temporary for testing
   (buildPath [(buildNode {:x 0 :y length})
               (buildNode {:x length :y length})
               (buildNode {:x length :y 0})
               (buildNode {:x 0 :y 0})]))

  ([length width]
   (reset! sture/nodeIDCounter 0) ;; temporary for testing
   [(buildPath [(buildNode {:x 0 :y width})
                (buildNode {:x length :y width})
                (buildNode {:x length :y 0})
                (buildNode {:x 0 :y 0})])])

  ([length width center]
   (reset! sture/nodeIDCounter 0) ;; temporary for testing
   [(buildPath (let [x-min (- (:x center) (/ length 2))
                     x-max (+ (:x center) (/ length 2))
                     y-min (- (:y center) (/ width 2))
                     y-max (+ (:y center) (/ width 2))]
                 [(buildNode {:x x-min :y y-max})
                  (buildNode {:x x-max :y y-max})
                  (buildNode {:x x-max :y y-min})
                  (buildNode {:x x-min :y y-min})]))]))

(defn adjustRectangle
  "adjusts the length and width of a rectangle"
  [path length width center]
  (let [new-path (atom path)
        nodes (:nodes path)
        ;; center {:x (/ length 2) :y (/ width 2)}
        x-min (- (:x center) (/ length 2))
        x-max (+ (:x center) (/ length 2))
        y-min (- (:y center) (/ width 2))
        y-max (+ (:y center) (/ width 2))]
    ;; (println length width center x-min x-max y-min)
    (swap! new-path assoc-in [:nodes] [])
    (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path)
                                            (assoc-in (get nodes 0) [:position] (getPosition {:x x-min :y y-max}))
                                            (assoc-in (get nodes 1) [:position] (getPosition {:x x-max :y y-max}))
                                            (assoc-in (get nodes 2) [:position] (getPosition {:x x-max :y y-min}))
                                            (assoc-in (get nodes 3) [:position] (getPosition {:x x-min :y y-min}))))))


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