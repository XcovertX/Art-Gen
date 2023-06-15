(ns sketch.tree
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]]
            [sketch.calculations :as calc]
            [sketch.grow :as grow])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])
  (:import [processing.core PShape PGraphics]))

;; ------ Notes ----------------------
;; make option to choose connected nodes with no overlap
;; make multiple path option where each tree can be on individual paths
;; add noise adjusting function to make branches bigger or smaller


;; ------ Definitions ----------------
(defrecord Path [nodes
                 settings
                 is-closed
                 bounds
                 brownian
                 alignment
                 node-injection-interval
                 is-fixed
                 age
                 is-mature
                 is-divide-ready])

(defrecord Node [pos settings data lifespan id parent-node-id])
(defrecord Data [is-fixed
                 is-end
                 is-tip
                 is-branch-ready
                 to-remove
                 is-random
                 velocity
                 next-position
                 growth-iteration-count
                 age
                 side
                 is-bottom
                 branch-count
                 distance-from-top
                 delay-growth-by])

(def default-tree-node-settings (hash-map
                                 :min-distance 2.1
                                 :max-distance 2
                                 :branch-rate 50 
                                 :branch-angle 0
                                 :branch-height-minimum 8
                                 :max-branching 1
                                 :node-injection-interval 1
                                 :brownian-motion-range 0.05
                                 :fill-color nil
                                 :stroke-color nil
                                 :draw-edges true
                                 :draw-nodes true
                                 :draw-fixed-nodes false
                                 :draw-all-random-injections? false
                                 :bug-finder-mode? true
                                 :uniform-node-settings? false
                                 :hardend? false))

(def default-tree-path-settings (hash-map
                    :min-distance 3
                    :max-distance 6
                    :repulsion-radius 7
                    :max-velocity 0.01
                    :attraction-force 0.01
                    :repulsion-force 20
                    :allignment-force 0.001
                    :node-injection-interval 10
                    :brownian-motion-range 0.25
                    :fill-color nil
                    :stroke-color nil
                    :draw-edges true
                    :draw-nodes false
                    :draw-fixed-nodes false
                    :draw-all-random-injections? false
                    :draw-new-random-injections? false
                    :bug-finder-mode? true
                    :uniform-node-settings? false
                    :hardend? false))

;; ------------ Init Tree Functions -----------------

(def counter (atom 0))

(defn buildNode
  "constructs a new node"
  [x y settings is-fixed is-end is-tip is-branch-ready to-remove is-random side is-bottom distance-from-top delay-growth-by]
  (reset! counter (+ @counter 1)) 
  (Node. [x y]
         settings
         (Data. is-fixed is-end is-tip is-branch-ready to-remove is-random 0 {:x x :y y} 0 0 side is-bottom 0 distance-from-top delay-growth-by)
         0
         @counter
         -1))

(defn buildBottomFixedNode
  "builds an unmovable fixed node on the bottom of the tree"
  [node x y side]
  (buildNode x y default-tree-node-settings
             false false false false false false side true (+ (:distance-from-top (:data node)) 1) 0))

(defn buildBottomNode
  "builds a movable node on the bottom of the tree"
  [node x y side]
  (buildNode x y default-tree-node-settings
             false false false false false false side false (+ (:distance-from-top (:data node)) 1) 0))

(defn buildPath
  "builds a path"
  [nodes settings is-closed bounds brownian alignment node-injection-interval is-fixed]
  (Path. nodes settings is-closed bounds brownian alignment node-injection-interval is-fixed 2 false false))

(defn createLine
  "creates a node list containg 2 nodes"
  [pos1 pos2]
  (vector
   (buildNode (get pos1 0) (get pos1 1) default-tree-node-settings true true false false false false "left" true 0 0)
   (buildNode (get pos2 0) (get pos2 1) default-tree-node-settings true true false false false false "right" true 0 0)))

(defn addLinePath
  [pos1 pos2]
  (buildPath (createLine pos1 pos2) default-tree-path-settings false [] true 0.45 10 false))

