(ns sketch.dynamic
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]]
            [sketch.color :as colo]
            [sketch.hitomezashi :as hito]
            [sketch.divider :as divi]
            [sketch.cell :as cell]
            [sketch.calculations :as cal]
            [sketch.grow :as grow]
            [sketch.coral :as coral]
            [sketch.tree :as tree]
            [sketch.shapes :as shape]
            [sketch.draw :as draw]
            [sketch.path :as path]
            [sketch.ray-tracer :as rt])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])

  (:import [processing.core PShape PGraphics]))

;; window height x width -- 900 x 900 for drawing
(def window-width 1100)
(def window-height 700)

(def img-url "source_images/eye.jpg")
(def img (ref nil))
(def canvas (atom {:paths []}))
(def counter (atom 0))
(def node-count (atom 0))

(defn setup []
  ;; (dosync (ref-set img (load-image img-url)))
  (color-mode :hsb)
  (stroke 360 0 360)
  (stroke-weight 2)
  (background 0 0 0)
  (fill 255)
  (reset! canvas {:paths [] :lines []})
  (reset! counter 0)
  (reset! tree/trees true)
  (reset! tree/counter 0)
  (reset! tree/i 0)
  (reset! node-count 0)
  (reset! path/nodeIDCounter 0)
  (reset! path/pathIDCounter 0)
  ;; (no-loop)
  )

(defn exportCanvas
  "exports the canvas picture to a given folder"
  [folder-path file-name]
  (save (str "sketch-" file-name ".tif"))
  (let [filename (str "sketch-" file-name ".tif")
        thumbnail (str "sketch-" file-name "-1000.tif")]
    (save filename)
    (sh "convert" "-LZW" filename filename)
    (sh "convert" "-scale" "1000x1000" filename thumbnail)
    (println "Done with image" file-name)))

(defn draw []
  (background 0 0 0)

  (if (and (= @counter 0)
           @tree/trees)
    (swap! canvas assoc-in [:paths]
           (conj (:paths @canvas) (tree/seed-tree
                                   {:x 0 :y (+ window-height 150)}
                                   {:x window-width :y (+ window-height 150)}
                                   {:is-random? true
                                    :growth-delay (rand-int 200)
                                    :seed-count 5
                                    :branch-rate 50
                                    :seeds [100 200 300]})))
    (doseq [path-index (range (count (:paths @canvas)))]

      (if @tree/trees
        (when (= (:type (:data (get (:paths @canvas) path-index))) "tree")

          (swap! canvas assoc-in [:paths path-index] (tree/applyTreeGrowth (get (:paths @canvas) path-index) window-width window-height))

          (when (not @tree/trees)
            (swap! canvas assoc-in [:lines] (path/convertPathToLines (get (:paths @canvas) path-index)
                                                                     0 window-width
                                                                     0 window-height))))

        (let [rays1 (rt/getRays {:x (mouse-x) :y (mouse-y)} 150 2000 (:lines @canvas))]
          (doseq [r (range (count rays1))]
            (line
             (mouse-x)
             (mouse-y)
             (:x (:point-b (get rays1 r)))
             (:y (:point-b (get rays1 r))))) 
          (draw/drawPath (get (:paths @canvas) path-index))
          (exportCanvas "test" (str "rays-" @counter))))
      (draw/drawPath (get (:paths @canvas) path-index))))
  (swap! counter inc))




