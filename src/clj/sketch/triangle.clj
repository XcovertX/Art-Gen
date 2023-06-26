(ns triangle
  (:require [sketch.shapes :as shape]
            [sketch.calculations :as calc]))

;; ----------- triangle division functions ------------

(defrecord Triangle [number iteration node-a node-b node-c pix])

(defn determineLongestTriangleSide
  "Determines the longest side of a given triangle using"
  [x1 y1 x2 y2 x3 y3]
  (let
   [a (calc/distance x1 y1 x2 y2)
    b (calc/distance x2 y2 x3 y3)
    c (calc/distance x3 y3 x1 y1)]

    (if (> a b)
      (if (> a c)
        (vector x1 y1 x2 y2 x3 y3)
        (vector x3 y3 x1 y1 x2 y2))
      (if (> b c)
        (vector x2 y2 x3 y3 x1 y1)
        (vector x3 y3 x1 y1 x2 y2)))))

(defn getOppositeCorner
  "finds the opposing corner of a triangle given two other points"
  [[x1 y1 x2 y2 x3 y3]]
  (vector x3 y3))

(defn getTrianglePixels
  "retrieves all of the pixels contained within a given triangle"
  [vertices]
  (let [[xmin ymin xmax ymax] (calc/bbox vertices)]
    (filter identity
            (doall
             (map
              (fn [[x y :as p]]
                (let [bc (calc/barycentric vertices p)]
                  (if (calc/visible? bc)
                    (conj {:x x :y y}))))
              (for [y (range ymin ymax)
                    x (range xmin xmax)]
                [x y]))))))

(defn buildTriangle
  "builds a new triangle"
  [triangle-map depth x1 y1 x2 y2 x3 y3]
  (Triangle.
   (@triangle-map :triangle-count)
   depth
   (shape/buildNode {:x x1 :y y1})
   (shape/buildNode {:x x2 :y y2})
   (shape/buildNode {:x x3 :y y3})
   (getTrianglePixels [[x1 y1] [x2 y2] [x3 y3]])))

(defn addTriangle
  "adds a new triangle to traingle-map"
  [triangle-map new-triangle]
  (swap! triangle-map update-in [:triangle-count] inc)
  (swap! triangle-map assoc-in [:triangles] (conj (@triangle-map :triangles) new-triangle)))

(defn divideTriangles
  "Divides a given triangle into two new triangles, split
   along the longest edge"
  [triangle-map tri-map iteration x1 y1 x2 y2 x3 y3]
  (let [longest-side (determineLongestTriangleSide x1 y1 x2 y2 x3 y3)
        median (calc/calculateMedian longest-side)
        opposite-corner (getOppositeCorner longest-side)
        depth (dec iteration)
        rand1 (calc/calculateRandomInt 1 100)
        rand2 (calc/calculateRandomInt 1 100)]

    (if (>= depth 0)
      (do
        (if (or (< rand1 99) > depth 9)
          (do
            (divideTriangles triangle-map tri-map depth
                             (get median 0)          (get median 1)
                             (get longest-side 0)    (get longest-side 1)
                             (get opposite-corner 0) (get opposite-corner 1))
            ;; (line (get median 0) (get median 1) (get opposite-corner 0) (get opposite-corner 1))
            )
          (do
            (addTriangle
             triangle-map
             (buildTriangle triangle-map depth x1 y1 x2 y2 x3 y3))))
        (if (or (< rand2 99) (> depth 9))
          (do
            (divideTriangles triangle-map tri-map depth
                             (get median 0)          (get median 1)
                             (get longest-side 2)    (get longest-side 3)
                             (get opposite-corner 0) (get opposite-corner 1))
            ;; (line (get median 0) (get median 1) (get opposite-corner 0) (get opposite-corner 1))
            )
          (do
            (addTriangle
             triangle-map
             (buildTriangle triangle-map depth x1 y1 x2 y2 x3 y3)))))
      (do
        (addTriangle
         triangle-map
         (buildTriangle triangle-map depth x1 y1 x2 y2 x3 y3))))))

(defn buildTriangles
  "Recursively builds triangles to a given iteration"
  [data]
  (when (:area-is-rectangle? data)
    (do
      (divideTriangles
       (:triangle-map data)
       {}
       (:depth data)
       (:x-min data)
       (:y-min data)
       (:x-max data)
       (:y-min data)
       (:x-max data)
       (:y-max data))
      (divideTriangles
       (:triangle-map data)
       {}
       (:depth data)
       (:x-min data)
       (:y-min data)
       (:x-max data)
       (:y-max data)
       (:x-min data)
       (:y-max data))))
  (when (:area-is-triangle? data)
    (do
      (divideTriangles
       (:triangle-map data)
       {}
       (:depth data)
       (:x1 data)
       (:y1 data)
       (:x2 data)
       (:y2 data)
       (:x3 data)
       (:y3 data))))
  (doseq [tri (:triangles @(:triangle-map data))]
    (let [pixels (:pix tri)
          triangle-center (calc/calculateTriangleCenter [(:x (:position (:node-a tri)))
                                                         (:y (:position (:node-a tri)))
                                                         (:x (:position (:node-b tri)))
                                                         (:y (:position (:node-b tri)))
                                                         (:x (:position (:node-c tri)))
                                                         (:y (:position (:node-c tri)))])

          distance-to-center (calc/calculateDistanceFromCenter triangle-center)
          rand (calc/calculateRandomInt 100)
          average? (cond
                     (>= distance-to-center 600) (if (< (calc/calculateRandomInt 100) 50)
                                                   true
                                                   false)
                     (>= distance-to-center 500) (if (< (calc/calculateRandomInt 100) 32)
                                                   true
                                                   false)
                     (>= distance-to-center 400) (if (< (calc/calculateRandomInt 100) 16)
                                                   true
                                                   false)
                     (>= distance-to-center 200) (if (< (calc/calculateRandomInt 100) 8)
                                                   true
                                                   false)
                     :else false)

          aver-r (if (= average? false)
                   (shape/calculateAverageColor pixels :r)
                   (- (shape/calculateAverageColor pixels :r) 30))
          aver-g (if (= average? false)
                   (shape/calculateAverageColor pixels :g)
                   (- (shape/calculateAverageColor pixels :g) 30))
          aver-b (if (= average? false)
                   (shape/calculateAverageColor pixels :b)
                   (- (shape/calculateAverageColor pixels :b) 30))]
      (shape/fillShape pixels aver-r aver-g aver-b))))