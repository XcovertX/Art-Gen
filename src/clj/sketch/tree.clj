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

(defn injectSeeds
  "adds a given count of seeds randomly dispersed"
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
              new-node (buildNode new-x new-y settings false false true true false false "top" false 0 (rand-int 350))
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
        p-4 [(addLinePath [0 0] [w h])]
        p-5 [(addLinePath [0 (- h 10)] [w (- h 10)])]
        path (injectSeeds (get p-5 0) seed-count)]
    [path]))

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
      (if (not (:is-fixed (:data (get (:nodes path) node-index))))
        (if (or (= (:side (:data (get (:nodes path) node-index))) "top")
                (= (:side (:data (get (:nodes path) node-index))) "left")
                (= (:side (:data (get (:nodes path) node-index))) "right"))
          (if (and (:is-branch-ready (:data (get (:nodes path) node-index)))
                   (> (:age (:data (get (:nodes path) node-index)))
                      (:delay-growth-by (:data (get (:nodes path) node-index)))))
            (let [node (get (:nodes path) node-index)
                  connected-nodes (grow/getConnectedNodes (:nodes path) node-index (:is-closed path))
                  new-left-x (- (get (:pos (get (:nodes path) node-index)) 0) 2)
                  new-left-y (+ (get (:pos (get (:nodes path) node-index)) 1) 0)
                  new-right-x (+ (get (:pos (get (:nodes path) node-index)) 0) 2)
                  new-right-y (+ (get (:pos (get (:nodes path) node-index)) 1) 0)
                  left-node (if (and (:is-bottom (:data (:prev connected-nodes)))
                                     (:is-fixed (:data (:prev connected-nodes))))
                              (buildNode new-left-x new-left-y default-tree-node-settings
                                         false false false false false false "left" true (+ (:distance-from-top (:data node)) 1) 0)
                              (buildNode new-left-x new-left-y default-tree-node-settings
                                         false false false false false false "left" false (+ (:distance-from-top (:data node)) 1) 0))
                  left-node (update-in left-node [:settings] assoc :branch-angle 15
                                       :branch-height-minimum (* 2 (:branch-height-minimum (:settings left-node))))
                  left-node (assoc left-node :parent-node-id (:id node))
                  right-node (if (and (:is-bottom (:data (:next connected-nodes)))
                                      (:is-fixed (:data (:next connected-nodes))))
                               (buildNode new-right-x new-right-y default-tree-node-settings
                                          false false false false false false "right" true (+ (:distance-from-top (:data node)) 1) 0)
                               (buildNode new-right-x new-right-y default-tree-node-settings
                                          false false false false false false "right" false (+ (:distance-from-top (:data node)) 1) 0))
                  right-node (update-in right-node [:settings] assoc :branch-angle -15
                                        :branch-height-minimum (* 2 (:branch-height-minimum (:settings right-node))))
                  right-node (assoc right-node :parent-node-id (:id node))
                  updated-node (update-in node [:data] assoc
                                          :is-branch-ready false
                                          :branch-count (inc (:branch-count (:data node))))
                  updated-node (if (< (:distance-from-top (:data updated-node)) 2)
                                 (update-in updated-node [:data] assoc :is-tip true)
                                 updated-node)]
              (when (or (= (:side (:data node)) "left") (= (:side (:data node)) "top"))
                (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) left-node)))
              (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) updated-node))
              (when (or (= (:side (:data node)) "right") (= (:side (:data node)) "top"))
                (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) right-node))))
            (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) (get (:nodes path) node-index))))
          (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) (get (:nodes path) node-index))))
        (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) (get (:nodes path) node-index)))))
    @new-path))

