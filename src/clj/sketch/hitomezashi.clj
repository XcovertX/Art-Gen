(ns sketch.hitomezashi
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]]
            [sketch.divider :as div]
            [sketch.calculations :as cal])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])
  (:import [processing.core PShape PGraphics]))

(def vowels #{\a \e \i \o \u})
(def consonants #{\b \c \d \f \g \h \j \k \l \m \n \p \q \r \s \t \v \w \x \y \z})

(defn draw-stitch-segment
  "draws alternating stitch"
  [segment first]
  (loop [i 0
         alternate first]
    (if (< (+ i 1) (count segment))
      (let [from (get segment i)
            to (get segment (+ i 1))
            x1 (:x from)
            y1 (:y from)
            x2 (:x to)
            y2 (:y to)]
        (if (= alternate true)
          (line x1 y1 x2 y2))
        (recur (inc i) (not alternate))))))

(defn buildXStitchSegments
  "constructs a collection of coordinates
   that consist of an entire stitch length
   for each xIntersection"
  [intersections xCount]
  
   (filterv not-empty
            (loop [i 0
                   result []]
              (if (> i (* xCount xCount))
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
    [intersections yCount]
  
   (filterv not-empty
            (loop [i 0
                   result []]
              (if (> i (* yCount yCount))
                result
                (recur
                 (inc i)
                 (conj result
                       (filterv (fn [y] (not= (:y y) nil))
                                (mapv
                                 (fn [[x y]] (conj {:x x :y y}))
                                 (for [coord intersections]
                                   (if (= (:y coord) i)
                                     [(:x coord) (:y coord)]))))))))))
(defn boolify-it
  "converts a given input into a boolean representation"
  [input]
  (let [rand (random 100)
        axis (if (string? input)
               (if (< rand 50)
                 (filterv some? (map #(when (vowels %1) %2) input (range)))
                 (filterv some? (map #(when (consonants %1) %2) input (range)))))]
    (loop [i 0
           result []]
      (if (>= i (count input))
        result
        (recur
         (inc i)
         (conj result (if (some #(= i %) axis)
                        true
                        false)))))))

(defn hito-stitch
  [xAxisInput yAxisInput stitchSize square]
  (let [[x1 y1 x2 y2] square
        xCount (+ (int (/ (- (- x2 x1) 1) stitchSize)) 1)
        yCount (+ (int (/ (- (- y2 y1) 1) stitchSize)) 1)
        intersections (div/divideGrid stitchSize x1 y1 x2 y2)
        xSegments (buildXStitchSegments intersections xCount)
        ySegments (buildYStitchSegments intersections yCount)
        xAxisBools (boolify-it xAxisInput)
        yAxisBools (boolify-it yAxisInput)]

    ;; (println xAxisBools)
    (loop [i 0]
      (if (< i xCount)
        (do
          (draw-stitch-segment (get xSegments i) (get xAxisBools i))
          (recur (inc i)))))
    
    (loop [i 0]
      (if (< i yCount)
        (do
          (draw-stitch-segment (get ySegments i) (get yAxisBools i))
          (recur (inc i)))))))