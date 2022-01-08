(ns sketch.dynamic
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]]
            [sketch.color :as colo]
            [sketch.hitomezashi :as hito]
            [sketch.divider :as divi])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])
  
  (:import [processing.core PShape PGraphics]))

;; window height x width -- 900 x 900 for drawing
(def window-width 481)
(def window-height 640)

(def img-url "tony3.jpg")
(def img (ref nil))

(defn setup []
  (dosync (ref-set img (load-image img-url)))
  )

(defn draw []
  (no-loop)
  ;; (color-mode :hsb 360 100 100 1.0)
  ;; (stroke-join :miter)
  (stroke-cap :project)
  (stroke 150 160 90)
  (stroke-weight 4)

  (doseq [img-num (range 2)] ;; picks how many pictures to make

    (background 20 50 204)
    ;; (image @img 0 0)

    ;; do drawing here

    ;;  (divider/drawGrid 50)
    ;;  (println divider/triangle-map)


    ;; (if (> img-num 0)
    (doseq [x (range 1)]

      (hito/hito-stitch "howlonggodeeeeprpepeplkrlibiroiemekmvijerihekngkjiwgjoegnmklnwdiohjbowijret" "whatisgoingonehiegtroiemfbyekdlciivjhroeodightrbgibolksodiitititimdlvjgdoemfbvubioensfksdooe" 10 [0 0 window-width window-height])
      (reset! divi/triangle-map {:triangle-count 0 :triangles []})
      (divi/buildTriangles 14)
      (doseq [tri (@divi/triangle-map :triangles)]
        (let [p (:pix tri)
              aver-r (colo/calculateAverageColor p :r)
              aver-g (colo/calculateAverageColor p :g)
              aver-b (colo/calculateAverageColor p :b)]
          (doseq [coord p]
            (let [rgb (first (colo/getPixelColors (vector coord)))
                  x (:x coord)
                  y (:y coord)]
              (set-pixel x y (color aver-r aver-g aver-b))))))
      (hito/hito-stitch "howlonggodeeeeprpepeplkrlibiroiemekmvijerihekngkjiwgjoegnmklnwdiohjbowijret" "whatisgoingonehiegtroiemfbyekdlciivjhroeodightrbgibolksodiitititimdlvjgdoemfbvubioensfksdooe" 20 [0 0 window-width window-height])
        ;; (reset! divi/square-map {:square-count 0 :squares []})
        ;; (divi/divideGoldenRectangles 0 0 (width) (height) 0 14)
        ;; (doseq [sqr (@divi/square-map :squares)]
        ;;   (let [p (:pix sqr)
        ;;         aver-r (colo/calculateAverageColor p :r)
        ;;         aver-g (colo/calculateAverageColor p :g)
        ;;         aver-b (colo/calculateAverageColor p :b)]
        ;;     (doseq [coord p]
        ;;       (let [rgb (first (colo/getPixelColors (vector coord)))
        ;;             x (:x coord)
        ;;             y (:y coord)]
        ;;         (set-pixel x y (color aver-r aver-g aver-b))))))
      )
        ;; )






    (save (str "sketch-" img-num ".tif"))
    (let [filename (str "sketch-" img-num ".tif")
          thumbnail (str "sketch-" img-num "-1000.tif")]
      (save filename)
      (sh "convert" "-LZW" filename filename)
      (sh "convert" "-scale" "1000x1000" filename thumbnail)
      (println "Done with image" img-num))))


  
  
