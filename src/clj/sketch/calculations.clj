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
        [x1 y1 x2 y2] coords
        new-y (+ (round (* (- y2 y1) (/ random-int 100))) y1)
        new-x (+ (round (* (- x2 x1) (/ random-int 100))) x1)]
    (vector new-x new-y)))

(defn calculateMedianTri
  "find the half-way point of a given distance"
  [coords]
  (let [[x1 y1 x2 y2] coords
        new-y (+ (/ (- y2 y1) 2) y1)
        new-x (+ (/ (- x2 x1) 2) x1)]
    (vector new-x new-y)))

(defn calculateMiddle
  "find the middle of two given coordinates"
  [coord1 coord2]
  (+ (/ (- coord2 coord1) 2) coord1))

(defn calculateCenter
  "finds the center of a given square"
  [w h]
  (let [wCenter (calculateMiddle (:x w) (:x h))
        hCenter (calculateMiddle (:y w) (:y h))]
    [wCenter hCenter]))

(defn calculateRandomBoolean
  "returns a random boolean"
  []
  (if (> (random 100) 50)
    true
    false))

(defn calculateSlope
  "calculates the slope give two points"
  [[x1 y1 x2 y2]]
  (/ (- y2 y1) (- x2 x1)))

(defn calculateTriangleCenter
  "finds the center of a given triangle"
  [tri]
  (let [[x1 y1 x2 y2 x3 y3] tri
        [a1 a2] (calculateMedianTri [x1 y1 x2 y2])
        [b1 b2] (calculateMedianTri [x2 y2 x3 y3])
        [c1 c2] (calculateMedianTri [x3 y3 x1 y1])
        div1 (calculateMedianTri [x1 y1 a1 a2])
        div2 (calculateMedianTri [x2 y2 c1 c2])
        div3 (calculateMedianTri [x3 y3 b1 b2])]
    (mapv
     (fn [x] (conj (round (/ x 3))))
     (for [x (mapv + div1 div2 div3)] x))))

(defn calculateDistance
  "finds the distance between two given points"
  [coord]
  (let [[x1 y1 x2 y2] coord
        x (- (+ (- (* x2 x2) (* x2 x1)) (* x1 x1)) (* x2 x1))
        y (- (+ (- (* y2 y2) (* y2 y1)) (* y1 y1)) (* y2 y1))]
    (sqrt (+ x y))))

(defn calculateDistanceFromCenter
  "finds the distance from the center of the canvas to a given point"
  [coord]
  (let [[x1 y1] (calculateCenter {:x 0 :y 0} {:x (width) :y (height)})
        [x2 y2] coord]
    (calculateDistance [x1 y1 x2 y2])))

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