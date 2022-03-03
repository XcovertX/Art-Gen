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
(def window-width 799)
(def window-height 605)

(def img-url "abstract-painting.jpg")
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
    ;; (image @img 0 0)

    ;; do drawing here

    ;;  (divider/drawGrid 50)
    ;;  (println divider/triangle-map)

    (if (> img-num 0)
      (doseq [x (range 1)]
        (reset! divi/triangle-map {:triangle-count 0 :triangles []})
        (divi/buildTriangles 13)
        (doseq [tri (@divi/triangle-map :triangles)]
          (let [p (:pix tri)
                triangle-center (cal/calculateTriangleCenter [(:x1 tri)
                                                              (:y1 tri)
                                                              (:x2 tri)
                                                              (:y2 tri)
                                                              (:x3 tri)
                                                              (:y3 tri)])

                distance-to-center (cal/calculateDistanceFromCenter triangle-center)
                rand (random 100)
              ;; average? (cond
              ;;           (>= distance-to-center 600) (if (< (random 100) 50)
              ;;                                        true
              ;;                                        false)
              ;;           (>= distance-to-center 500) (if (< (random 100) 32)
              ;;                                        true
              ;;                                        false)
              ;;           (>= distance-to-center 400) (if (< (random 100) 16)
              ;;                                        true
              ;;                                        false)
              ;;           (>= distance-to-center 200) (if (< (random 100) 8)
              ;;                                        true
              ;;                                        false)
              ;;           :else false)
                depth (cond
                        (>= (:iteration tri) 13) 26
                        (= (:iteration tri) 12) 24
                        (= (:iteration tri) 11) 22
                        (= (:iteration tri) 10) 20
                        (= (:iteration tri) 9) 18
                        (= (:iteration tri) 8) 16
                        (= (:iteration tri) 7) 14
                        (= (:iteration tri) 6) 12
                        (= (:iteration tri) 5) 10
                        (= (:iteration tri) 4) 8
                        (= (:iteration tri) 3) 6
                        (= (:iteration tri) 2) 4
                        (= (:iteration tri) 1) 2
                        :else 0)

              ;; aver-r (if (= average? false)
              ;;          (colo/calculateAverageColor p :r)
              ;;          (- (colo/calculateAverageColor p :r) 30))
              ;; aver-g (if (= average? false)
              ;;          (colo/calculateAverageColor p :g)
              ;;          (- (colo/calculateAverageColor p :g) 30))
              ;; aver-b (if (= average? false)
              ;;          (colo/calculateAverageColor p :b)
              ;;          (- (colo/calculateAverageColor p :b) 30))

                aver-r (- (colo/calculateAverageColor p :r) depth)
                aver-g (+ (colo/calculateAverageColor p :g) depth)
                aver-b (+ (colo/calculateAverageColor p :b) depth)]

            ;; sets shape color to average of the shape's collection of pixels
            ;; (doseq [coord p]
            ;;   (let [rgb (first (colo/getPixelColors (vector coord)))
            ;;         x (:x coord)
            ;;         y (:y coord)]

            ;;     (set-pixel x y (color aver-r aver-g aver-b))))

            (colo/fillShape tri (color 10 78 136))))))

    (save (str "sketch-" img-num ".tif"))
    (let [filename (str "sketch-" img-num ".tif")
          thumbnail (str "sketch-" img-num "-1000.tif")]
      (save filename)
      (sh "convert" "-LZW" filename filename)
      (sh "convert" "-scale" "1000x1000" filename thumbnail)
      (println "Done with image" img-num)))
(println "Job finished.")) 