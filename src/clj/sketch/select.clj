(ns sketch.select
  (:require [quil.core :refer :all]
            [clojure.core.async :as async])
  )

(def select-shapes (atom {:polygon-select-complete false
                          :polygon-select []}))
(def select-busy (atom false))

(defn add-ellipse-to-polygon-select
  "adds as ellipse to be drawn for the polygon selection tool"
  [point]
  (swap! select-shapes assoc-in [:polygon-select] (conj
                                                   (:polygon-select @select-shapes)
                                                   {:type "ellipse"
                                                    :x (:x point)
                                                    :y (:y point)
                                                    :w 3
                                                    :h 3})))

(defn add-line-to-polygon-select
  "adds a point that a line will be drawn to. 
   the originating point is center of the previous shape in the collection"
  [point-a point-b]
  (swap! select-shapes assoc-in [:polygon-select] (conj
                                                   (:polygon-select @select-shapes)
                                                   {:type "line"
                                                    :x1 (:x point-a)
                                                    :y1 (:y point-a)
                                                    :x2 (:x point-b)
                                                    :y2 (:y point-b)
                                                    :stroke-weight 2})))

(defn mark-polygon-select-polygon-complete
  []
  (swap! select-shapes assoc-in [:polygon-select-complete] true))

(defn clear-select
  "clears the select tool"
  []
  (swap! select-shapes assoc-in [:polygon-select] [])
  (swap! select-shapes assoc-in [:polygon-select-complete] false))

(defn select-point
  "user selects a point to be the next vertex"
  [prev-point]
  (loop []
    (if (not (mouse-pressed?))
      (let [x (mouse-x)
            y (mouse-y)
            point {:x x :y y}]
        (add-ellipse-to-polygon-select prev-point)
        (add-line-to-polygon-select prev-point point)
        (add-ellipse-to-polygon-select point)
        point)
      (recur))))

(defn select-polygon
  "user selects n number of verticies to form a polygon"
  []
  (async/thread
    (loop [t [{:x (mouse-x) :y (mouse-y)}]]
      (if (mouse-pressed?)
        (recur (conj t (select-point (last t))))
        (if (key-pressed?)
          (do
            (mark-polygon-select-polygon-complete)
            t)
          (recur t))))))