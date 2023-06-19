(ns sketch.tree
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]]
            [sketch.calculations :as calc]
            [sketch.grow :as grow]
            [sketch.path :as path])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])
  (:import [processing.core PShape PGraphics]))

;; ------ Definitions ----------------

(def default-tree-path-data
  (hash-map
   :type "tree"
   :age 1
   :branch-rate 50
   :branch-angle 0
   :branch-height-minimum 8
   :total-branch-count 0
   :max-branching 1
   :node-injection-interval 10
   :is-hardend false
   :is-mature false
   :is-divide-ready false
   :is-fixed false
   :is-closed false))

(def default-tree-node-data
  (hash-map
   :age 0
   :growth-iteration-count 0
   :parent-node-id nil
   :min-distance 0
   :max-distance 4.5
   :velocity 0
   :next-position nil
   :branch-rate 0
   :branch-angle 0
   :branch-height-minimum 8
   :branch-count 0
   :max-branching 1
   :side nil
   :distance-from-top 0
   :delay-growth-by 0
   :repulsion-radius 0
   :max-velocity 0
   :attraction-force 0
   :repulsion-force 0
   :allignment-force 0
   :node-injection-interval 0
   :brownian-motion-range 0
   :is-fixed false
   :is-end false
   :is-tip false
   :is-branch-ready false
   :is-random false
   :to-remove false
   :is-bottom false
   :is-hardend false))

(def default-tree-node-settings
  (hash-map
   :fill-color nil
   :stroke-color nil
   :draw-edges true
   :draw-nodes true
   :draw-fixed-nodes false
   :draw-all-random-injections? false
   :bug-finder-mode? true
   :uniform-node-settings? false))

