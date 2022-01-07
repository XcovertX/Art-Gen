(ns sketch.hitomezashi
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]]
            [sketch.divider :as div]
            [sketch.color :as colo]
            [sketch.dynamic :as dyn])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])
  (:import [processing.core PShape PGraphics]))

(defn draw-stitch
  "draws alternating stitch"
  [first coords]
  (doseq [coord coords]
    (let [x1 (:x1 coord)
          y1 (:y1 coord)
          x2 (:x2 coord)
          y2 (:y2 coord)]
      (line x1 y1 x2 y2)))
  )

(defn buildXStitchSegments
  "constructs a collection of coordinates
   that consist of an entire stitch length
   for each xIntersection"
  [intersections xCount]
  
   (filterv not-empty
            (loop [i 0
                   result []]
              (if (> i xCount)
                result
                (recur
                 (inc i)
                 (conj result
                       (filterv (fn [x] (not= (:x x) nil))
                                (mapv
                                 (fn [[x y]] (conj {:x x :y y}))
                                 (for [coord intersections]
                                   (if (= (:x coord) i)
                                     [(:x coord) (:y coord)]))))))))))

(defn buildYStitchSegments
  "constructs a collection of coordinates
   that consist of an entire stitch length
   for each xIntersection"
  [intersections])

(defn hito-stitch
  [xAxisNums yAxisNums stitchSize square]
  (let [[x1 y1 x2 y2] square
        xCount (+ (int (/ (- (- x2 x1) 1) stitchSize)) 1)
        yCount (+ (int (/ (- (- y2 y1) 1) stitchSize)) 1)
        intersections (div/divideGrid stitchSize x1 y1 x2 y2)
        xSegments (buildXStitchSegments intersections xCount)
        ySegments (buildYStitchSegments intersections)]
    (println xCount)
    (println yCount)
    ;; (println xSegments)
    (doseq [i (range 20)]
      (println (get xSegments i)))
    ;; (for [coord coords])
    ))