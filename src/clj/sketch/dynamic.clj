(ns sketch.dynamic
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]]
            [sketch.color :as colo]
            [sketch.hitomezashi :as hito]
            [sketch.divider :as divi]
            [sketch.calculations :as cal])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])
  
  (:import [processing.core PShape PGraphics]))

;; window height x width -- 900 x 900 for drawing
(def window-width 1920)
(def window-height 1080)

(def img-url "abstract2.jpg")
(def img (ref nil))

(defn setup []
  (dosync (ref-set img (load-image img-url))))

(defn draw []
  (no-loop)
  ;; (color-mode :hsb 360 100 100 1.0)
  ;; (stroke-join :miter)
  ;; (stroke-cap :project)
  (stroke 210 100 115)
  (stroke-weight 3)

  (doseq [img-num (range 2)] ;; picks how many pictures to make

    (background 0 0 0)
    (image @img 0 0)

    ;; do drawing here

    ;;  (divider/drawGrid 50)
    ;;  (println divider/triangle-map)

    (println (cal/calculateTriangleCenter {:x1 5 :y1 5 :x2 43 :y2 20 :x3 32 :y3 18}))

    ;; (if (> img-num 0)
    ;; (doseq [x (range 2)]

    ;;   (reset! divi/triangle-map {:triangle-count 0 :triangles []})
    ;;   (divi/buildTriangles 14)
    ;;   (doseq [tri (@divi/triangle-map :triangles)]
    ;;     (let [p (:pix tri)
    ;;           aver-r (colo/calculateAverageColor p :r)
    ;;           aver-g (colo/calculateAverageColor p :g)
    ;;           aver-b (colo/calculateAverageColor p :b)]
    ;;       (doseq [coord p]
    ;;         (let [rgb (first (colo/getPixelColors (vector coord)))
    ;;               x (:x coord)
    ;;               y (:y coord)]
    ;;           (set-pixel x y (color aver-r aver-g aver-b))))))))


      (save (str "sketch-" img-num ".tif"))
      (let [filename (str "sketch-" img-num ".tif")
            thumbnail (str "sketch-" img-num "-1000.tif")]
        (save filename)
        (sh "convert" "-LZW" filename filename)
        (sh "convert" "-scale" "1000x1000" filename thumbnail)
        (println "Done with image" img-num))))


  
  
