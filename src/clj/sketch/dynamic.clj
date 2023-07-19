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
            [sketch.cart :as cart]
            [sketch.triangle :as tri]
            [sketch.draw :as draw]
            [sketch.path :as path]
            [sketch.ray_tracer :as rt]
            [sketch.select :as select])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution]) 
  (:import [processing.core PShape PGraphics]))

;; window height x width -- 900 x 900 for drawing
(def window-width 450)
(def window-height 800)

(def img-url "source_images/tonybw.jpg")
(def img (ref nil))
(def canvas (atom {:paths []}))
(def counter (atom 0))
(def node-count (atom 0))
(def to-draw (atom {:primary []
                    :temporary []}))
(def tool-in-use (atom {:select-tool false}))


(defn setup []
  (dosync (ref-set img (load-image img-url)))
  (color-mode :hsb)
  (stroke 360 360 360)
  (stroke-weight 3)
  (ellipse-mode :radius)
  (background 0 0 0)
  ;; (fill 0)
  (reset! canvas {:paths [] :lines []})
  (reset! counter 0)
  (reset! select/select-shapes {:polygon-select-complete false
                                :polygon-select []})
  (reset! tree/trees true)
  (reset! tree/counter 0)
  (reset! tree/i 0)
  (reset! node-count 0)
  (reset! path/nodeIDCounter 0)
  (reset! path/pathIDCounter 0)
  (reset! select/select-busy false)
  (reset! to-draw {:primary []
                   :temporary []})
  (reset! tool-in-use {:select-tool false})
  (reset! cart/all-cart {:carts []
                         :count 300 
                         :direction "EAST"
                         :width window-width
                         :height window-height
                         :part-width 20
                         :part-height 20
                         :color 1
                         :part-count 1
                         :min-speed 3000
                         :max-speed 300
                         :edge-extention 1000
                         :draw-stationary-parts false
                         :draw-gridlines true
                         :draw-cart-center false
                         })
  ;; (no-loop) 
  )

(defn export-JPG
  "exports the canvas picture to a given folder"
  [file-name]
  (let [filename (str "sketch-" file-name ".jpg")]
    (save (str "saved-images/" filename))
    (println "Done with" filename)))  

(defn export-Canvas
  "exports the canvas picture to a given folder"
  [file-name]
  (save (str "sketch-" file-name ".tif"))
  (let [filename (str "sketch-" file-name ".tif")
        thumbnail (str "sketch-" file-name "-1000.tif")]
    (save filename)
    (sh "convert" "-LZW" filename filename)
    (sh "convert" "-scale" "1000x1000" filename thumbnail)
    (println "Done with image" file-name)))


    
(defn draw-temporary-shapes
  []
  (doseq [idx (range (count (:polygon-select @select/select-shapes)))]
    (let [s (get (:polygon-select @select/select-shapes) idx)]
      (when (= (:type s) "ellipse")
        (ellipse (:x s) (:y s) (:w s) (:h s))
        (when (= idx (- (count (:polygon-select @select/select-shapes)) 1))
          (if (:polygon-select-complete @select/select-shapes)
            (let [start-point (first (:polygon-select @select/select-shapes))]
              (line (:x s) (:y s) (:x start-point) (:y start-point)))
            (line (:x s) (:y s) (mouse-x) (mouse-y)))))
      (when (= (:type s) "line")
        (line (:x1 s) (:y1 s) (:x2 s) (:y2 s))))))

(defn add-to-draw
 "adds a colection of shapes to me drawn"
 [data]
 (swap! to-draw assoc-in [:primary] (conj (:primary @to-draw) data)))

(defn draw-primary
  ""
  []
  (image @img 0 0 window-width window-height)
  (when (not (empty? (:primary @to-draw)))
    (let [td (first (:primary @to-draw))] 
      (swap! to-draw assoc-in [:primary] (rest (:primary @to-draw)))
      (tri/draw-triangle-map-average td)
      (export-JPG @counter)
      (dosync (ref-set img (load-image (str "saved-images/sketch-" @counter ".jpg"))))
      (swap! counter inc))))

(defn draw-starting-image
  "draws the first image to the screen"
  []
  (image @img 0 0 window-width window-height)
  (export-JPG @counter)
  (println "Done with image" @counter)
  (swap! counter inc))

(defn draw-temporary
  "draws temporary objects to the screen after the primary image has been updated"
  []
  (draw-temporary-shapes))

(defn draw []
  (if (< @counter 1)
    (do
      (cart/cart-generator)
      (swap! counter inc))
    (do
      (background 0 0 0)
      (cart/update-carts)
      (swap! counter inc))))

;; (defn draw []

;;   (if (< @counter 1)
;;     (draw-starting-image)
;;     (do
;;       (draw-primary)
;;       (draw-temporary)
;;       (when (and (mouse-pressed?)
;;                  (not @select/select-busy))
;;         (swap! select/select-busy not)
;;         (let [area (select/select-polygon)]

;;           (async/go
;;             (let [triangle-data (tri/buildTriangles {:draw-type "average"
;;                                                      :area (async/<! area)
;;                                                      :x-min 0
;;                                                      :y-min 0
;;                                                      :x-max window-width
;;                                                      :y-max window-height
;;                                                      :depth 7
;;                                                      :triangle-map (atom
;;                                                                     {:triangle-count 0
;;                                                                      :node-count 0
;;                                                                      :triangles []
;;                                                                      :nodes {}})})]
;;               (add-to-draw triangle-data)
;;               (select/clear-select)
;;               (swap! select/select-busy not))))))))

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
 




