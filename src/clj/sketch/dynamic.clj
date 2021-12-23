(ns sketch.dynamic
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]]
            [sketch.divider :as divider])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])
  
  (:import [processing.core PShape PGraphics]))

(defn draw []
  (no-loop)
  (color-mode :hsb 360 100 100 1.0)
  (stroke 40 90 90 1)
  (stroke-weight 1)
   (doseq [img-num (range 1)] ;; picks how many pictures to make
     (background 0 0 0)
    ;; do drawing here

    ;;  (divider/drawGrid 50)
    ;;  (println divider/triangle-map)


     (reset! divider/triangle-map {:triangle-count 0 :triangles ()})
     (divider/buildTriangles 8)
     (pprint (str "Total triangles: " (get @divider/triangle-map :triangle-count)))
     (pprint @divider/triangle-map)

     (save (str "sketch-" img-num ".tif"))
     (let [filename (str "sketch-" img-num ".tif")
           thumbnail (str "sketch-" img-num "-1000.tif")]
       (save filename)
       (sh "convert" "-LZW" filename filename)
       (sh "convert" "-scale" "1000x1000" filename thumbnail)
       (println "Done with image" img-num))))

(defn setup [])
  
  