;; (defn draw []

;;   (background 0 0 0)
;;   (if (= @counter 0)
;;     (do

;;       (swap! canvas assoc-in [:paths] (conj (:paths @canvas) (shape/createRectangle (- window-width 100) (- window-height 100) {:x (/ window-width 2) :y (/ window-height 2)})))
;;       (swap! canvas assoc-in [:paths]
;;              (conj (:paths @canvas) (tree/seed-tree
;;                                      {:x 0 :y (- window-height 50)}
;;                                      {:x window-width :y (- window-height 50)}
;;                                      {:is-random? true
;;                                       :growth-delay (rand-int 150)
;;                                       :seed-count 5
;;                                       :branch-rate 50
;;                                       :seeds [100 200 300]})))
;;       ;; (swap! canvas assoc-in [:paths 0] (tree/branch (get (:paths @canvas) 0) true))
;;       ;; (swap! canvas assoc-in [:paths] (conj (:paths @canvas) (tree/seed-tree {:x 0 :y (- window-height 200)} {:x window-width :y (- window-height 200)} 2)))
;;       ;; (swap! canvas assoc-in [:paths] (conj (:paths @canvas) (tree/seed-tree {:x 0 :y (- window-height 100)} {:x window-width :y (- window-height 100)} 2)))
;;       ;; (println (:paths @canvas))
;;       (doseq [path (:paths @canvas)
;;               :let [nodes (:nodes path)]]
;;         (doseq [node nodes]
;;           (println "pos:" (:position node) "id:" (:ID node) "pid:" (:parent-node-id (:data node)) "age:" (:age (:data path)) "dgb:" (:delay-growth-by (:data node)) "branch-count:" (:branch-count (:data node))))
;;         (draw/drawPath path)))

;;     (let []
;;       (doseq [path-index (range (count (:paths @canvas)))]

;;         (when (= (:type (:data (get (:paths @canvas) path-index))) "tree")

;;           (swap! canvas assoc-in [:paths path-index] (tree/applyTreeGrowth (get (:paths @canvas) path-index) window-width window-height)))

;;         (when (= (:type (:data (get (:paths @canvas) path-index))) "shape")
;;           (swap! canvas assoc-in [:paths path-index]
;;                  (if (> (:age (:data (get (:paths @canvas) path-index))) 230)
;;                    (path/setAllNodesToFixed (get (:paths @canvas) path-index))
;;                    (path/incPathAge (shape/adjustRectangle
;;                                      (get (:paths @canvas) path-index)
;;                                      (- window-width @counter)
;;                                      (- window-height @counter)
;;                                      {:x (/ window-width 2) :y (/ window-height 2)})))))
;;         ;; (when (< @node-count (count nodes)) 
;;         ;;   (println "---------------")
;;         ;;   (doseq [node nodes] 
;;         ;;     (println "pos:" (:position node) "id:" (:ID node) "pid:" (:parent-node-id (:data node)) "age:" (:age (:data path)) "dgb:" (:delay-growth-by (:data node)) "branch-count:" (:branch-count (:data node))))) 
;;         ;; (reset! node-count (count nodes))
;;         (draw/drawPath (get (:paths @canvas) path-index)))))
;;   (swap! counter inc)
;;   ;; (Thread/sleep 100)
;;   )
 

;; mouse example
  ;; (doseq [[ind capt fn] [[0 "mouse-button" mouse-button]
  ;;                        [1 "mouse-pressed?" mouse-pressed?]
  ;;                        [2 "mouse-x" mouse-x] [3 "mouse-y" mouse-y]
  ;;                        [4 "pmouse-x" pmouse-x] [5 "pmouse-y" pmouse-y]]]
  ;;   (text (str capt " " (fn)) 10 (+ (* 20 ind) 20)))











  ;; (no-loop)
  ;; (color-mode :hsb 360 100 100 1.0)
  ;; (stroke-join :miter)
  ;; (stroke-cap :project)
  ;; (stroke 210 100 115)
  ;; (stroke-weight 3)

