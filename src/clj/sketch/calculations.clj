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
  (let [wCenter (calculateMiddle (:x w) (:y w))
        hCenter (calculateMiddle (:x h) (:y h))]
    {:x wCenter :y hCenter}))

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
  (let [[x1 y1] [(:x1 tri) (:y1 tri)]
        [x2 y2] [(:x2 tri) (:y2 tri)]
        [x3 y3] [(:x3 tri) (:y3 tri)]
        [a1 a2] (calculateMedianTri [x1 y1 x2 y2])
        [b1 b2] (calculateMedianTri [x2 y2 x3 y3])
        [c1 c2] (calculateMedianTri [x3 y3 x1 y1])
        div1 (calculateMedianTri [x1 y1 a1 a2])
        div2 (calculateMedianTri [x2 y2 c1 c2])
        div3 (calculateMedianTri [x3 y3 b1 b2])]
    (mapv
     (fn [x] (conj (round (/ x 3))))
     (for [x (mapv + div1 div2 div3)] x))))