(ns sketch.divider
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]]
            [sketch.calculations :as calc]
            [sketch.dynamic :as dyna])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])

  (:import [processing.core PShape PGraphics]))

(def triangle-map (atom {:triangle-count 0 :triangles []}))
(defrecord Triangle [number iteration x1 y1 x2 y2 x3 y3 pix])

(def square-map (atom {:square-count 0 :squares []}))
(defrecord Square [number iteration x1 y1 x2 y2 pix])

;; ----------- Square division functions ------------
(defn drawVerticalLine
  "draws a single vertical line of a given length at a given point"
  [x length]
  (let [start (:start length)
        end (:end length)]
    (line x start x end)))

(defn drawVerticalLines
  "recursively draws vertical lines
   seperated by a given distance."
  [distance x1 x2]
  (let [middle (calc/calculateMiddle x1 x2)]
    (if (>= (- x2 x1) distance)
      (do
        (line middle 0 middle dyna/window-height)
        (drawVerticalLines distance middle x2)
        (drawVerticalLines distance x1 middle))
      ())))

(def xCollection (atom []))

(defn divideCanvasVertical
  "recursively draws vertical lines
   seperated by a given distance."
  [distance x1 x2]
  (if (>= (- x2 x1) distance)
   (let [middle (calc/calculateMiddle x1 x2)]
     (divideCanvasVertical distance x1 middle)
     (divideCanvasVertical distance middle x2)))
  (swap! xCollection conj x1 x2))

(defn divideCanvasVertically
  "divides the canvas by the closest distinct divisor of a 
   given number and returns a collection of x division points"
  [divisor]
  (let [allDivisors (calc/calculateDivisors dyna/window-width)
        div (if (contains? allDivisors divisor)
              divisor
              (last (filter (fn [x] (< x divisor)) allDivisors)))]
    (take-nth div (range dyna/window-width))))

(defn divideVert
  [distance x1 y1 x2 y2]
  (let [xIntervals (take-nth distance (range (- x2 x1)))]
    ;; (doseq [x xIntervals]
    ;;   (drawVerticalLine x {:start y1 :end y2}))
    xIntervals))

(defn drawHorizontalLine
  "draws a single horizontal line of a given length at a given point"
  [y length]
  (let [start (:start length)
        end (:end length)]
    (line start y end y)))

(defn divideHorz
  [distance x1 y1 x2 y2]
  (let [yIntervals (take-nth distance (range (- y2 y1)))]
    ;; (doseq [y yIntervals]
    ;;   (drawHorizontalLine y {:start x1 :end x2}))
    yIntervals))

(defn divideGrid
   "divides the canvas by a given distance and returns all intersecting points"
  [distance x1 y1 x2 y2]
  (let [xIntervals (into [] (divideVert distance x1 y1 x2 y2))
        yIntervals (into [] (divideHorz distance x1 y1 x2 y2))]
    (mapv
     (fn [[x y]] (conj {:x x :y y}))
     (for [[x y] (map vector xIntervals yIntervals)]
       [x y]))))

(defn drawHorizontalLines
  "recursively draws horizontal lines 
   seperated by a given distance."
  [distance y1 y2]
  (let [middle (calc/calculateMiddle y1 y2)]

    (if (>= (- y2 y1) distance)
      (do
        (line 0 middle dyna/window-width middle)
        (drawHorizontalLines distance middle y2)
        (drawHorizontalLines distance y1 middle))
      ())))

(defn drawGrid
  "Recursively draws a grid of a given size"
  [distance]
  (drawVerticalLines distance 0 dyna/window-width)
  (drawHorizontalLines distance 0 dyna/window-height))

(defn addSquare
  "adds a new square to square-map"
  [new-square]
  (swap! square-map update-in [:square-count] inc)
  (swap! square-map assoc-in [:squares] (conj (@square-map :squares) new-square)))

(defn getSquarePixels
  "retrieves all of the pixels contained within a given triangle"
  [x1 y1 x2 y2]
  (let [[xmin ymin xmax ymax] [x1 y1 x2 y2]]
    (filter identity
            (doall
             (map
              (fn [[x y]]
                (conj {:x x :y y}))
              (for [y (range ymin ymax)
                    x (range xmin xmax)]
                [x y]))))))

