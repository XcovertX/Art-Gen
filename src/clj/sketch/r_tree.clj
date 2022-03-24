(ns sketch.r_tree
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


(defrecord BoundingBox [x0 y0 x1 y1])
(defrecord Node [bounding-box data children])

(defn intersects?
  "intersects? returns true if the two bounding boxes overlap, false otherwise. If either box is nil false is returned."
  [a b]
  (println "intersects" "a" a "b" b)
  (not
   (or
    (nil? a)
    (nil? b)
    (< (:x1 a) (:x0 b))
    (< (:x1 b) (:x0 a))
    (< (:y1 a) (:y0 b))
    (< (:y1 b) (:y0 a)))))

(defn make-bounding-box
  "make-bounding-box constructs a new bounding box."
  [x0 y0 x1 y1]
  (BoundingBox.
   (min x0 x1)
   (min y0 y1)
   (max x0 x1)
   (max y0 y1)))

(defn compute-bounding-box
  "compute-bounding-box computes the bounding box for a sequence of nodes"
  [nodes]
  (println "compute bb:" nodes)
  (let [bb (map :bounding-box nodes)
        x0 (reduce min (map :x0 bb))
        y0 (reduce min (map :y0 bb))
        x1 (reduce max (map :x1 bb))
        y1 (reduce max (map :y1 bb))]
    (println x0 y0 x1 y1)
    (make-bounding-box x0 y0 x1 y1)))

(defn make-leaf
  "make-leaf creates a node record for the given data and bounding box with no children."
  [bbox data]
  (Node. bbox
         data
         nil))

(defn make-branch
  "make-branch creates a node record with no data and the given children. The bounding box is computed."
  [children]
  (println "hssere")
  (Node.
   (compute-bounding-box children)
   nil
   children))

(declare top-down)

(defn- split
  "split partitions a sequence of data and recursivley calls top-down on the partitions."
  [level m nodes]
  (println "here")
  (let [k      (quot (dec (+ (count nodes) m)) m)
        dim    (get [:x0 :y0] (mod level 2))]
    (->> nodes
         (sort-by #(get-in % [:bounding-box dim]))
         (partition k k nil)
         (map #(top-down (inc level) m %))
         (into [])
         make-branch)))

(defn- top-down
  "top-down implements top-down bulk-load algorithm. It returns the root node of a subtree."
  [level m nodes]
  (println (count nodes) m)
  (if (<= (count nodes) m)
    (make-branch nodes)
    (split level m nodes)))

(defn create
  "create a new rtree containing the given leaf nodes."
  ([leaves] (create {} leaves))
  ([opts leaves]
   (when (not-empty leaves)
     (let [maxChildren (get opts :max-children 25)]
       (top-down 0 maxChildren leaves)))))

(defn- -search-intersection
  [tree box]
  (println "search2")
  (if (intersects? box (:bounding-box tree))
    (cons (:data tree)
          (mapcat #(-search-intersection % box) (:children tree)))))

(defn search-intersection
  "search-intersection searches the tree for all data that intersects with the given box"
  [tree box]
  (println "search1")
  (->> (-search-intersection tree box)
       (remove nil?)))

(defn bulk-update
  "bulk-update does a cheap update of object state, without re-organising the tree. func should map from leaf nodes to leaf nodes, and may return nil to delete an item."
  [{:keys [children] :as node} func]
  (if (nil? children)
    (func node)
    (let [children' (->> (map #(bulk-update % func) children)
                         (remove nil?))]
      (when (not-empty children')
        (-> (compute-bounding-box children')
            (assoc node :children children' :bounding-box))))))

(defn rand-floats
  "rand-floats generates a sequence of floats in the range [lo, hi) of length n."
  [lo hi n]
  (->> (repeatedly #(rand (- hi lo)))
       (map #(+ lo %))
       (take n)))

(defn random-data
  "random-data generate a sequence of nodes with random data of length n."
  [n]
  (let [x0s (rand-floats 0 1 n)
        y0s (rand-floats 0 1 n)
        ws  (rand-floats 0.01 0.1 n)
        hs  (rand-floats 0.01 0.1 n)
        x1s (map + x0s ws)
        y1s (map + y0s hs)
        bbs (map make-bounding-box x0s y0s x1s y1s)
        ds  (->> (range n)
                 (map str))]
    (map make-leaf bbs ds)))

(defn regular-data
  "regular-data generates a sequence of nodes with highly regular data of length n."
  [n]
  (let [bbs (map make-bounding-box
                 (range 0 n)
                 (reverse (range 0 n))
                 (range 1 (inc n))
                 (reverse (range 1 (inc n))))
        ds  (->> (range n)
                 (map str))]
    (map make-leaf bbs ds)))

(defn test-tree
  []
  (let [nodes [{:x 1 :y 1 :z 1 :b 1 :data ["data1"]}
               {:x 20 :y 11 :z 20 :b 11 :data ["data2"]}
               {:x 3 :y 6 :z 3 :b 6 :data ["data3"]}
               {:x 22 :y 400 :z 22 :b 400 :data ["data4"]}
               {:x 9 :y 189 :z 9 :b 189 :data ["data5"]}
               {:x 2 :y 20 :z 2 :b 20 :data ["data6"]}
               {:x 3 :y 2 :z 3 :b 2 :data ["data7"]}
               {:x 99 :y 99 :z 99 :b 99 :data ["data8"]}]
        xs (map :x nodes)
        ys (map :y nodes)
        zs (map :z nodes)
        bs (map :b nodes)
        ds (map :data nodes)
        bbs (map make-bounding-box xs ys zs bs)]
    (map make-leaf bbs ds)))

(defn test-create
  []
  (let [nodes  (test-tree)
        ds     (map :data nodes)
        b (vector (Node. {:x0 2, :y0 7, :x1 2, :y1 9} ["data6"] nil))
        bbox   (compute-bounding-box b)
        tree   (create {:max-children 4} nodes)
        ds'    (search-intersection tree bbox)]
    (if (= (sort ds)
           (sort ds))
      "they are equal"
      "not all nodes were inserted")
   (println "tree" tree)))

(defn growth-create
  [nodes]
  (let [ds (map :data nodes)
        ]))


