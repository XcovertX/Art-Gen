(ns sketch.triangle
  (:require [sketch.shapes :as shape]
            [sketch.calculations :as calc]))

;; ----------- triangle division functions ------------

(defrecord Triangle [id iteration nodes pixels])

(defn determineLongestTriangleSide
  "Determines the longest side of a given triangle using"
  [left-node right-node bottom-node]
  (let
   [lr (calc/distance (:x left-node) (:y left-node) (:x right-node) (:y right-node))
    rb (calc/distance (:x right-node) (:y right-node) (:x bottom-node) (:y bottom-node))
    bl (calc/distance (:x bottom-node) (:y bottom-node) (:x left-node) (:y left-node))]

    (if (> lr rb)
      (if (> lr bl)
        (vector left-node right-node bottom-node "lr")
        (vector bottom-node left-node right-node "bl"))
      (if (> rb bl)
        (vector right-node bottom-node left-node "rb")
        (vector bottom-node left-node right-node "bl")))))

(defn getOppositeCorner
  "finds the opposing corner of a triangle given two other points"
  [side]
  {:x (:x side) :y (:y side)})

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

(defn getLeftNodeId
  "returns the new left-node's id based on new orientation"
  [orientation is-left? ln rn bn nn]
  (if is-left?
    (cond 
      (= orientation "lr") (:node-id ln)
      (= orientation "rb") (:node-id nn)
      (= orientation "bl") (:node-id bn)
      :else -1)
    (cond
      (= orientation "lr") (:node-id nn)
      (= orientation "rb") (:node-id rn)
      (= orientation "bl") (:node-id bn)
      :else -1)))

(defn getRightNodeId
  "returns the new right-node's id based on new orientation"
  [orientation is-left? ln rn bn nn]
  (if is-left?
    (cond
      (= orientation "lr") (:node-id nn)
      (= orientation "rb") (:node-id bn)
      (= orientation "bl") (:node-id ln)
      :else -1)
    (cond
      (= orientation "lr") (:node-id rn)
      (= orientation "rb") (:node-id bn)
      (= orientation "bl") (:node-id nn)
      :else -1)))

(defn getBottomNodeId
  "returns the new bottom-node's id based on new orientation"
  [orientation is-left? ln rn bn nn]
  (if is-left?
    (cond
      (= orientation "lr") (:node-id bn)
      (= orientation "rb") (:node-id ln)
      (= orientation "bl") (:node-id nn)
      :else -1)
    (cond
      (= orientation "lr") (:node-id bn)
      (= orientation "rb") (:node-id nn)
      (= orientation "bl") (:node-id rn)
      :else -1)))

(defn buildTriangle
  "builds a new triangle"
  [triangle-map depth node-a node-b node-c]
  (Triangle.
   (@triangle-map :triangle-count)
   depth
   {:node-a (assoc (shape/buildNode {:x (:x node-a) :y (:y node-a)}) :ID (:node-id node-a))
    :node-b (assoc (shape/buildNode {:x (:x node-b) :y (:y node-b)}) :ID (:node-id node-b))
    :node-c (assoc (shape/buildNode {:x (:x node-c) :y (:y node-c)}) :ID (:node-id node-c))}
   (getTrianglePixels [[(:x node-a) (:y node-a)] [(:x node-b) (:y node-b)] [(:x node-c) (:y node-c)]])))

(defn addTriangle
  "adds a new triangle to traingle-map"
  [triangle-map new-triangle]
  (swap! triangle-map update-in [:triangle-count] inc)
  (swap! triangle-map assoc-in [:triangles] (conj (@triangle-map :triangles) new-triangle)))