(defn divideGoldenRectangles
  "recursively divides plane by into random rectangles based on the golden ratio"
  [x y width height depth desiredDepth]
  (let [x1 x
        y1 y
        x2 width
        y2 height
        rand1 (random 100)
        rand2 (random 100)
        goldenWidth (+ (calc/calculateGoldenRatio (- width x1)) x1)
        goldenHeight (+ (calc/calculateGoldenRatio (- height y1)) y1)]
    (if (< depth desiredDepth)
      (if (even? depth)
        (do
          (if (> rand1 (/ (* depth depth) 2))
            (divideGoldenRectangles goldenWidth y1 x2 y2 (inc depth) desiredDepth)
            (addSquare
             (Square.
              (@square-map :square-count) depth x1 y1 x2 y2
              (getSquarePixels x1 y1 x2 y2))))
          (if (> rand2 (/ (* depth depth) 2))
            (divideGoldenRectangles x1 y1 goldenWidth y2 (inc depth) desiredDepth)
            (addSquare
             (Square.
              (@square-map :square-count) depth x1 y1 x2 y2
              (getSquarePixels x1 y1 x2 y2))))
          (drawVerticalLine goldenWidth {:start y1 :end y2}))
        (do
          (if (> rand1 (/ (* depth depth) 2))
            (divideGoldenRectangles x1 goldenHeight x2 y2 (inc depth) desiredDepth)
            (addSquare
             (Square.
              (@square-map :square-count) depth x1 y1 x2 y2
              (getSquarePixels x1 y1 x2 y2))))
          (if (> rand2 (/ (* depth depth) 2))
            (divideGoldenRectangles x1 y1 x2 goldenHeight (inc depth) desiredDepth)
            (addSquare
             (Square.
              (@square-map :square-count) depth x1 y1 x2 y2
              (getSquarePixels x1 y1 x2 y2))))
          (drawHorizontalLine goldenHeight {:start x1 :end x2})))
      (do
        (if (even? depth)
          (drawHorizontalLine goldenHeight {:start x1 :end x2})
          (drawVerticalLine goldenWidth {:start y1 :end y2}))
        (addSquare
         (Square.
          (@square-map :square-count) depth x1 y1 x2 y2
          (getSquarePixels x1 y1 x2 y2)))))))

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

(defn getOppositeCorner
  "finds the opposing corner of a triangle given two other points"
  [[x1 y1 x2 y2 x3 y3]]
  (vector x3 y3))

(defn cross [v1 v2]
  (let [[a1 a2 a3] v1
        [b1 b2 b3] v2]
    [(- (* a2 b3) (* a3 b2))
     (- (* a3 b1) (* a1 b3))
     (- (* a1 b2) (* a2 b1))]))

(defn bbox [vertices width height]
  (let [xsorted (sort-by first vertices)
        ysorted (sort-by second vertices)
        [xmin] (first xsorted)
        [xmax] (last xsorted)
        [_ ymin] (first ysorted)
        [_ ymax] (last ysorted)]
    [(max 0 xmin)
     (max 0 ymin)
     (min width xmax)
     (min height ymax)]))

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

(defn visible? [bc]
  (every? #(not (neg? %)) bc))

(defn getTrianglePixels
  "retrieves all of the pixels contained within a given triangle"
  [vertices]
  (let [[xmin ymin xmax ymax] (bbox vertices dyna/window-width dyna/window-height)]
    (filter identity
            (doall
             (map
              (fn [[x y :as p]]
                (let [bc (barycentric vertices p)]
                  (if (visible? bc)
                    (conj {:x x :y y}))))
              (for [y (range ymin ymax)
                    x (range xmin xmax)]
                [x y]))))))

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
        median (calc/calculateMedian longest-side)
        opposite-corner (getOppositeCorner longest-side)
        depth (dec iteration)
        rand1 (random 1 100)
        rand2 (random 1 100)]

    (if (>= depth 0)
      (do
        (if (or (< rand1 99) (> depth 5))
          (do
            (divideTriangles tri-map depth
                             (get median 0)          (get median 1)
                             (get longest-side 0)    (get longest-side 1)
                             (get opposite-corner 0) (get opposite-corner 1))
            ;; (line (get median 0) (get median 1) (get opposite-corner 0) (get opposite-corner 1))
            )
          (do
            (addTriangle
             (Triangle.
              (@triangle-map :triangle-count) depth x1 y1 x2 y2 x3 y3
              (getTrianglePixels [[x1 y1] [x2 y2] [x3 y3]])))))
        (if (or (< rand2 99) (> depth 5))
          (do
            (divideTriangles tri-map depth
                             (get median 0)          (get median 1)
                             (get longest-side 2)    (get longest-side 3)
                             (get opposite-corner 0) (get opposite-corner 1))
            ;; (line (get median 0) (get median 1) (get opposite-corner 0) (get opposite-corner 1))
            )
          (do
            (addTriangle
             (Triangle.
              (@triangle-map :triangle-count) depth x1 y1 x2 y2 x3 y3
              (getTrianglePixels [[x1 y1] [x2 y2] [x3 y3]]))))))
      (do
        (addTriangle
         (Triangle.
          (@triangle-map :triangle-count) depth x1 y1 x2 y2 x3 y3
          (getTrianglePixels [[x1 y1] [x2 y2] [x3 y3]])))))))

(defn buildTriangles
  "Recursively builds triangles to a given iteration"
  [iteration]
  ;; (dividePlaneIntoTriangles 0 0 window-width window-height)
  (divideTriangles {} iteration, 0 0, dyna/window-width 0, dyna/window-width dyna/window-height)
  (divideTriangles {} iteration, 0 0, dyna/window-width dyna/window-height, 0 dyna/window-height))
