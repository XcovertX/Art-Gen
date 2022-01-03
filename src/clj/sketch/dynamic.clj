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


(def img-url "rhc.jpeg")
(def img (ref nil))

(defn setup []
  (dosync (ref-set img (load-image img-url)))
  )

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

(defn draw []
  (no-loop)
  ;; (color-mode :hsb 360 100 100 1.0)
  (stroke 40 90 90)
  (stroke-weight 1)

  (doseq [img-num (range 10)] ;; picks how many pictures to make

    (background 0 0 0)
    (image @img 0 0)

    ;; do drawing here

    ;;  (divider/drawGrid 50)
    ;;  (println divider/triangle-map)



    (reset! divider/triangle-map {:triangle-count 0 :triangles []})
    (divider/buildTriangles 14)
    (doseq [tri (@divider/triangle-map :triangles)]
      (let [p (:pix tri)
            aver-r (calculateAverageColor p :r)
            aver-g (calculateAverageColor p :g)
            aver-b (calculateAverageColor p :b)]
        (doseq [coord p]
          (let [rgb (first (getPixelColors (vector coord)))
                x (:x coord)
                y (:y coord)]
            (set-pixel x y (color aver-r aver-g aver-b))))))



    (save (str "sketch-" img-num ".tif"))
    (let [filename (str "sketch-" img-num ".tif")
          thumbnail (str "sketch-" img-num "-1000.tif")]
      (save filename)
      (sh "convert" "-LZW" filename filename)
      (sh "convert" "-scale" "1000x1000" filename thumbnail)
      (println "Done with image" img-num))))


  
  
