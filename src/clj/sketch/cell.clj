(ns sketch.cell
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]]
            [sketch.calculations :as calc])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])

  (:import [processing.core PShape PGraphics]))

;; -------- Cellular propagation functions ----------

(def cell-map (atom {:cell-count 0 :cells []}))
(defrecord Cell [number growth-counter growth-increment growth-rate
                 pix center-pix cell-wall])

(defn addCell
  "adds a cell to cell-map"
  [new-cell]
  (swap! cell-map update-in [:cell-count] inc)
  (swap! cell-map assoc-in [:cells] (conj (@cell-map :cells) new-cell)))

(defn updateCell
  "atomically updates and existing cell within cell-map"
  [index]
  ;; (println (assoc-in (@cell-map :cells) [1 :growth-counter] 5))
  (swap! cell-map assoc-in [:cells index :growth-counter] 5))

(defn buildCell
  "builds a new cell"
  [center-pixel grow-inc grow-rate]
  (let [starting-cell (vector center-pixel)]
    (addCell
     (Cell.
      (@cell-map :cell-count) 0 grow-inc grow-rate
      starting-cell center-pixel starting-cell))))

(defn drawCell
  "draws a given cell"
  [cell cell-color]
  (doseq [p (:pix cell)]
    (let [x (:x p) y (:y p) growable (:growable p)]
      (if growable
        (set-pixel x y cell-color)))))

(defn drawCells
  "draws a given collection of cells"
  [cell-color]
  (doseq [cell (:cells @cell-map)]
    (drawCell cell (color (rand-int 255) (rand-int 255) (rand-int 255)))))

(defn growCells
  "grows a given group of cells"
  [cell-collection]
  (doseq [c cell-collection]
    (let [theta 0
          step (/ Math/PI 12)
          radius (:growth-counter c)
          center (:center c)]
      (doseq [p (:cell-wall c)]
        (if (:growable p)
          (doseq []
            (let [x (:x p) y (:y p)
                  h (+ x (* radius (cos theta)))
                  k (+ y (* radius (sin theta)))]
              (swap! cell-map assoc-in [:cells (:number c) :cell-wall]
                     (calc/calculateLine (vector x y) (vector h k))))))))))

(defn cellCollisionCheck
  "verifies a cell's potential growth does not collide with another cell
   returns a collection containing any pixel that collides with another cell"
  [cell-number pixel-collection cell-collection]
  (let [collided-pixels (atom {})]
    (doseq [cell cell-collection]
      (if (not= (:number cell) cell-number)
        (let [cell-interior-pixels (:pix cell)]
          (doseq [pixel pixel-collection]
            (if (.contains cell-interior-pixels pixel)
              (swap! collided-pixels assoc-in [0] pixel))))))
    @collided-pixels))



(defn collectCirclePixels
  "retrieves the pixels from four quadrants of a circle"
  [xc yc x y cell-number]
  (let [new-cell-pixels
        (vec (set (vector {:x (+ xc x) :y (+ yc y) :growable true}
                          {:x (- xc x) :y (+ yc y) :growable true}
                          {:x (+ xc x) :y (- yc y) :growable true}
                          {:x (- xc x) :y (- yc y) :growable true}
                          {:x (+ xc y) :y (+ yc x) :growable true}
                          {:x (- xc y) :y (+ yc x) :growable true}
                          {:x (+ xc y) :y (- yc x) :growable true}
                          {:x (- xc y) :y (- yc x) :growable true})))

        colliding-pixels
        (cellCollisionCheck cell-number new-cell-pixels (@cell-map :cells))
        growable-pixels
        (filterv #(not (some (fn [u] (= u %)) colliding-pixels)) new-cell-pixels)
        total-cell-wall-pixels
        (into (:cell-wall ((@cell-map :cells) cell-number)) growable-pixels)
        total-cell-pixels
        (into (:pix ((@cell-map :cells) cell-number)) growable-pixels)]
    ;; (println "tcwp" total-cell-wall-pixels)
    (swap! cell-map assoc-in [:cells cell-number :pix] total-cell-pixels)
    (swap! cell-map assoc-in [:cells cell-number :cell-wall] total-cell-wall-pixels)))

(defn growBres
  "second attempt to expand a cell by a single pixel"
  []
  (doseq [cell (@cell-map :cells)]
    (let [cell-number (:number cell)
          growth-color (color (rand-int 255) (rand-int 255) (rand-int 255))]
      (doseq [n (range (:growth-increment cell))]
        (swap! cell-map assoc-in [:cells cell-number :cell-wall] [])
        (let [xc (:x (:center-pix ((@cell-map :cells) cell-number)))
              yc (:y (:center-pix ((@cell-map :cells) cell-number)))
              r (:growth-counter ((@cell-map :cells) cell-number))
              x (atom 0) y (atom r) d (atom (- 3 (* 2 r)))]
          (collectCirclePixels xc yc @x @y cell-number)
          (swap! cell-map update-in [:cells cell-number :growth-counter] inc)
          (while (>= @y @x)
            (swap! x inc)
            (if (> @d 0)
              (do
                (swap! y dec)
                (reset! d (+ (+ @d (* 4 (- @x @y))) 10)))
              (reset! d (+ (+ @d (* 4 @x)) 6)))
            (collectCirclePixels xc yc @x @y cell-number))
          ;; (drawCell ((@cell-map :cells) cell-number) growth-color)
          )))))
