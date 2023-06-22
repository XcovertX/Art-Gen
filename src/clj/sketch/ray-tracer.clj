(ns sketch.ray-tracer  
  (:require [quil.core :refer :all]
            [sketch.calculations :as calc]) 
  (:use [clojure.pprint]))

(def TAU (* 2 Math/PI))

(defn getRayCast
  "builds an individual ray"
  [point-a point-b point-c point-d]
  
  (let [s1 {:x (- (:x point-b) (:x point-a))
            :y (- (:y point-b) (:y point-a))}
        s2 {:x (- (:x point-d) (:x point-c))
            :y (- (:y point-d) (:y point-c))}]
    ;; (println s1 s2)
(let        [s (/ (+ (* (- (:y s1))
                   (- (:x point-a) (:x point-c)))
                (* (:x s1)
                   (- (:y point-a) (:y point-c))))
             (+ (* (- (:x s2))
                   (:y s1))
                (* (:x s1)
                   (:y s2))))
        t (/ (- (* (:x s2)
                   (- (:y point-a) (:y point-c)))
                (* (:y s2)
                   (- (:x point-a) (:x point-c))))
             (+ (* (- (:x s2))
                   (:y s1))
                (* (:x s1)
                   (:y s2))))]
    (if (and (>= s 0)
             (<= s 1)
             (>= t 0)
             (<= t 1))
      (let [x (+ (:x point-a) (* t (:x s1)))
            y (+ (:y point-a) (* t (:y s1)))] 
        (dist (:x point-a) (:y point-a) x y))
      -1)
    )))

(defn trace
  "Draw a line from x1,y1 to x2,y2 using Bresenham's, to a java BufferedImage in the colour of pixel."
  [point-a point-b color]
  (let [x1 (:x point-a)
        y1 (:y point-a)
        x2 (:x point-b)
        y2 (:y point-b) 
        dist-x (Math/abs (- x1 x2))
        dist-y (Math/abs (- y1 y2))
        steep (> dist-y dist-x)
        [x1 y1 x2 y2] (if steep [y1 x1 y2 x2] [x1 y1 x2 y2])
        [x1 y1 x2 y2] (if (> x1 x2) [x2 y2 x1 y1] [x1 y1 x2 y2])
        delta-x (- x2 x1)
        delta-y (Math/abs (- y1 y2))
        y-step (if (< y1 y2) 1 -1)]
    (println x1 y1 x2 y2)
    (loop [x x1 y y1 error (Math/floor (/ delta-x 2))]
    ;;   (println x x1 y y1 error)
      (if (< x x2)
        (if (< error delta-y)
          (recur (inc x) (+ y y-step) (+ error (- delta-x delta-y)))
          (recur (inc x) y            (- error delta-y)))
        {:x x :y y}))))

(defn getRays
  "returns a map of rays from a given radius and resolution (ray count)"
  [point-a resolution max-distance]
  (vec 
   (flatten
    (for [i (range resolution)]
      (let [lines [{:point-a {:x 50 :y 150} :point-b {:x 200 :y 400}}
                   {:point-a {:x 150 :y 150} :point-b {:x 100 :y 100}}
                   {:point-a {:x 50 :y 450} :point-b {:x 300 :y 453}}
                   {:point-a {:x 50 :y 350} :point-b {:x 400 :y 400}}]
            direction (* TAU (/ i resolution))
            point-b {:x (+ (:x point-a) (* (Math/cos direction) max-distance))
                     :y (+ (:y point-a) (* (Math/sin direction) max-distance))}
            min-distance (first
                          (sort
                           (for [line lines]
                             (let [distance (getRayCast point-a point-b (:point-a line) (:point-b line))
                                   min-distance (if (and (< distance max-distance)
                                                         (> distance 0))
                                                  distance
                                                  max-distance)]
                               min-distance))))]
        {:point-a point-a :point-b {:x (+ (:x point-a) (* (Math/cos direction) min-distance))
                                    :y (+ (:y point-a) (* (Math/sin direction) min-distance))}})))))