(defn grow
  "causes each tree branch to grow"
  [path top-rate side-rate]
  (let [new-path (atom path)]
    (swap! new-path update-in [] assoc :age (+ (:age @new-path) 1))
    (swap! new-path assoc-in [:nodes] [])
    (doseq [node-index (range (count (:nodes path)))]
      (if (not (:is-fixed (:data (get (:nodes path) node-index))))                                  ;; when node is not fixed
        (do
          (when (= (:side (:data (get (:nodes path) node-index))) "top")                            ;; when node is top of tree 
            (if (and (not (:is-branch-ready (:data (get (:nodes path) node-index))))
                     (> (:age (:data (get (:nodes path) node-index)))
                        (:delay-growth-by (:data (get (:nodes path) node-index)))))                     ;; when node is not ready to branch
              (let [node (get (:nodes path) node-index)
                    updated-node (update-in node [:pos] assoc 1 (- (get (:pos node) 1) top-rate))   ;; inc node vertical pos by the given rate
                    updated-node (update-in updated-node [:data] assoc :growth-iteration-count
                                            (+ (:growth-iteration-count (:data updated-node)) 1)
                                            :age (+ (:age (:data updated-node)) 1))                 ;; inc growth iteration count
                    updated-node (if (> (:growth-iteration-count (:data updated-node))
                                        (:branch-height-minimum (:settings updated-node)))          ;; if GIC is > BHM then 
                                   (update-in updated-node [:data] assoc
                                              :growth-iteration-count 0                             ;; set GIC to zero
                                              :is-branch-ready true)                                ;; set node ready to branch
                                   updated-node)]
                (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) updated-node)))
              (let [node (get (:nodes path) node-index)
                    updated-node (update-in node [:data] assoc :age (+ (:age (:data node)) 1))]
                (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) updated-node)))))        ;; add new node to new-path's set of nodes
          (when (= (:side (:data (get (:nodes path) node-index))) "left")                           ;; when node is on left side of tree
            (if (:is-bottom (:data (get (:nodes path) node-index)))                                 ;; if node is the bottom of the tree
              (let [node (get (:nodes path) node-index)
                    updated-node (update-in node [:data] assoc :growth-iteration-count
                                            (+ (:growth-iteration-count (:data node)) 1)
                                            :age (+ (:age (:data node)) 1))                         ;; inc age iteration count
                    updated-node (let [updated-node (update-in updated-node [:pos] assoc
                                                               0 (- (get (:pos updated-node) 0) (/ side-rate 10)))
                                       updated-node (update-in updated-node [:data] assoc
                                                               :growth-iteration-count 0)]      ;; set GIC to zero
                                   updated-node)]                                                 ;; inc node left pos by the given rate
                (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) updated-node)))        ;; add new node to new-path's set of nodes
              (if (not (:is-branch-ready (:data (get (:nodes path) node-index))))                 ;; when node is not ready to branch and not the bottom of the tree
                (let [node (get (:nodes path) node-index)
                      updated-node (if (> (:age (:data node)) (:branch-height-minimum (:settings node)))
                                     (if (< (:distance-from-top (:data node))
                                            (:max-distance (:settings node)))
                                       (update-in node [:pos] assoc
                                                  0 (- (get (:pos node) 0) (/ side-rate (+ (rand-int 10) 4)))
                                                  1 (- (get (:pos node) 1) (/ side-rate (+ (rand-int 5) 2))))
                                       (update-in node [:pos] assoc
                                                  0 (- (get (:pos node) 0) (/ side-rate (+ (rand-int 8) 5)))
                                                  1 (- (get (:pos node) 1) (/ side-rate (+ (rand-int 5) 2)))))
                                     node)
                      updated-node (update-in updated-node [:data] assoc :growth-iteration-count
                                              (+ (:growth-iteration-count (:data updated-node)) 1)  ;; inc growth iteration count
                                              :age (+ (:age (:data updated-node)) 1))               ;; inc age count

                      updated-node (if (and
                                        (> (:growth-iteration-count (:data updated-node))
                                           (:branch-height-minimum (:settings updated-node)))
                                        (< (:branch-count (:data node))
                                           (:max-branching (:settings node))))
                                     (update-in updated-node [:data] assoc
                                                :growth-iteration-count 0                           ;; set GIC to zero
                                                :is-branch-ready true)                              ;; set node ready to branch
                                     updated-node)
                      updated-node (if (> (:distance-from-top (:data updated-node))
                                          (:max-distance (:settings updated-node)))
                                     (update-in updated-node [:data] assoc
                                                :is-branch-ready false
                                                ;; :is-bottom true
                                                )
                                     updated-node)]
                  (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) updated-node)))
                (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) (get (:nodes path) node-index))))))
          (when (= (:side (:data (get (:nodes path) node-index))) "right")                          ;; when node is on right side of tree
            (if (:is-bottom (:data (get (:nodes path) node-index)))                                 ;; if node is the bottom of the tree
              (let [node (get (:nodes path) node-index)
                    updated-node (update-in node [:data] assoc :growth-iteration-count
                                            (+ (:growth-iteration-count (:data node)) 1)          ;; inc growth iteration count
                                            :age (+ (:age (:data node)) 1))                       ;; inc age iteration count
                    updated-node (let [updated-node (update-in updated-node [:pos] assoc
                                                               0 (+ (get (:pos updated-node) 0) (/ side-rate 10)))
                                       updated-node (update-in updated-node [:data] assoc
                                                               :growth-iteration-count 0)]        ;; set GIC to zero
                                   updated-node)]                                                   ;; inc node left pos by the given rate
                (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) updated-node)))          ;; add new node to new-path's set of nodes
              (if (not (:is-branch-ready (:data (get (:nodes path) node-index))))                 ;; when node is not ready to branch and not the bottom of the tree
                (let [node (get (:nodes path) node-index)
                      updated-node (if (> (:age (:data node)) (:branch-height-minimum (:settings node)))
                                     (if (< (:distance-from-top (:data node))
                                            (:max-distance (:settings node)))
                                       (update-in node [:pos] assoc
                                                  0 (+ (get (:pos node) 0) (/ side-rate (+ (rand-int 10) 4)))
                                                  1 (- (get (:pos node) 1) (/ side-rate (+ (rand-int 5) 2))))
                                       (update-in node [:pos] assoc
                                                  0 (+ (get (:pos node) 0) (/ side-rate (+ (rand-int 8) 5)))
                                                  1 (- (get (:pos node) 1) (/ side-rate (+ (rand-int 5) 2)))))
                                     node)
                      updated-node (update-in updated-node [:data] assoc :growth-iteration-count
                                              (+ (:growth-iteration-count (:data updated-node)) 1)  ;; inc growth iteration count
                                              :age (+ (:age (:data updated-node)) 1))               ;; inc age count
                      updated-node (if (and
                                        (> (:growth-iteration-count (:data updated-node))
                                           (:branch-height-minimum (:settings updated-node)))
                                        (< (:branch-count (:data node))
                                           (:max-branching (:settings node))))
                                     (update-in updated-node [:data] assoc
                                                :growth-iteration-count 0                           ;; set GIC to zero
                                                :is-branch-ready true)                              ;; set node ready to branch
                                     updated-node)
                      updated-node (if (> (:distance-from-top (:data updated-node))
                                          (:max-distance (:settings updated-node)))
                                     (update-in updated-node [:data] assoc
                                                :is-branch-ready false
                                                ;; :is-bottom true
                                                )
                                     updated-node)]
                  (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) updated-node)))
                (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) (get (:nodes path) node-index)))))))
        (swap! new-path assoc-in [:nodes] (conj (:nodes @new-path) (get (:nodes path) node-index)))))
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
