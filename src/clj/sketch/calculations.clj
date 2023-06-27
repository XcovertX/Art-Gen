(ns sketch.calculations
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])
  (:import [processing.core PShape PGraphics]))

(def TAU (* 2 (Math/PI)))
(def distance dist)
(def rand random)

(defn calculateDivisors
  "finds all distinct divisors of a given number"
  [num]
  (into []
   (for [n (range num)
        :let [k n]
        :when (= (mod num (if (= n 0) 1 n)) 0)]
    k)))

(defn calculateGoldenRatio
  "finds the golden ratio integer of a given length"
  [length]
  (round (* length 0.618)))

(defn calculateMedian
  "find the half-way point of a given distance"
  [coords]
  (let [random-int (+ (random 20) 30)
        x1 (:x (first coords))
        y1 (:y (first coords))
        x2 (:x (second coords))
        y2 (:y (second coords))
        new-y (+ (round (* (- y2 y1) (/ random-int 100))) y1)
        new-x (+ (round (* (- x2 x1) (/ random-int 100))) x1)] 
    {:x new-x :y new-y}))

(defn calculateMedianTri
  "find the half-way point of a given distance"
  [coords]
  (let [[x1 y1 x2 y2] coords
        new-y (+ (/ (- y2 y1) 2) y1)
        new-x (+ (/ (- x2 x1) 2) x1)]
    [new-x new-y]))

(defn calculateMiddle
  "find the middle of two given coordinates"
  [coord1 coord2]
  (+ (/ (- coord2 coord1) 2) coord1))

(defn calculateCenter
  "finds the center of a given square"
  [w h]
  (let [wCenter (calculateMiddle (:x w) (:x h))
        hCenter (calculateMiddle (:y w) (:y h))]
    {:x wCenter :y hCenter}))

(defn calculateRandomBoolean
  "returns a random boolean"
  []
  (if (> (random 100) 50)
    true
    false))

(defn calculateRandomInt
  "returns a random int between two given ints"
  ([max]
   (random max))
  ([min max]
   (random min max)))

(defn calculateSlope
  "calculates the slope give two points"
  [[x1 y1 x2 y2]]
  (/ (- y2 y1) (- x2 x1)))

(defn calculateTriangleCenter
  "finds the center of a given triangle"
  [triangle]
  (let [x1 (:x (:position (:node-a triangle)))
        y1 (:y (:position (:node-a triangle)))
        x2 (:x (:position (:node-b triangle)))
        y2 (:y (:position (:node-b triangle)))
        x3 (:x (:position (:node-c triangle)))
        y3 (:y (:position (:node-c triangle)))
        a (calculateMedianTri [x1 y1 x2 y2])
        b (calculateMedianTri [x2 y2 x3 y3])
        c (calculateMedianTri [x3 y3 x1 y1])
        div1 (calculateMedianTri [x1 y1 (get a 0) (get a 1)])
        div2 (calculateMedianTri [x2 y2 (get b 0) (get b 1)])
        div3 (calculateMedianTri [x3 y3 (get c 0) (get c 1)])
        result (mapv
                (fn [x] (conj (round (/ x 3))))
                (for [x (mapv + div1 div2 div3)] x))]
{:x (get result 0) :y (get result 1)}))

(defn calculateDistance
  "finds the distance between two given points"
  [point-a point-b]
  (let [x1 (:x point-a) 
        y1 (:y point-a)
        x2 (:x point-b)
        y2 (:y point-b)
        x (- (+ (- (* x2 x2) (* x2 x1)) (* x1 x1)) (* x2 x1))
        y (- (+ (- (* y2 y2) (* y2 y1)) (* y1 y1)) (* y2 y1))]
    (sqrt (+ x y))))

(defn calculateDistanceFromCenter
  "finds the distance from the center of the canvas to a given point"
  [coord]
  (let [center (calculateCenter {:x 0 :y 0} {:x (width) :y (height)})]
    (calculateDistance center coord)))

(defn retrieveLinePixels
  "recurisively retrieves the pixels of a given line"
  [x1 y1 x2 y2 dx dy p pixel-list]
  (if (< x1 x2)
    (if (>= p 0)
      (let [new-pixel-list (conj pixel-list {:x x1 :y x2})
            y1 (+ y1 1) p (+ p (- (* 2 dy) (* 2 dx)))]
        (retrieveLinePixels x1 y1 x2 y2 dx dy p new-pixel-list))
      (let [new-pixel-list (conj pixel-list {:x x1 :y x2})
            x1 (+ x1 1) p (+ p (* 2 dy))]
        (retrieveLinePixels x1 y1 x2 y2 dx dy p new-pixel-list)))
    pixel-list))

(defn calculateLine
  "retrieves all pixels of a line between two points"
  [start end]
  (let [[x1 y1] start [x2 y2] end
        dx (- x2 x1) dy (- y2 y1)
        p (- (* 2 dy) dx)]
    (println x1 y1 x2 y2 dx dy p)
    (retrieveLinePixels x1 y1 x2 y2 dx dy p [])))

(defn euclidean-distance
  [vec1 vec2]
  (Math/sqrt
   (reduce + (map #(Math/pow (- %1 %2) 2) vec1 vec2))))

(defn visible? [bc]
  (every? #(not (neg? %)) bc))

(defn cross [v1 v2]
  (let [[a1 a2 a3] v1
        [b1 b2 b3] v2]
    [(- (* a2 b3) (* a3 b2))
     (- (* a3 b1) (* a1 b3))
     (- (* a1 b2) (* a2 b1))]))

(defn bbox [vertices]
  (let [xsorted (sort-by first vertices)
        ysorted (sort-by second vertices)
        [xmin] (first xsorted)
        [xmax] (last xsorted)
        [_ ymin] (first ysorted)
        [_ ymax] (last ysorted)]
    [(max 0 xmin)
     (max 0 ymin)
     (min (width) xmax)
     (min (height) ymax)]))

(defn barycentric [vertices p]
  (let [[[x1 y1] [x2 y2] [x3 y3]] vertices
        [px py] p
        u1 [(- x3 x1) (- x2 x1) (- x1 px)]
        u2 [(- y3 y1) (- y2 y1) (- y1 py)]
        [ux uy uz] (cross u1 u2)]
    (if (zero? (Math/abs uz))
      [-1 1 1]
      (let [u (- 1.0 (/ (+ ux uy) uz))
            v (float (/ uy uz))
            w (float (/ ux uz))]
        [u v w]))))