(ns sketch.dynamic
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

(defn h
  ([] (h 1.0))
  ([value] (* (height) value)))

(defn w
  ([] (w 1.0))
  ([value] (* (width) value)))

;; draw vertical lines
(defn drawVerticalLines
  "this recursively draws vertical lines
   seperated by a given distance."
  [distance x1 x2]
  (let [middle (+ (/ (- x2 x1) 2) x1)]

    (if (>= (- x2 x1) distance)
      (do
        (line middle 0 middle window-height)
        (drawVerticalLines distance middle x2)
        (drawVerticalLines distance x1 middle))
      ())))

;; draw horizontal lines
(defn drawHorizontalLines
  "this recursively draws horizontal lines
   seperated by a given distance."
  [distance y1 y2]
  (let [middle (+ (/ (- y2 y1) 2) y1)]

    (if (>= (- y2 y1) distance)
      (do
        (println (- y2 y1) middle)
        (line 0 middle window-width middle)
        (drawHorizontalLines distance middle y2)
        (drawHorizontalLines distance y1 middle))
      ())))

(defn drawGrid
  "Recursively draws a grid of a given size"
  [distance]
  (drawVerticalLines distance 0 window-width)
  (drawHorizontalLines distance 0 window-height))

(defn dividePlaneIntoTriangles
  "Divides the plane into two triangles"
  [x1 y1 x2 y2]
  (line x1 y1 x2 y2)
  )

(defn determineLongestTriangleSide
  "Determines the longest side of a given triangle using "
  [x1 y1 x2 y2 x3 y3]
  (let
   [a (dist x1 y1 x2 y2)
    b (dist x2 y2 x3 y3)
    c (dist x3 y3 x1 y1)]

    (if (> a b)
      (if (> a c)
        (vector x1 y1 x2 y2)
        (vector x3 y3 x1 y1))
      (if (> b c)
        (vector x2 y2 x3 y3)
        (vector x3 y3 x1 y1)))))

(defn calculateSlope
  "calculates the slope give two points"
  [[x1 y1 x2 y2]]
  (/ (- y2 y1) (- x2 x1)))

(defn calculateMedian
  "find the half-way point of a given distance"
  [[x1 y1 x2 y2]]
  (let [new-y (+ (* (- y2 y1) (/ 11 20)) y1)
        new-x (+ (* (- x2 x1) (/ 11 20)) x1)]
    (vector new-x new-y)))

(defn divideTriangles
  "Divides a given triangle into two new triangles, split
   along the longest edge"
  [iteration x1 y1 x2 y2 x3 y3]
  (let [longest-side (determineLongestTriangleSide x1 y1 x2 y2 x3 y3)
        median (calculateMedian [x2 y2 x3 y3])
        depth (dec iteration)]
    (println median)
    (stroke (random 180 360) 90 90 1)
    (line x1 y1 (get median 0) (get median 1))
    (if (>= depth 0)
      (do
        (divideTriangles depth, (get median 0) (get median 1), x3 y3, x1 y1)
        (divideTriangles depth, (get median 0) (get median 1), x1 y1, x2 y2))
      (println "bottom"))))

(defn buildTriangles
  "Recursively builds triangles to a given iteration"
  [iteration]
  (dividePlaneIntoTriangles 0 0 window-width window-height)
  (divideTriangles iteration, 0 window-height, 0 0, window-width window-height)
  (divideTriangles iteration, window-width 0, window-width window-height, 0 0))


(defn draw []
  (no-loop)
  (color-mode :hsb 360 100 100 1.0)
  (stroke 40 90 90 1)
  (doseq [img-num (range 1)]
    (background 220 49 66)
    ;; do drawing here

    ;; (drawGrid 50)
    (buildTriangles 14)

    (save (str "sketch-" img-num ".tif"))
    (let [filename (str "sketch-" img-num ".tif")
          thumbnail (str "sketch-" img-num "-1000.tif")]
      (save filename)
      (sh "convert" "-LZW" filename filename)
      (sh "convert" "-scale" "1000x1000" filename thumbnail)
      (println "Done with image" img-num))))

(defn setup []
  
  )
