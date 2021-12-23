(ns sketch.divider
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])

  (:import [processing.core PShape PGraphics]))

;; window height x width -- 900 x 900 for drawing
(def window-height 900) ;
(def window-width 900)

(def triangle-map (atom {:triangle-count 0 :triangles ()}))

(defrecord Triangle [number x1 y1 x2 y2 x3 y3])

(defn getMiddle
  "find the middle of two given coordinates"
  [coord1 coord2]
  (+ (/ (- coord2 coord1) 2) coord1))

;; ----------- Square division functions ------------

(defn drawVerticalLines
  "recursively draws vertical lines
   seperated by a given distance."
  [distance x1 x2]
  (let [middle (getMiddle x1 x2)]

    (if (>= (- x2 x1) distance)
      (do
        (line middle 0 middle window-height)
        (drawVerticalLines distance middle x2)
        (drawVerticalLines distance x1 middle))
      ())))

(defn drawHorizontalLines
  "recursively draws horizontal lines 
   seperated by a given distance."
  [distance y1 y2]
  (let [middle (getMiddle y1 y2)]

    (if (>= (- y2 y1) distance)
      (do
        (line 0 middle window-width middle)
        (drawHorizontalLines distance middle y2)
        (drawHorizontalLines distance y1 middle))
      ())))

(defn drawGrid
  "Recursively draws a grid of a given size"
  [distance]
  (drawVerticalLines distance 0 window-width)
  (drawHorizontalLines distance 0 window-height))

;; ----------- triangle division functions ------------

(defn dividePlaneIntoTriangles
  "Divides the plane into two triangles"
  [x1 y1 x2 y2]
  (line x1 y1 x2 y2))

(defn determineLongestTriangleSide
  "Determines the longest side of a given triangle using"
  [x1 y1 x2 y2 x3 y3]
  (let
   [a (dist x1 y1 x2 y2)
    b (dist x2 y2 x3 y3)
    c (dist x3 y3 x1 y1)]

    (if (> a b)
      (if (> a c)
        (vector x1 y1 x2 y2 x3 y3)
        (vector x3 y3 x1 y1 x2 y2))
      (if (> b c)
        (vector x2 y2 x3 y3 x1 y1)
        (vector x3 y3 x1 y1 x2 y2)))))

(defn calculateSlope
  "calculates the slope give two points"
  [[x1 y1 x2 y2]]
  (/ (- y2 y1) (- x2 x1)))

(defn calculateMedian
  "find the half-way point of a given distance"
  [[x1 y1 x2 y2 x3 y3]]
  (let [random-int (+ (random 20) 30)
        new-y (+ (round (* (- y2 y1) (/ random-int 100))) y1)
        new-x (+ (round (* (- x2 x1) (/ random-int 100))) x1)]
    (vector new-x new-y)))

(defn getOppositeCorner
  "finds the opposing corner of a triangle given two other points"
  [[x1 y1 x2 y2 x3 y3]]
  (vector x3 y3))

(defn addTriangle
  "adds a new triangle to traingle-map"
  [new-triangle]
  (swap! triangle-map update-in [:triangle-count] inc)
  (swap! triangle-map assoc-in [:triangles] (conj (@triangle-map :triangles) new-triangle)))

(defn divideTriangles
  "Divides a given triangle into two new triangles, split
   along the longest edge"
  [tri-map iteration x1 y1 x2 y2 x3 y3]
  (let [longest-side (determineLongestTriangleSide x1 y1 x2 y2 x3 y3)
        median (calculateMedian longest-side)
        opposite-corner (getOppositeCorner longest-side)
        depth (dec iteration)
        rand1 (random 1 100)
        rand2 (random 1 100)]

    (if (>= depth 0)
      (do
        (if (< rand1 93)
          (do
            (divideTriangles tri-map depth
                             (get median 0)          (get median 1)
                             (get longest-side 0)    (get longest-side 1)
                             (get opposite-corner 0) (get opposite-corner 1))
            (line (get median 0) (get median 1) (get opposite-corner 0) (get opposite-corner 1)))
          (addTriangle (Triangle. (@triangle-map :triangle-count) x1 y1 x2 y2 x3 y3)))
        (if (< rand2 93)
          (do
            (divideTriangles tri-map depth
                             (get median 0)          (get median 1)
                             (get longest-side 2)    (get longest-side 3)
                             (get opposite-corner 0) (get opposite-corner 1))
            (line (get median 0) (get median 1) (get opposite-corner 0) (get opposite-corner 1)))
          (addTriangle (Triangle. (@triangle-map :triangle-count) x1 y1 x2 y2 x3 y3))))
      (addTriangle (Triangle. (@triangle-map :triangle-count) x1 y1 x2 y2 x3 y3)))))

(defn buildTriangles
  "Recursively builds triangles to a given iteration"
  [iteration]
  (dividePlaneIntoTriangles 0 0 window-width window-height)
  (divideTriangles {} iteration, 0 0, window-width 0, window-width window-height)
  (divideTriangles {} iteration, 0 0, window-width window-height, 0 window-height))
