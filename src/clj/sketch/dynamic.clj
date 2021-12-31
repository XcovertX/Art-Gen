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


(def img-url "test.jpg")
(def img (ref nil))

(defn setup []
  (dosync (ref-set img (load-image img-url)))
  )

(defn getPixelColors
  "retrieves a collection of colors for a given group of pixels"
  [pixel-collection]
  (map
   (fn [pix]
     (let [x (pix :x)
           y (pix :y)]
       {:r (red (get-pixel 11 11)) :g (green (get-pixel 11 2)) :b (blue (get-pixel 222 3))}))
   (for [pix pixel-collection] pix)))

(defn calculateAverageColor
  "calculates the average color of a given collection of colors"
  [colors]
  (colors 0))

(defn draw []
  (no-loop)
  ;; (color-mode :hsb 360 100 100 1.0)
  (stroke 40 90 90)
  (stroke-weight 1)

  (doseq [img-num (range 2)] ;; picks how many pictures to make

    (background 0 0 0)
    (image @img 0 0)


    ;;  (ellipse 100 100 100 100)

    ;; do drawing here

    ;;  (divider/drawGrid 50)
    ;;  (println divider/triangle-map)



    ;; (reset! divider/triangle-map {:triangle-count 0 :triangles []})
    (divider/buildTriangles 3)

    (prn (get (vector (getPixelColors (divider/getTrianglePixels [[0 0] [2 2] [0 2]]))) 0))

    ;; (prn (:pix (get (@divider/triangle-map :triangles) 4)))
    ;; (let [ts (@divider/triangle-map :triangles)
    ;;       t (get ts 4)
    ;;       p (:pix t)]
    ;;   (doseq [coord p]
    ;;     (set-pixel (coord :x) (coord :y) (color 120 44 44))))
    
    ;; (doseq [tri (@divider/triangle-map :triangles)]
    ;;   (let [p (:pix tri)
    ;;         r (random 255)
    ;;         g (random 255)
    ;;         b (random 255)]
    ;;     (doseq [coord p]
    ;;       (set-pixel (coord :x) (coord :y) (color r g b)))))
    
        ;; (doseq [tri (@divider/triangle-map :triangles)]
        ;;   (let [p (:pix tri)
        ;;         clrs (getPixelColors p)]
        ;;     (doseq [coord p]
        ;;       (set-pixel 
        ;;        (coord :x) (coord :y) 
        ;;        (color 100 (clrs :g) (clrs :b))))))
        
    ;; (divider/draw-triangle img window-width window-height )
    ;;  (pprint (str "Total triangles: " (get @divider/triangle-map :triangle-count)))
    ;;  (pprint @divider/triangle-map)


    (save (str "sketch-" img-num ".tif"))
    (let [filename (str "sketch-" img-num ".tif")
          thumbnail (str "sketch-" img-num "-1000.tif")]
      (save filename)
      (sh "convert" "-LZW" filename filename)
      (sh "convert" "-scale" "1000x1000" filename thumbnail)
      (println "Done with image" img-num))))


  
  