(defn injectSeedsOnOnePath
  "adds a given count of nodes randomly dispersed to a single path"
  [path seed-count]
  (let [new-path (atom path)
        nodes (:nodes path)
        node-first (get nodes 0)
        length (- (count nodes) 1)
        node-last (get nodes length)
        distance (grow/getDistance node-first node-last)
        seeds (vec (distinct (sort (vec (take seed-count (repeatedly #(rand-int distance)))))))
        settings (if (:uniform-node-settings? (:settings path))
                   (:settings path)
                   (:settings node-first))]
    (doseq [node-index (range (+ (count seeds) 2))]
      (when (and (not= node-index 0)
                 (not= node-index (+ (count seeds) 1)))
        (let [connected-nodes (grow/getConnectedNodes (:nodes @new-path) node-index (:is-closed @new-path))
              next-node (:next connected-nodes)
              previous-node (:prev connected-nodes)
              new-y (get (:pos previous-node) 1)
              new-x (get seeds (- node-index 1))
              new-node (buildNode new-x new-y settings false false true true false false "top" false 0 (rand-int 200))
              updated-new-node (update-in new-node [:settings] assoc :branch-angle 0)
              updated-new-node (update updated-new-node :parent-node-id inc)] 
          (swap! new-path assoc-in [:nodes] (grow/insert (:nodes @new-path) node-index updated-new-node)))))
    @new-path))

(defn seed-tree
  "initializes tree growth"
  [w h seed-count]
  (let [p-1 [(grow/createTriangle 50 50 (- w 50) 50 (/ w 2) (- h 50))]
        p-2 [(addLinePath [0 (/ h 2)] [w (/ h 2)])]
        p-3 [(grow/createCirclePath 150 100 200 150 150 200 100 150)]
        p-4 [(addLinePath [0 400] [w 800])]
        p-5 [(addLinePath [0 (- h 10)] [w (- h 10)])]
        p-6 [(addLinePath [0 (- h 50)] [w (- h 50)])]
        p-7 [(addLinePath [0 (+ h 300)] [w (+ h 300)])]
        path-1 (injectSeedsOnOnePath (get p-6 0) seed-count)]
    [path-1]))

(defn makeBranchReady
  "this marks a node as 'ready to branch'"
  [node]
  (update-in node [:data] assoc :is-branch-ready true))

(defn makeBranchUnready
  "this marks a node as not 'ready to branch'"
  [node]
  (update-in node [:data] assoc :is-branch-ready false))

(defn branchReady?
  "checks if node has been marked ready to branch"
  [node]
  (:is-branch-ready (:data node)))

(defn minimumGrowthAchieved?
  "checks if growth-iteration-count has exceeded branch-height-minimum"
  [node]
  (> (:growth-iteration-count (:data node))
     (:branch-height-minimum (:settings node))))

(defn branchCountMaxed?
  "checks if branch-count has exceeded max-branching"
  [node]
  (>= (:branch-count (:data node))
      (:max-branching (:settings node))))

(defn matured?
  "checks if node age has exceeded branch-height-minimum"
  [node]
  (> (:age (:data node))
     (:branch-height-minimum (:settings node))))

(defn isFixed?
  "checks if node age has exceeded branch-height-minimum"
  [node]
  (:is-fixed (:data node)))

(defn reachedMaxDistanceFromTop?
  "determines if node distance from top has exceeded max distance"
  [node]
  (> (:distance-from-top (:data node))
     (:max-distance (:settings node))))

(defn isLeftSide?
  "checks if given node is ont the left side of the tree"
  [node]
  (= (:side (:data node)) "left"))

(defn isTop?
  "checks if given node is the top if the tree"
  [node]
  (= (:side (:data node)) "top"))

(defn isBottom?
  "checks if given node is on the bottom if the tree"
  [node]
  (:is-bottom (:data node)))

(defn isRightSide?
  "checks if given node is ont the right side of the tree"
  [node]
  (= (:side (:data node)) "right"))

(defn growthDelayComplete?
  "checks if age has exceeded growth-delay"
  [node]
  (> (:age (:data node))
     (:delay-growth-by (:data node))))

(defn setBranchAngle
  "sets the branch growth angle of a given node"
  [node angle]
  (update-in node [:settings] assoc :branch-angle angle))

(defn setBranchHeightMinimum
  "sets the branch height minimum to a factor of 2 of what it currently is"
  [node]
  (update-in node [:settings] assoc :branch-height-minimum (* 2 (:branch-height-minimum (:settings node)))))

(defn setParentNodeID
  "sets the parent node ID in the given node to the nodeID that it branched from"
  [node parentNode]
  (assoc node :parent-node-id (:id parentNode)))

(defn getNewRightX
  "retrives the new x pos for the right side of a tree"
  [node]
  (+ (get (:pos node) 0) 2))

(defn getNewLeftX
  "retrives the new x pos for the left side of a tree"
  [node]
  (- (get (:pos node) 0) 2))

(defn getNewRightY
  "retrives the new y pos for the right side of a tree"
  [node]
  (+ (get (:pos node) 1) 0))

(defn getNewLeftY
  "retrives the new y pos for the left side of a tree"
  [node]
  (+ (get (:pos node) 1) 0))

(defn incBranchCount
  "increments a given node'sbranch count"
  [node]
  (update-in node [:data :branch-count] inc))


;; --------- Branching functions --------
(defn insertNodeLeft
  "inserts a node to the left of a node"
  [path node node-index]
  (if (and (> node-index 0)
           (< node-index (- (count (:nodes path)) 1))
           (not (:is-end node)))
    (assoc-in path [:nodes] (grow/insert (:nodes path) (- node-index 1) node))
    path))

(defn insertNodeRight
  "inserts a node to the right of a node"
  [path node node-index]
  (if (and (> node-index 0)
           (< node-index (- (count (:nodes path)) 1))
           (not (:is-end node))) 
    (assoc-in path [:nodes] (grow/insert (:nodes path) node-index node))
    path))

(defn branch
  "causes tree structure to branch"
  [path]
  (let [new-path (atom path)]
    (swap! new-path assoc-in [:nodes] [])
    (doseq [node-index (range (count (:nodes path)))]
      (let[node (get (:nodes path) node-index)]
       (if (not (isFixed? node))
        (if (or (isTop? node)
                (isLeftSide? node)
                (isRightSide? node))
          (if (and (branchReady? node)
                   (growthDelayComplete? node))
            (let [connected-nodes (grow/getConnectedNodes (:nodes path) node-index (:is-closed path))
                  new-left-x (getNewLeftX node)
                  new-left-y (getNewLeftY node)
                  new-right-x (getNewRightX node)
                  new-right-y (getNewRightY node)
                  left-node (if (and (isBottom? (:prev connected-nodes))
                                     (isFixed? (:prev connected-nodes)))
                              (buildBottomFixedNode node new-left-x new-left-y "left")
                              (buildBottomNode node new-left-x new-left-y "left"))
                  left-node (setBranchAngle left-node 15)
                  left-node (setBranchHeightMinimum left-node)
                  left-node (setParentNodeID left-node node)
                  right-node (if (and (isBottom? (:next connected-nodes))
                                      (isFixed? (:next connected-nodes)))
                               (buildBottomFixedNode node new-right-x new-right-y "right")
                               (buildBottomNode node new-right-x new-right-y "right"))
                  right-node (setBranchAngle right-node -15)
                  right-node (setBranchHeightMinimum right-node)
                  right-node (setParentNodeID right-node node)
                  updated-node (makeBranchUnready node)
                  updated-node (incBranchCount updated-node)]
              (when (or (isLeftSide? node) (isTop? node))
                (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) left-node)))
              (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) updated-node))
              (when (or (isRightSide? node) (isTop? node))
                (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) right-node))))
            (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) node)))
          (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) node)))
        (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) node)))))
    @new-path))