(def default-tree-path-settings
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

;; ------------ Init Tree Functions -----------------

(def counter (atom 0))

(defn buildBottomNode
  "builds an unmovable fixed node on the bottom of the tree"
  [node position side fixed]
  (path/buildNode
   (path/getPosition position) 
   default-tree-node-settings
   (assoc default-tree-node-data
           :side side
           :is-fixed fixed
           :distance-from-top (inc (:distance-from-top (:data node))))))

(defn injectSeedsOnOnePath
  "adds a given count of nodes randomly dispersed to a single path"
  [path seed-count] 
  (let [new-path (atom path)
        nodes (:nodes path)
        first-node (get nodes 0)
        length (count nodes) ;
        last-node (get nodes (- length 1))
        distance (path/getDistance first-node last-node)
        seeds (vec (distinct (sort (vec (take seed-count (repeatedly #(rand-int distance)))))))
        settings (if (:uniform-node-settings? (:settings path))
                   (:settings path)
                   (:settings first-node))]
    (doseq [node-index (range (+ (count seeds) 2))]
      (when (and (not= node-index 0)
                 (not= node-index (+ (count seeds) 1)))
        (let [connected-nodes (path/getConnectedNodes (:nodes @new-path) node-index (:is-closed (:data @new-path)))
              next-node (:next connected-nodes)
              previous-node (:prev connected-nodes)
              new-x (get seeds (- node-index 1))
              new-y (:y (:position previous-node)) 
              new-node (path/buildNode {:x new-x :y new-y} settings default-tree-node-data)
              new-node (update-in new-node [:data] assoc
                                  :max-distance 3
                                  :branch-angle 0 
                                  :it-tip true
                                  :is-branch-ready true
                                  :branch-rate 50
                                  :side "top"
                                  :delay-growth-by (rand-int 200)
                                  :parent-node-id 0)] 
          (swap! new-path assoc-in [:nodes] (path/insert (:nodes @new-path) node-index new-node)))))
    @new-path))

(defn seed-tree
  "creates a tree path comprised of a line and seeds it with a given number of nodes"
  [pos1 pos2 seed-count]
  (let [path (path/createLinePath pos1 pos2)
        tree-path (update-in path [:data] assoc :type "tree")]
    (injectSeedsOnOnePath tree-path seed-count)))

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
     (:branch-height-minimum (:data node))))

(defn branchCountMaxed?
  "checks if branch-count has exceeded max-branching"
  [node]
  (>= (:branch-count (:data node))
      (:max-branching (:data node))))

(defn matured?
  "checks if node age has exceeded branch-height-minimum"
  [node]
  (> (:age (:data node))
     (:branch-height-minimum (:data node))))

(defn isFixed?
  "checks if node age has exceeded branch-height-minimum"
  [node]
  (:is-fixed (:data node)))

(defn reachedMaxDistanceFromTop?
  "determines if node distance from top has exceeded max distance"
  [node]
  (> (:distance-from-top (:data node))
     (:max-distance (:data node))))

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
  (update-in node [:data] assoc :branch-angle angle))

(defn setBranchHeightMinimum
  "sets the branch height minimum to a factor of 2 of what it currently is"
  [node]
  (update-in node [:data] assoc :branch-height-minimum (* 2 (:branch-height-minimum (:data node)))))

(defn setParentNodeID
  "sets the parent node ID in the given node to the nodeID that it branched from"
  [node parentNode]
  (update-in node [:data] assoc :parent-node-id (:id parentNode)))

(defn getNewRightX
  "retrives the new x pos for the right side of a tree"
  [node]
  (+ (:x (:position node)) 2))

(defn getNewLeftX
  "retrives the new x pos for the left side of a tree"
  [node]
  (- (:x (:position node)) 2))

(defn getNewRightY
  "retrives the new y pos for the right side of a tree"
  [node]
  (:y (:position node)))

(defn getNewLeftY
  "retrives the new y pos for the left side of a tree"
  [node]
  (:y (:position node)))

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
    (assoc-in path [:nodes] (path/insert (:nodes path) (- node-index 1) node))
    path))

(defn insertNodeRight
  "inserts a node to the right of a node"
  [path node node-index]
  (if (and (> node-index 0)
           (< node-index (- (count (:nodes path)) 1))
           (not (:is-end node))) 
    (assoc-in path [:nodes] (path/insert (:nodes path) node-index node))
    path))

(defn getTopNodes
  "returns a vec with indexes of al nodes marked 'top'"
  [nodes] 
  (let [mi-nodes (map-indexed vector nodes)]
    (filterv #(= (:side (:data (second %))) "top") mi-nodes)))

(defn getNextRightBottomNodeIndex
  "returns the index of the next node marked 'bottom' of the right side of the tree"
  [nodes]
  (let [indexs-and-nodes (map-indexed vector nodes)]
    (first (mapv #(:is-bottom (:data (second %))) indexs-and-nodes))))

(defn getNextLeftBottomNodeIndex
  "returns the index of the next node marked 'bottom' of the left side of the tree"
  [nodes]
  (let [indexs-and-nodes (map-indexed vector nodes)]
    (last (mapv #(:is-bottom (:data (second %))) indexs-and-nodes))))

(defn getRightNodesInTree
  "returns a vec of all right side nodes, in order from bottom node to the top node"
  [nodes top-node-index]
  (let [bottom-node-index (getNextRightBottomNodeIndex (subvec nodes top-node-index))]
   (subvec nodes (- top-node-index 1) bottom-node-index)))

(defn getLeftNodesInTree
  "returns a vec of all left side nodes, in order from bottom node to the top node"
  [nodes top-node-index]
  (let [bottom-node-index (getNextLeftBottomNodeIndex (subvec nodes 0 (+ top-node-index 1)))]
    (subvec nodes (- bottom-node-index 1) top-node-index)))

(defn nodesOverlap?
  "checks if the given left and right nodes have overlapping x-coords"
  [left-node right-node]
  (< (:x (:position right-node)) (:x (:position left-node))))

(defn getNonOverlappinNodes
  "returns a vec of nonoverlapping nodes representing the valley between tree top to tree top"
  [nodes-a nodes-b]
;; (println (map (fn [x] (:position x)) nodes-a))
  ;; (println "na:" (map (fn [x] [(:x (:position x)) (:y (:position x))]) nodes-b))
  ;; (println "nb:" (map (fn [x] [(:x (:position x)) (:y (:position x))]) nodes-b))
  ;; (println "nb count:" (count (map (fn [x] [(:x (:position x)) (:y (:position x))]) nodes-b)))
  ;; (println "concat na nb:" (mapv (fn [x] [(:x (:position x)) (:y (:position x))]) (concat nodes-a nodes-b)))
;; (println "fn fn:" (mapv (fn [x] [(:x (:position x)) (:y (:position x))]) (mapv (fn [a]
;;                                                                                  (mapv (fn [b]
;;                                                                                          (when (nodesOverlap? a b)
;;                                                                                            [a b])) nodes-b)) nodes-a)))
  (let [combined-nodes (into [] (concat nodes-a nodes-b))]
    (filterv #(not (some (fn [u] (= (:x (:position u)) (:x (:position %))))
                        (vec
                         (remove nil?
                                 (set
                                  (into []
                                        (flatten
                                         (mapv (fn [a]
                                                 (mapv (fn [b]
                                                         (when (nodesOverlap? a b)
                                                           [a b])) nodes-b)) nodes-a)))))))) combined-nodes)))

(defn removeOverLappingTreeNodes
  "removes the sections of the path that overlap"
  [path]
  (let [nodes (:nodes path)
        top-node-positions (getTopNodes nodes)
        new-path (atom path)
        fst 0
        lst (- (count top-node-positions) 1)]
    (swap! new-path assoc-in [:nodes] [])
    (doseq [index (range (count top-node-positions))] ;; this need to be a count of spans between tops, not tops
      (println "idx" index)
      (println "cnt" (range (count top-node-positions)))
      (case index
        0 (do
            (println "fst")
            (println [(first nodes)])
            (println (map (fn [x] (first x)) top-node-positions))
            (println (first (get top-node-positions index)))
            (println @new-path)
            (swap! new-path assoc-in [:nodes] (into [] (concat (:nodes @new-path) (getNonOverlappinNodes
                                                                                   [(first nodes)]
                                                                                   (subvec nodes 0 (first (get top-node-positions index))))))))
        (- (count top-node-positions) 1) (do
                                           (println "lst")
                                           (swap! new-path assoc-in [:nodes] (into [] (concat (:nodes @new-path) (getNonOverlappinNodes
                                                                                                                  (subvec nodes (first (get top-node-positions index)))
                                                                                                                  [(last nodes)])))))
        (do
          (println "default")
          (getNonOverlappinNodes
           (subvec nodes (first (get top-node-positions index)))
           (subvec nodes (getNextLeftBottomNodeIndex (subvec nodes (first (get top-node-positions index))))))
          )))
    @new-path))

(defn branch
  "causes tree structure to branch"
  [path]
  (let [new-path (atom path)]
    (swap! new-path assoc-in [:nodes] [])
    (doseq [node-index (range (count (:nodes path)))]
      (let [node (get (:nodes path) node-index)]
        (if (not (isFixed? node))
          (if (or (isTop? node)
                  (isLeftSide? node)
                  (isRightSide? node))
            
            (if (and (branchReady? node)
                     (growthDelayComplete? node)) 
              (let [connected-nodes (path/getConnectedNodes (:nodes path) node-index (:is-closed (:data path)))
                    new-left-x (getNewLeftX node)
                    new-left-y (getNewLeftY node)
                    new-right-x (getNewRightX node)
                    new-right-y (getNewRightY node)
                    left-node (if (and (isBottom? (:prev connected-nodes))
                                       (isFixed? (:prev connected-nodes)))
                                (buildBottomNode node {:x new-left-x :y new-left-y} "left" true)
                                (buildBottomNode node {:x new-left-x :y new-left-y} "left" false))
                    left-node (setBranchAngle left-node 15)
                    left-node (setBranchHeightMinimum left-node)
                    left-node (setParentNodeID left-node node)
                    right-node (if (and (isBottom? (:next connected-nodes))
                                        (isFixed? (:next connected-nodes)))
                                 (buildBottomNode node {:x new-right-x :y new-right-y} "right" true)
                                 (buildBottomNode node {:x new-right-x :y new-right-y} "right" false))
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
  (let [new-path (atom (path/incPathAge path))] 
    (swap! new-path assoc-in [:nodes] [])
    (doseq [node-index (range (count (:nodes path)))] 
      (let [node (get (:nodes path) node-index)]
        (if (not (isFixed? node))
          (do
            (when (isTop? node)
              (if (and (not (branchReady? node))
                       (growthDelayComplete? node))
                (let [updated-node (path/moveNodeYPositionUp node top-rate)
                      updated-node (grow/incGrowthCount updated-node)
                      updated-node (path/incNodeAge updated-node)
                      updated-node (if (minimumGrowthAchieved? updated-node)
                                     (makeBranchReady (grow/resetGrowthCount updated-node))
                                     updated-node)]

                  (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) updated-node)))
                (let [updated-node (path/incNodeAge node)]
                  (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) updated-node)))))

            (when (isLeftSide? node)
              (if (isBottom? node)
                (let [updated-node (grow/incGrowthCount node)
                      updated-node (path/incNodeAge updated-node)
                      updated-node (path/moveNodeXPositionLeft (grow/resetGrowthCount updated-node) (/ side-rate 10))]
                  (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) updated-node)))
                (if (not (branchReady? node))
                  (let [updated-node (if (matured? node)
                                       (if (not (reachedMaxDistanceFromTop? node))
                                         (let [updated-node (path/moveNodeXPositionLeft node (/ side-rate (+ (rand-int 10) 4)))
                                               updated-node (path/moveNodeYPositionUp updated-node (/ side-rate (+ (rand-int 5) 2)))]
                                           updated-node)
                                         (let [updated-node (path/moveNodeXPositionLeft node (/ side-rate (+ (rand-int 8) 5)))
                                               updated-node (path/moveNodeYPositionUp updated-node (/ side-rate (+ (rand-int 5) 2)))]
                                           updated-node))
                                       node)
                        updated-node (grow/incGrowthCount updated-node)
                        updated-node (path/incNodeAge updated-node)
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
                      updated-node (path/incNodeAge updated-node)
                      updated-node (path/moveNodeXPositionRight (grow/resetGrowthCount updated-node) (/ side-rate 10))]
                  (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) updated-node)))
                (if (not (branchReady? node))
                  (let [updated-node (if (matured? node)
                                       (if (not (reachedMaxDistanceFromTop? node))
                                         (let [updated-node (path/moveNodeXPositionRight node (/ side-rate (+ (rand-int 10) 4)))
                                               updated-node (path/moveNodeYPositionUp updated-node (/ side-rate (+ (rand-int 5) 2)))]
                                           updated-node)
                                         (let [updated-node (path/moveNodeXPositionRight node (/ side-rate (+ (rand-int 8) 5)))
                                               updated-node (path/moveNodeYPositionUp updated-node (/ side-rate (+ (rand-int 5) 2)))]
                                           updated-node))
                                       node)
                        updated-node (grow/incGrowthCount updated-node)
                        updated-node (path/incNodeAge updated-node)
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


;; --------- Primary Growth Iterator Functions ---------------
(def i (atom 0))
(defn applyTreeGrowth
  [paths width height]
  
  (let [new-paths (atom paths)]
    
    (doseq [path-index (range (count @new-paths))]

      (swap! new-paths assoc-in [path-index] (branch (get @new-paths path-index)))

      (swap! new-paths assoc-in [path-index] (grow (get @new-paths path-index) 4 3))

      (when (and (= @i 0)
                 (> (:age (:data (get @new-paths path-index))) 400))
        (swap! new-paths assoc-in [path-index] (removeOverLappingTreeNodes (get @new-paths path-index)))
        (swap! new-paths assoc-in [path-index] (path/setAllNodesToFixed (get @new-paths path-index)))
        (swap! i inc))
      (swap! new-paths assoc-in [path-index] (path/incPathAge (get @new-paths path-index))))
    
    @new-paths))
