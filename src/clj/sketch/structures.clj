(ns sketch.structures)

;; A namespace for all compatible data structures
;; Currently not used and exists as a start to reorganizing the app

;; Used as a global means to give unique IDs to new paths
(def pathIDCounter (atom 0))

;; Used as a global means to give unique IDs to new nodes
(def nodeIDCounter (atom 0))

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
  (hash-map :age 0))

(def default-path-settings
  "Settings to be included with a path not provided with any"
  (hash-map 
   :fill-color nil
   :stroke-color nil
   :draw-edges true
   :draw-nodes false
   :draw-fixed-nodes false
   :draw-all-random-injections? false
   :draw-new-random-injections? false
   :bug-finder-mode? true
   :uniform-node-settings? false))

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

;; ------------ Nodes ---------------
(defrecord Node [ID position settings data])

(defrecord NodeData [next-position age velocity])

(defrecord Position2D [x y])

(defrecord Position3D [x y z])

(def default-node-data
  "Data to be included with a node not provided with any"
  (hash-map :age 0))

(def default-node-settings
  "default setting to be included with a node where settings is set to nil"
  (hash-map
   :is-fixed false
   :is-end false
   :is-random false
   :fill-color nil
   :stroke-color nil
   :draw-edges true
   :draw-nodes true
   :draw-fixed-nodes false
   :draw-all-random-injections? false
   :bug-finder-mode? true
   :uniform-node-settings? false))

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

(defn buildNode
  "constructs a new node"
  ([position]
   (Node. (generateNodeID) (getPosition position) default-node-settings default-node-data)) 
  
  ([position settings]
   (Node. (generateNodeID) (getPosition position) settings default-node-data))
  
  ([position settings data]
   (Node. (generateNodeID) (getPosition position) settings data)))