(defn grow
  "causes each tree branch to grow"
  [path top-rate side-rate]
  (let [new-path (atom path)]
    (swap! new-path update-in [] assoc :age (grow/incPathAge @new-path))
    (swap! new-path assoc-in [:nodes] [])
    (doseq [node-index (range (count (:nodes path)))]
      (let [node (get (:nodes path) node-index)]
       (if (not (isFixed? node))
         (do
           (when (isTop? node)
             (if (and (not (branchReady? node))
                      (growthDelayComplete? node))
               (let [updated-node (grow/moveNodeYPositionUp node top-rate)
                     updated-node (grow/incGrowthCount updated-node)
                     updated-node (grow/incNodeAge updated-node)
                     updated-node (if (minimumGrowthAchieved? updated-node)
                                    (makeBranchReady (grow/resetGrowthCount updated-node))
                                    updated-node)]
                 (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) updated-node)))
               (let [updated-node (grow/incNodeAge node)]
                 (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) updated-node)))))

           (when (isLeftSide? node)
             (if (isBottom? node)
               (let [updated-node (grow/incGrowthCount node)
                     updated-node (grow/incNodeAge updated-node)
                     updated-node (grow/moveNodeXPositionLeft (grow/resetGrowthCount updated-node) (/ side-rate 10))]
                 (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) updated-node)))
               (if (not (branchReady? node))
                 (let [updated-node (if (matured? node)
                                      (if (not (reachedMaxDistanceFromTop? node))
                                        (let [updated-node (grow/moveNodeXPositionLeft node (/ side-rate (+ (rand-int 10) 4)))
                                              updated-node (grow/moveNodeYPositionUp updated-node (/ side-rate (+ (rand-int 5) 2)))]
                                          updated-node)
                                        (let [updated-node (grow/moveNodeXPositionLeft node (/ side-rate (+ (rand-int 8) 5)))
                                              updated-node (grow/moveNodeYPositionUp updated-node (/ side-rate (+ (rand-int 5) 2)))]
                                          updated-node))
                                      node)
                       updated-node (grow/incGrowthCount updated-node)
                       updated-node (grow/incNodeAge updated-node)
                       updated-node (if (and
                                         (minimumGrowthAchieved? updated-node)
                                         (not (branchCountMaxed? updated-node)))
                                      (makeBranchReady (grow/resetGrowthCount updated-node))
                                      updated-node)
                       updated-node (if (reachedMaxDistanceFromTop? updated-node)
                                      (makeBranchUnready updated-node)
                                      updated-node)]
                   (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) updated-node)))
                 (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) node)))))

           (when (isRightSide? node)
             (if (isBottom? node)
               (let [updated-node (grow/incGrowthCount node)
                     updated-node (grow/incNodeAge updated-node)
                     updated-node (grow/moveNodeXPositionRight (grow/resetGrowthCount updated-node) (/ side-rate 10))]
                 (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) updated-node)))
               (if (not (branchReady? node))
                 (let [updated-node (if (matured? node)
                                      (if (not (reachedMaxDistanceFromTop? node))
                                        (let [updated-node (grow/moveNodeXPositionRight node (/ side-rate (+ (rand-int 10) 4)))
                                              updated-node (grow/moveNodeYPositionUp updated-node (/ side-rate (+ (rand-int 5) 2)))]
                                          updated-node)
                                        (let [updated-node (grow/moveNodeXPositionRight node (/ side-rate (+ (rand-int 8) 5)))
                                              updated-node (grow/moveNodeYPositionUp updated-node (/ side-rate (+ (rand-int 5) 2)))]
                                          updated-node))
                                      node)
                       updated-node (grow/incGrowthCount updated-node)
                       updated-node (grow/incNodeAge updated-node)
                       updated-node (if (and
                                         (minimumGrowthAchieved? updated-node)
                                         (not (branchCountMaxed? updated-node)))
                                      (makeBranchReady (grow/resetGrowthCount updated-node))
                                      updated-node)
                       updated-node (if (reachedMaxDistanceFromTop? updated-node)
                                      (makeBranchUnready updated-node)
                                      updated-node)]
                   (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) updated-node)))
                 (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) node))))))
         (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) node)))))
    @new-path))