(defn divideTriangles
  "Divides a given triangle into two new triangles, split
   along the longest edge"
  [triangle-map tri-map iteration left-node right-node bottom-node] 
  (let [longest-side (determineLongestTriangleSide left-node right-node bottom-node)
        new-node (assoc (calc/calculateMedian longest-side) :node-id (inc (:node-count @triangle-map)))
        opposite-corner (getOppositeCorner (get longest-side 2))
        orientation (last longest-side)
        depth (dec iteration)
        rand1 (calc/calculateRandomInt 1 100)
        rand2 (calc/calculateRandomInt 1 100)]

    (if (>= depth 0)
      (do
        (swap! triangle-map update-in [:node-count] inc)
        (let [left-node-id   (getLeftNodeId   orientation true left-node right-node bottom-node new-node)
              right-node-id  (getRightNodeId  orientation true left-node right-node bottom-node new-node)
              bottom-node-id (getBottomNodeId orientation true left-node right-node bottom-node new-node)]
          (if (or (< rand1 99) > depth 9)
            (divideTriangles triangle-map tri-map depth
                             {:node-id left-node-id   :x (:x (first longest-side)) :y (:y (first longest-side))}
                             {:node-id right-node-id  :x (:x new-node) :y (:y new-node)}
                             {:node-id bottom-node-id :x (:x opposite-corner) :y (:y opposite-corner)})
            (addTriangle
             triangle-map
             (buildTriangle triangle-map depth
                            {:node-id left-node-id   :x (:x (first longest-side)) :y (:y (first longest-side))}
                            {:node-id right-node-id  :x (:x new-node) :y (:y new-node)}
                            {:node-id bottom-node-id :x (:x opposite-corner) :y (:y opposite-corner)}))))
        
        (let [left-node-id   (getLeftNodeId   orientation false left-node right-node bottom-node new-node)
              right-node-id  (getRightNodeId  orientation false left-node right-node bottom-node new-node)
              bottom-node-id (getBottomNodeId orientation false left-node right-node bottom-node new-node)]
          (if (or (< rand2 99) (> depth 9))
            (divideTriangles triangle-map tri-map depth
                             {:node-id left-node-id   :x (:x new-node) :y (:y new-node)}
                             {:node-id right-node-id  :x (:x (second longest-side)) :y (:y (second longest-side))}
                             {:node-id bottom-node-id :x (:x opposite-corner) :y (:y opposite-corner)})
            (addTriangle
             triangle-map
             (buildTriangle triangle-map depth
                            {:node-id left-node-id   :x (:x new-node) :y (:y new-node)}
                            {:node-id right-node-id  :x (:x (second longest-side)) :y (:y (second longest-side))}
                            {:node-id bottom-node-id :x (:x opposite-corner) :y (:y opposite-corner)})))))
      (addTriangle
       triangle-map
       (buildTriangle triangle-map depth
                      {:node-id (:node-id left-node)   :x (:x left-node)   :y (:y left-node)}
                      {:node-id (:node-id right-node)  :x (:x right-node)  :y (:y right-node)}
                      {:node-id (:node-id bottom-node) :x (:x bottom-node) :y (:y bottom-node)})))))

(defn insertNode
  []
  );;you havve to insert individual nodes

(defn buildTriangles
  "Recursively builds triangles to a given iteration"
  [data]
  (when (:area-is-rectangle? data) 
    ;; (swap! (:triangle-map data) assoc-in [(str (:node-count @(:triangle-map data))) :nodes] )
    (swap! (:triangle-map data) update-in [:node-count] inc)
    (divideTriangles
     (:triangle-map data)
     {}
     (:depth data)
     {:node-id 0 :x (:x-max data) :y (:y-max data)}
     {:node-id 1 :x (:x-min data) :y (:y-min data)}
     {:node-id 2 :x (:x-max data) :y (:y-min data)})
    (swap! (:triangle-map data) update-in [:node-count] inc)
    (divideTriangles
     (:triangle-map data)
     {}
     (:depth data)
     {:node-id 1 :x (:x-min data) :y (:y-min data)}
     {:node-id 0 :x (:x-max data) :y (:y-max data)} 
     {:node-id (:node-count @(:triangle-map data)) :x (:x-min data) :y (:y-max data)}))
  (when (:area-is-triangle? data)
    (swap! (:triangle-map data) assoc-in [:node-count] 2)
    (divideTriangles
     (:triangle-map data)
     {}
     (:depth data)
     {:node-id 0 :x (:x1 data) :y (:y1 data)}
     {:node-id 1 :x (:x2 data) :y (:y2 data)}
     {:node-id 2 :x (:x3 data) :y (:y3 data)}))
  (doseq [tri (:triangles @(:triangle-map data))]
    
    (let [pixels (:pixels tri)
          nodes (:nodes tri)
          triangle-center (calc/calculateTriangleCenter nodes)

          distance-to-center (calc/calculateDistanceFromCenter triangle-center) 
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

      (shape/fillShape pixels aver-r aver-g aver-b)))
      @(:triangle-map data))