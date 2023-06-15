(ns sketch.hitomezashi
  (:require [quil.core :refer [line random]]
            [sketch.divider :as div]) 
  (:refer [clojure.pprint]))

(def vowels #{\a \e \i \o \u})
(def consonants #{\b \c \d \f \g \h \j \k \l \m \n \p \q \r \s \t \v \w \x \y \z})

(defn draw-stitch-segment
  "draws alternating stitch"
  [segment first xDif yDif]
  (loop [i 0
         alternate first]
    (when (< (+ i 1) (count segment))
      (let [from (get segment i)
            to (get segment (+ i 1))
            x1 (+ (:x from) xDif)
            y1 (+ (:y from) yDif)
            x2 (+ (:x to) xDif)
            y2 (+ (:y to) yDif)]
        (when (= alternate true)
          (line x1 y1 x2 y2))
        (recur (inc i) (not alternate))))))

(defn buildXStitchSegments
  "constructs a collection of coordinates
   that consist of an entire stitch length
   for each xIntersection"
  [intersections xCount stitchSize]
  
   (filterv not-empty
            (loop [i 0
                   result []]
              (if (>= i xCount)
                result
                (recur
                 (inc i)
                 (conj result
                       (filterv (fn [x] (not= (:x x) nil))
                                (mapv
                                 (fn [[x y]] (conj {:x x :y y}))
                                 (for [coord intersections]
                                   (when (= (:x coord) (* i stitchSize))
                                     [(:x coord) (:y coord)]))))))))))

(defn buildYStitchSegments
  "constructs a collection of coordinates
   that consist of an entire stitch length
   for each xIntersection"
    [intersections yCount stitchSize]
  
   (filterv not-empty
            (loop [i 0
                   result []]
              (if (>= i yCount)
                result
                (recur
                 (inc i)
                 (conj result
                       (filterv (fn [y] (not= (:y y) nil))
                                (mapv
                                 (fn [[x y]] (conj {:x x :y y}))
                                 (for [coord intersections]
                                   (when (= (:y coord) (* i stitchSize))
                                     [(:x coord) (:y coord)]))))))))))
(defn boolify-it
  "converts a given input into a boolean representation"
  [input]
  (let [rand (random 100)
        axis (when (string? input)
               (if (< rand 50)
                 (filterv some? (map #(when (vowels %1) %2) input (range)))
                 (filterv some? (map #(when (consonants %1) %2) input (range)))))]
    (loop [i 0
           result []]
      (if (>= i (count input))
        result
        (recur
         (inc i)
         (conj result (if (some #(= i %) axis)
                        true
                        false)))))))

(defn hito-stitch
  [xAxisInput yAxisInput stitchSize square]
  (let [[x1 y1 x2 y2] square
        xStitchCount (+ (int (/ (- (- x2 x1) 1) stitchSize)) 1)
        yStitchCount (+ (int (/ (- (- y2 y1) 1) stitchSize)) 1)
        xDif (/ (mod (- x2 x1) stitchSize) 2)
        yDif (/ (mod (- y2 y1) stitchSize) 2)
        intersections (div/divideGrid stitchSize x1 y1 x2 y2)
        xSegments (buildXStitchSegments intersections xStitchCount stitchSize)
        ySegments (buildYStitchSegments intersections yStitchCount stitchSize)
        xAxisBools (boolify-it xAxisInput)
        yAxisBools (boolify-it yAxisInput)]

    (loop [i 0]
      (when (< i xStitchCount)
        (draw-stitch-segment (get xSegments i) (get xAxisBools i) xDif yDif)
        (recur (inc i))))

    (loop [i 0]
      (when (< i yStitchCount)
        (draw-stitch-segment (get ySegments i) (get yAxisBools i) xDif yDif)
        (recur (inc i))))))