(defn getTopNodes
  "returns a vec with indexes of al nodes marked 'top'"
  [nodes]
  (filterv #(= (:side (:data %)) "top") nodes))

(defn removeOverLappingTreeNodes
  "removes the sections of the path that overlap"
  [path low-x high-x]
  (let [nodes (:nodes path)
        new-nodes (atom [])
        top-node-positions (getTopNodes (:nodes path))]
    (doseq [index (range (count top-node-positions))]
      (when (= index 0)
        (swap! new-nodes assoc [] (filterv
                                   #(>= (get (:pos %) 0) low-x)
                                   (subvec nodes 0 (get top-node-positions index)))))
      
      )
    (mapv (fn [x] (* x x)) (range 1 10))))

;; --------- Primary Growth Iterator Functions ---------------

(defn applyTreeGrowth
  [paths width height]
  (let [new-paths (atom paths)]
    (doseq [path-index (range (count @new-paths))]
      (swap! new-paths assoc-in [path-index] (branch (get @new-paths path-index)))
      (swap! new-paths assoc-in [path-index] (grow (get @new-paths path-index) 4 3))
      (when (> (:age (get @new-paths path-index)) 300)
        (swap! new-paths assoc-in [path-index] (grow/setAllNodesToFixed (get @new-paths path-index))))
      (swap! new-paths assoc-in [path-index] (grow/incPathAge (get @new-paths path-index))))
    @new-paths))
