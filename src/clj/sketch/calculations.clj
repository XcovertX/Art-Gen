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
  [[x1 y1 x2 y2 x3 y3]]
  (let [random-int (+ (random 20) 30)
        new-y (+ (round (* (- y2 y1) (/ random-int 100))) y1)
        new-x (+ (round (* (- x2 x1) (/ random-int 100))) x1)]
    (vector new-x new-y)))

(defn calculateMiddle
  "find the middle of two given coordinates"
  [coord1 coord2]
  (+ (/ (- coord2 coord1) 2) coord1))

(defn calculateSlope
  "calculates the slope give two points"
  [[x1 y1 x2 y2]]
  (/ (- y2 y1) (- x2 x1)))