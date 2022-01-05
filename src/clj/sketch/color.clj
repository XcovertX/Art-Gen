(ns sketch.color
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])

  (:import [processing.core PShape PGraphics]))

(defn getPixelColors
  "retrieves a collection of colors for a given group of pixels"
  [pixel-collection]
  (map
   (fn [pix]
     (let [x (:x pix)
           y (:y pix)]
       {:r (red (get-pixel x y)) :g (green (get-pixel x y)) :b (blue (get-pixel x y))}))
   (for [pix pixel-collection] pix)))

(defn calculateAverageColor
  "calculates the average color of a given collection of colors"
  [pixels key]
  (int
   (/
    (reduce +
            (map
             (fn [pixel]
               (let [color (first (getPixelColors (vector pixel)))
                     rgb (key color)]
                 (conj rgb)))
             (for [pixel pixels] pixel)))
    (let [num (count pixels)]
      (if (> num 0)
        num
        1)))))