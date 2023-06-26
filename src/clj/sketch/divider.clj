(ns sketch.divider
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
        (line middle 0 middle (height))
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
  (let [allDivisors (calc/calculateDivisors width)
        div (if (contains? allDivisors divisor)
              divisor
              (last (filter (fn [x] (< x divisor)) allDivisors)))]
    (take-nth div (range width))))

(defn divideVert
  [distance x1 y1 x2 y2]
  (let [xIntervals (take-nth distance (range (- x2 x1)))]
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
    yIntervals))

(defn divideGrid
  "divides the canvas by a given distance and returns all intersecting points"
  [distance x1 y1 x2 y2]
  (let [xIntervals (into [] (divideVert distance x1 y1 x2 y2))
        yIntervals (into [] (divideHorz distance x1 y1 x2 y2))]
    (mapv
     (fn [[x y]] (conj {:x x :y y}))
     (for [x xIntervals
           y yIntervals]
       [x y]))))

(defn drawHorizontalLines
  "recursively draws horizontal lines 
   seperated by a given distance."
  [distance y1 y2]
  (let [middle (calc/calculateMiddle y1 y2)]

    (if (>= (- y2 y1) distance)
      (do
        (line 0 middle width middle)
        (drawHorizontalLines distance middle y2)
        (drawHorizontalLines distance y1 middle))
      ())))

(defn drawGrid
  "Recursively draws a grid of a given size"
  [distance]
  (drawVerticalLines distance 0 (width))
  (drawHorizontalLines distance 0 (height)))

(defn addSquare
  "adds a new square to square-map"
  [new-square]
  (swap! square-map update-in [:square-count] inc)
  (swap! square-map assoc-in [:squares] (conj (@square-map :squares) new-square)))

(defn getSquarePixels
  "retrieves all of the pixels contained within a given square"
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
        rand1 (rand-int 1000)
        rand2 (rand-int 1618)
        goldenWidth (+ (calc/calculateGoldenRatio (- x2 x1)) x1)
        goldenHeight (+ (calc/calculateGoldenRatio (- y2 y1)) y1)]
    (if (< depth desiredDepth)
      (if (even? rand1)
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
              (getSquarePixels x1 y1 x2 y2)))))
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
              (getSquarePixels x1 y1 x2 y2))))))
      (addSquare
       (Square.
        (@square-map :square-count) depth x1 y1 x2 y2
        (getSquarePixels x1 y1 x2 y2))))))



