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

(defn h
  ([] (h 1.0))
  ([value] (* (height) value)))

(defn w
  ([] (w 1.0))
  ([value] (* (width) value)))

(defn draw []
  (no-loop)
  (color-mode :hsb 360 100 100 1.0)
  (stroke 40 90 90 1)

  (doseq [img-num (range 10)]
    (background 220 49 66)
    ;; do drawing here

    (rect (w 0.1) (h 0.1) (w 0.5) (h 0.5))
    (line 0 0 100 100)

    (save (str "sketch-" img-num ".tif"))
    (let [filename (str "sketch-" img-num ".tif")
          thumbnail (str "sketch-" img-num "-1000.tif")]
      (save filename)
      (sh "convert" "-LZW" filename filename)
      (sh "convert" "-scale" "1000x1000" filename thumbnail)
      (println "Done with image" img-num))))

(defn setup []
  
  )
