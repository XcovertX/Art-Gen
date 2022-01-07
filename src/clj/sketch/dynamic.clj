(ns sketch.dynamic
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]]
            [sketch.divider :as divider]
            [sketch.color :as color]
            [sketch.hitomezashi :as hito])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])
  
  (:import [processing.core PShape PGraphics]))

;; window height x width -- 900 x 900 for drawing
(def window-width 500)
(def window-height 500)

(def img-url "dew.jpg")
(def img (ref nil))

(defn setup []
  (dosync (ref-set img (load-image img-url)))
  )

(defn draw []
  (no-loop)
  ;; (color-mode :hsb 360 100 100 1.0)
  (stroke 40 90 90)
  (stroke-weight 1)

  (doseq [img-num (range 10)] ;; picks how many pictures to make

    (background 0 0 0)
    ;; (image @img 0 0)

    ;; do drawing here

    ;;  (divider/drawGrid 50)
    ;;  (println divider/triangle-map)


    ;; (if (> img-num 0)
    ;;  (do
    ;;   (reset! divider/triangle-map {:triangle-count 0 :triangles []})
    ;;   (divider/buildTriangles 12)
    ;;   (doseq [tri (@divider/triangle-map :triangles)]
    ;;     (let [p (:pix tri)
    ;;           aver-r (calculateAverageColor p :r)
    ;;           aver-g (calculateAverageColor p :g)
    ;;           aver-b (calculateAverageColor p :b)]
    ;;       (doseq [coord p]
    ;;         (let [rgb (first (getPixelColors (vector coord)))
    ;;               x (:x coord)
    ;;               y (:y coord)]
    ;;           (set-pixel x y (color aver-r aver-g aver-b)))))))

      ;; (let [point (divider/findGoldenRatio divider/window-width)]
      ;;   (divider/drawVerticalLine point {:start 0 :end divider/window-height}))
      ;; (do
      ;;   (reset! divider/square-map {:square-count 0 :squares []})
      ;;   (divider/divideGoldenRectangles 0 0 divider/window-width divider/window-height 0 14)
      ;;   (doseq [sqr (@divider/square-map :squares)]
      ;;     (let [p (:pix sqr)
      ;;           aver-r (color/calculateAverageColor p :r)
      ;;           aver-g (color/calculateAverageColor p :g)
      ;;           aver-b (color/calculateAverageColor p :b)]
      ;;       (doseq [coord p]
      ;;         (let [rgb (first (color/getPixelColors (vector coord)))
      ;;               x (:x coord)
      ;;               y (:y coord)]
      ;;           (set-pixel x y (color aver-r aver-g aver-b)))))))
      ;; )

    ;; (let [lines (into [] (divider/divideCanvasVertically 200))]
    ;;   (doseq [x lines]
    ;;    (divider/drawVerticalLine x {:start 0 :end window-height})))

    (hito/hito-stitch 3 3 10 [0 0 window-width window-height])

    (save (str "sketch-" img-num ".tif"))
    (let [filename (str "sketch-" img-num ".tif")
          thumbnail (str "sketch-" img-num "-1000.tif")]
      (save filename)
      (sh "convert" "-LZW" filename filename)
      (sh "convert" "-scale" "1000x1000" filename thumbnail)
      (println "Done with image" img-num))))


  
  
