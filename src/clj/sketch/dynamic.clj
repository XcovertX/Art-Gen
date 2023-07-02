(ns sketch.dynamic
  (:require [clojure.core.async.impl.ioc-macros]
            [quil.core :refer :all]
            [clojure.java.shell :refer [sh]]
            [clojure.core.async :as async]
            [sketch.hitomezashi :as hito]
            [sketch.divider :as divi]
            [sketch.grow :as grow]
            [sketch.coral :as coral]
            [sketch.tree :as tree]
            [sketch.triangle :as tri]
            [sketch.draw :as draw]
            [sketch.path :as path]
            [sketch.ray_tracer :as rt])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution]) 
  (:import [processing.core PShape PGraphics]))

;; window height x width -- 900 x 900 for drawing
(def window-width 1000)
(def window-height 667)

(def img-url "source_images/sun.jpg")
(def img (ref nil))
(def canvas (atom {:paths []}))
(def counter (atom 0))
(def node-count (atom 0))
(def shapes (atom {:polygon-select-complete false
                   :polygon-select []}))
(def selectBusy (atom false))
(def toDraw (atom {:primary []
                   :temporary []}))
(def toolInUse (atom {:select-tool false}))

(defn setup []
  (dosync (ref-set img (load-image img-url))) 
  (color-mode :rgb)
  ;; (image-mode :corner)
  (stroke 360 360 360)
  (stroke-weight 2)
  ;; (background 0 0 0)
  (fill 0)
  (reset! canvas {:paths [] :lines []})
  (reset! counter 0)
  (reset! shapes {:polygon-select-complete false
                  :polygon-select []})
  ;; (reset! tree/trees true)
  ;; (reset! tree/counter 0)
  ;; (reset! tree/i 0)
  (reset! node-count 0)
  ;; (reset! path/nodeIDCounter 0)
  ;; (reset! path/pathIDCounter 0)
  (reset! selectBusy false)
  (reset! toDraw {:primary []
                  :temporary []})
  (reset! toolInUse {:select-tool false})
  ;; (no-loop) 
  )

(defn exportJPG
  "exports the canvas picture to a given folder"
  [file-name]
  (let [filename (str "sketch-" file-name ".jpg")]
    (save filename)
    (println "Done with" filename)))

(defn exportCanvas
  "exports the canvas picture to a given folder"
  [file-name]
  (save (str "sketch-" file-name ".tif"))
  (let [filename (str "sketch-" file-name ".tif")
        thumbnail (str "sketch-" file-name "-1000.tif")]
    (save filename)
    (sh "convert" "-LZW" filename filename)
    (sh "convert" "-scale" "1000x1000" filename thumbnail)
    (println "Done with image" file-name)))

(defn addEllipseToPolygonSelect
  "adds as ellipse to be drawn for the polygon selection tool"
  [point]
  (swap! shapes assoc-in [:polygon-select] (conj
                                            (:polygon-select @shapes)
                                            {:type "ellipse"
                                             :x (:x point)
                                             :y (:y point)
                                             :w 3
                                             :h 3})))

(defn addLineToPolygonSelect
  "adds a point that a line will be drawn to. 
   the originating point is center of the previous shape in the collection"
  [point-a point-b]
  (swap! shapes assoc-in [:polygon-select] (conj
                                            (:polygon-select @shapes)
                                            {:type "line"
                                             :x1 (:x point-a)
                                             :y1 (:y point-a)
                                             :x2 (:x point-b)
                                             :y2 (:y point-b)
                                             :stroke-weight 2})))

(defn markPolygonSelectPolygonComplete
  []
  (swap! shapes assoc-in [:polygon-select-complete] true))

(defn clearSelect
  "clears the select tool"
  []
  (swap! shapes assoc-in [:polygon-select] [])
  (swap! shapes assoc-in [:polygon-select-complete] false))

(defn selectPoint
  "user selects a point to be the next vertex"
  [prev-point]
  (loop []
    (if (not (mouse-pressed?))
      (let [x (mouse-x)
            y (mouse-y)
            point {:x x :y y}]
        (addEllipseToPolygonSelect prev-point)
        (addLineToPolygonSelect prev-point point)
        (addEllipseToPolygonSelect point)
        point)
      (recur))))

(defn selectPolygon
  "user selects n number of verticies to form a polygon"
  []
  (async/thread
    (loop [t [{:x (mouse-x) :y (mouse-y)}]]
      (if (mouse-pressed?)
        (recur (conj t (selectPoint (last t))))
        (if (key-pressed?)
          (do
            (markPolygonSelectPolygonComplete)
            t)
          (recur t))))))