;;   (doseq [img-num (range 2)] ;; picks how many pictures to make


;;     ;; (image @img 0 0)

;;     ;; do drawing here

;;     ;;  (divider/drawGrid 50)
;;     ;;  (println divider/triangle-map)

;;     (if (> img-num 0)
;;       (doseq [x (range 1)]
;;         ;; (reset! divi/triangle-map {:triangle-count 0 :triangles []})
;;         ;; (divi/buildTriangles 13)
;;         ;; (doseq [tri (@divi/triangle-map :triangles)]
;;         ;;   (let [p (:pix tri)
;;         ;;         triangle-center (cal/calculateTriangleCenter [(:x1 tri)
;;         ;;                                                       (:y1 tri)
;;         ;;                                                       (:x2 tri)
;;         ;;                                                       (:y2 tri)
;;         ;;                                                       (:x3 tri)
;;         ;;                                                       (:y3 tri)])

;;         ;;         distance-to-center (cal/calculateDistanceFromCenter triangle-center)
;;         ;;         rand (random 100)
;;         ;;       ;; average? (cond
;;         ;;       ;;           (>= distance-to-center 600) (if (< (random 100) 50)
;;         ;;       ;;                                        true
;;         ;;       ;;                                        false)
;;         ;;       ;;           (>= distance-to-center 500) (if (< (random 100) 32)
;;         ;;       ;;                                        true
;;         ;;       ;;                                        false)
;;         ;;       ;;           (>= distance-to-center 400) (if (< (random 100) 16)
;;         ;;       ;;                                        true
;;         ;;       ;;                                        false)
;;         ;;       ;;           (>= distance-to-center 200) (if (< (random 100) 8)
;;         ;;       ;;                                        true
;;         ;;       ;;                                        false)
;;         ;;       ;;           :else false)
;;         ;;         depth (cond
;;         ;;                 (>= (:iteration tri) 13) 26
;;         ;;                 (= (:iteration tri) 12) 24
;;         ;;                 (= (:iteration tri) 11) 22
;;         ;;                 (= (:iteration tri) 10) 20
;;         ;;                 (= (:iteration tri) 9) 18
;;         ;;                 (= (:iteration tri) 8) 16
;;         ;;                 (= (:iteration tri) 7) 14
;;         ;;                 (= (:iteration tri) 6) 12
;;         ;;                 (= (:iteration tri) 5) 10
;;         ;;                 (= (:iteration tri) 4) 8
;;         ;;                 (= (:iteration tri) 3) 6
;;         ;;                 (= (:iteration tri) 2) 4
;;         ;;                 (= (:iteration tri) 1) 2
;;         ;;                 :else 0)

;;         ;;       ;; aver-r (if (= average? false)
;;         ;;       ;;          (colo/calculateAverageColor p :r)
;;         ;;       ;;          (- (colo/calculateAverageColor p :r) 30))
;;         ;;       ;; aver-g (if (= average? false)
;;         ;;       ;;          (colo/calculateAverageColor p :g)
;;         ;;       ;;          (- (colo/calculateAverageColor p :g) 30))
;;         ;;       ;; aver-b (if (= average? false)
;;         ;;       ;;          (colo/calculateAverageColor p :b)
;;         ;;       ;;          (- (colo/calculateAverageColor p :b) 30))

;;         ;;         aver-r (- (colo/calculateAverageColor p :r) depth)
;;         ;;         aver-g (+ (colo/calculateAverageColor p :g) depth)
;;         ;;         aver-b (+ (colo/calculateAverageColor p :b) depth)]

;;             ;; sets shape color to average of the shape's collection of pixels
;;             ;; (doseq [coord p]
;;             ;;   (let [rgb (first (colo/getPixelColors (vector coord)))
;;             ;;         x (:x coord)
;;             ;;         y (:y coord)]

;;             ;;     (set-pixel x y (color aver-r aver-g aver-b))))))

;;         ;; (reset! cell/cell-map {:cell-count 0 :cells []})
;;         ;; (doseq [i (range 1)]
;;         ;;   (cell/buildCell {:x 100 :y 100 :growable true} 5 1)
;;         ;;   (cell/buildCell {:x 110 :y 110 :growable true} 3 1)
;;         ;;   (cell/buildCell {:x 90 :y 130 :growable true} 2 1)
;;         ;;   (cell/buildCell {:x 160 :y 80 :growable true} 1 1)
;;         ;;   (cell/buildCell {:x 30 :y 93 :growable true} 10 1)
;;         ;;   (cell/buildCell {:x 41 :y 53 :growable true} 2 1)
;;         ;;   (cell/buildCell {:x 10 :y 180 :growable true} 2 1))

;;         ;; (doseq [i (range 10)]
;;         ;;   (cell/growBres))
;;         ;; (cell/drawCells (color 255 (rand-int 255) (rand-int 255)))


;;         (reset! cell/node-map {:nodes []})
;;         ))

;;     (save (str "sketch-" img-num ".tif"))
;;     (let [filename (str "sketch-" img-num ".tif")
;;           thumbnail (str "sketch-" img-num "-1000.tif")]
;;       (save filename)
;;       (sh "convert" "-LZW" filename filename)
;;       (sh "convert" "-scale" "1000x1000" filename thumbnail)
;;       (println "Done with image" img-num)))
;; (println "Job finished.")) 