(defn selectTriangle
  "user selects 3 spots to form an area"
  []
  (async/thread
    (let [point-a (loop []
                    (if (not (mouse-pressed?)) 
                      (let [x (mouse-x)
                            y (mouse-y)
                            point {:x x :y y}] 
                        (addEllipseToPolygonSelect point)
                        point)
                      (recur)))
          x1 (:x point-a)
          y1 (:y point-a)
          point-b (loop []
                    (if (mouse-pressed?)
                      (loop []
                        (if (not (mouse-pressed?)) 
                          (let [x (mouse-x)
                                y (mouse-y)
                                point {:x x :y y}]
                            (addLineToPolygonSelect point-a point)
                            (addEllipseToPolygonSelect point)
                            point)
                          (recur)))
                      (recur)))
          x2 (:x point-b)
          y2 (:y point-b)
          point-c (loop []
                    (if (mouse-pressed?)
                      (loop []
                        (if (not (mouse-pressed?)) 
                          (let [x (mouse-x)
                                y (mouse-y)
                                point {:x x :y y}]
                            (addLineToPolygonSelect point-b point)
                            (addEllipseToPolygonSelect point)
                            (markPolygonSelectPolygonComplete)
                            point)
                          (recur)))
                      (recur)))
          x3 (:x point-c)
          y3 (:y point-c)]
      [{:x x1 :y y1} {:x x2 :y y2} {:x x3 :y y3}])))
    
(defn drawShapes
  []
  (doseq [idx (range (count (:polygon-select @shapes)))]
    (let [s (get (:polygon-select @shapes) idx)]
      (when (= (:type s) "ellipse")
        (ellipse (:x s) (:y s) (:w s) (:h s))
        (when (= idx (- (count (:polygon-select @shapes)) 1))
          (if (:polygon-select-complete @shapes)
            (let [start-point (first (:polygon-select @shapes))]

              (line (:x s) (:y s) (:x start-point) (:y start-point)))
            (line (:x s) (:y s) (mouse-x) (mouse-y)))))
      (when (= (:type s) "line")
        (line (:x1 s) (:y1 s) (:x2 s) (:y2 s))))))

(defn addToDraw
 "adds a colection of shapes to me drawn"
 [data]
 (swap! toDraw assoc-in [:primary] (conj (:primary @toDraw) data)))

(defn drawPrimary
  ""
  []
  (image @img 0 0 window-width window-height)
  (when (not (empty? (:primary @toDraw)))
    (let [td (first (:primary @toDraw))] 
      (swap! toDraw assoc-in [:primary] (rest (:primary @toDraw)))
      (tri/drawTriangleMapAverage td)
      (exportJPG @counter)
      (dosync (ref-set img (load-image (str "sketch-" @counter ".jpg"))))
      (swap! counter inc))))

(defn drawStartingImage
  "draws the first image to the screen"
  []
  (image @img 0 0 window-width window-height)
  (exportJPG @counter)
  (println "Done with image" @counter)
  (swap! counter inc))

(defn drawTemporary
  ""
  []
  (drawShapes))

(defn draw []
  
  (if (< @counter 1)
    (drawStartingImage)
    (do
     (drawPrimary)
     (drawTemporary)
     (when (and (mouse-pressed?)
                (not @selectBusy))
       (swap! selectBusy not)
       (let [area (selectPolygon)]

         (async/go
           (let [triangle-data (tri/buildTriangles {:draw-type "average"
                                                    :area (async/<! area)
                                                    :x-min 0
                                                    :y-min 0
                                                    :x-max window-width
                                                    :y-max window-height
                                                    :depth 7
                                                    :triangle-map (atom
                                                                   {:triangle-count 0
                                                                    :node-count 0
                                                                    :triangles []
                                                                    :nodes {}})})]
             (addToDraw triangle-data)
             (clearSelect)
             (swap! selectBusy not))))))))

;; (defn draw []
;;   (background 0 0 0)

;;   (if (and (= @counter 0)
;;            @tree/trees)
;;     (swap! canvas assoc-in [:paths]
;;            (conj (:paths @canvas) (tree/seed-tree
;;                                    {:x 0 :y (+ window-height 150)}
;;                                    {:x window-width :y (+ window-height 150)}
;;                                    {:is-random? true
;;                                     :growth-delay (rand-int 200)
;;                                     :seed-count 5
;;                                     :branch-rate 50
;;                                     :seeds [100 200 300]})))
;;     (doseq [path-index (range (count (:paths @canvas)))]

;;       (if @tree/trees
;;         (when (= (:type (:data (get (:paths @canvas) path-index))) "tree")

;;           (swap! canvas assoc-in [:paths path-index] (tree/applyTreeGrowth (get (:paths @canvas) path-index) window-width window-height))

;;           (when (not @tree/trees)
;;             (swap! canvas assoc-in [:lines] (path/convertPathToLines (get (:paths @canvas) path-index)
;;                                                                      0 window-width
;;                                                                      0 window-height))))

;;         (let [rays1 (rt/getRays {:x (mouse-x) :y (mouse-y)} 150 2000 (:lines @canvas))]
;;           (doseq [r (range (count rays1))]
;;             (line
;;              (mouse-x)
;;              (mouse-y)
;;              (:x (:point-b (get rays1 r)))
;;              (:y (:point-b (get rays1 r)))))))
;;       (draw/drawPath (get (:paths @canvas) path-index))))
;;   (swap! counter inc))




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




