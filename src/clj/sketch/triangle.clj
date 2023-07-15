(ns sketch.triangle
  (:require [sketch.shapes :as shape]
            [sketch.calculations :as calc]))

;; ----------- triangle division functions ------------

(defrecord Triangle [id iteration nodes pixels])

(defn insert-node
  "inserts a node into the triangle map if the node does not already exist in the collection"
  [triangle-map node]
  (let [node-id (:ID node)]
    (when (not (contains? (mapv (fn [x] (:ID x)) (:nodes @triangle-map)) node-id))
      (swap! triangle-map assoc-in [:nodes (keyword (str (:node-count @triangle-map)))] node)
      (swap! triangle-map update-in [:node-count] inc))
    (get (:nodes @triangle-map) node-id)))

(defn build-triangle-node
  "builds a node with an id that matches the triangle-map pattern"
  [x y id]
  (assoc-in (shape/build-node {:x x :y y}) [:ID] (keyword (str id))))

(defn determine-longest-triangle-side
  "Determines the longest side of a given triangle using"
  [left-node right-node bottom-node]
  (let
   [lr (calc/distance (:x (:position left-node))   (:y (:position left-node))
                      (:x (:position right-node))  (:y (:position right-node)))
    rb (calc/distance (:x (:position right-node))  (:y (:position right-node))
                      (:x (:position bottom-node)) (:y (:position bottom-node)))
    bl (calc/distance (:x (:position bottom-node)) (:y (:position bottom-node))
                      (:x (:position left-node))   (:y (:position left-node)))]

    (if (> lr rb)
      (if (> lr bl)
        (vector left-node right-node bottom-node "lr")
        (vector bottom-node left-node right-node "bl"))
      (if (> rb bl)
        (vector right-node bottom-node left-node "rb")
        (vector bottom-node left-node right-node "bl")))))

(defn get-opposite-corner
  "finds the opposing corner of a triangle given two other points"
  [side]
  {:x (:x side) :y (:y side)})

(defn get-triangle-pixels
  "retrieves all of the pixels contained within a given triangle"
  [vertices]
  (let [[xmin ymin xmax ymax] (calc/bbox vertices)]
    (filter identity
            (doall
             (map
              (fn [[x y :as p]]
                (let [bc (calc/barycentric vertices p)]
                  (when (calc/visible? bc)
                    (conj {:x x :y y}))))
              (for [y (range ymin ymax)
                    x (range xmin xmax)]
                [x y]))))))

(defn get-left-node-id
  "returns the new left-node's id based on new orientation"
  [orientation is-left? ln rn bn nn]
  (if is-left?
    (cond
      (= orientation "lr") (:ID ln)
      (= orientation "rb") (:ID nn)
      (= orientation "bl") (:ID bn)
      :else -1)
    (cond
      (= orientation "lr") (:ID nn)
      (= orientation "rb") (:ID rn)
      (= orientation "bl") (:ID bn)
      :else -1)))

(defn get-right-node-id
  "returns the new right-node's id based on new orientation"
  [orientation is-left? ln rn bn nn]
  (if is-left?
    (cond
      (= orientation "lr") (:ID nn)
      (= orientation "rb") (:ID bn)
      (= orientation "bl") (:ID ln)
      :else -1)
    (cond
      (= orientation "lr") (:ID rn)
      (= orientation "rb") (:ID bn)
      (= orientation "bl") (:ID nn)
      :else -1)))

(defn get-bottom-node-id
  "returns the new bottom-node's id based on new orientation"
  [orientation is-left? ln rn bn nn]
  (if is-left?
    (cond
      (= orientation "lr") (:ID bn)
      (= orientation "rb") (:ID ln)
      (= orientation "bl") (:ID nn)
      :else -1)
    (cond
      (= orientation "lr") (:ID bn)
      (= orientation "rb") (:ID nn)
      (= orientation "bl") (:ID rn)
      :else -1)))

(defn build-triangle
  "builds a new triangle"
  [triangle-map depth node-a node-b node-c]
  (Triangle.
   (@triangle-map :triangle-count)
   depth
   {:node-a (:ID node-a) :node-b (:ID node-b) :node-c (:ID node-c)}
   (get-triangle-pixels [[(:x (:position node-a)) (:y (:position node-a))]
                         [(:x (:position node-b)) (:y (:position node-b))]
                         [(:x (:position node-c)) (:y (:position node-c))]])))

(defn add-triangle
  "adds a new triangle to traingle-map"
  [triangle-map new-triangle]
  (swap! triangle-map update-in [:triangle-count] inc)
  (swap! triangle-map assoc-in [:triangles] (conj (@triangle-map :triangles) new-triangle)))

(defn divide-triangles
  "Divides a given triangle into two new triangles, split
   along the longest edge"
  [triangle-map iteration left-node right-node bottom-node]
  (let [depth (dec iteration)]
    (if (>= depth 0)

      (let [median (calc/calculateMedian [left-node right-node])
            new-node (insert-node triangle-map (build-triangle-node (:x median) (:y median) (:node-count @triangle-map)))
            rand1 (calc/calculateRandomInt 1 100)
            rand2 (calc/calculateRandomInt 1 100)]

        (let [longest-side (determine-longest-triangle-side left-node new-node bottom-node)
              orientation (last longest-side)
              left-node-id   (get-left-node-id   orientation true left-node right-node bottom-node new-node)
              right-node-id  (get-right-node-id  orientation true left-node right-node bottom-node new-node)
              bottom-node-id (get-bottom-node-id orientation true left-node right-node bottom-node new-node)
              left-node      (left-node-id   (:nodes @triangle-map))
              right-node     (right-node-id  (:nodes @triangle-map))
              bottom-node    (bottom-node-id (:nodes @triangle-map))]
          (if (or (< rand1 111) (> depth 14))
            (divide-triangles triangle-map depth left-node right-node bottom-node)
            (let [new-triangle (build-triangle triangle-map depth left-node right-node bottom-node)]
              (add-triangle triangle-map new-triangle)
              new-triangle)))

        (let [longest-side (determine-longest-triangle-side new-node right-node bottom-node)
              orientation (last longest-side)
              left-node-id   (get-left-node-id   orientation false left-node right-node bottom-node new-node)
              right-node-id  (get-right-node-id  orientation false left-node right-node bottom-node new-node)
              bottom-node-id (get-bottom-node-id orientation false left-node right-node bottom-node new-node)
              left-node      (left-node-id   (:nodes @triangle-map))
              right-node     (right-node-id  (:nodes @triangle-map))
              bottom-node    (bottom-node-id (:nodes @triangle-map))]
          (if (or (< rand2 111) (> depth 14))
            (divide-triangles triangle-map depth left-node right-node bottom-node)
            (let [new-triangle (build-triangle triangle-map depth left-node right-node bottom-node)]
              (add-triangle triangle-map new-triangle)
              new-triangle))))
      (let [new-triangle (build-triangle triangle-map depth left-node right-node bottom-node)]
        (add-triangle triangle-map new-triangle)
        new-triangle))))

(defn get-neighboring-verticies
  "returns a hash-map containing the prev and next verticies"
  [index vector]
  (let [prev (if (= index 0)
               (last vector)
               (get vector (- index 1)))
        next (if (= index (- (count vector) 1))
               (first vector)
               (get vector (+ index 1)))]
    {:prev prev :next next}))

(defn area-contains-no-other-vertex?
  "returns true if the any any of the given verticies is found within the area of the given triangle"
  [triangle verticies]
  (some false? (let [a (:a triangle)
                     b (:b triangle)
                     c (:c triangle)]
                 (for [v verticies
                       :let [x (:x v)
                             y (:y v)
                             avx [0 (- (:x b) (:x a)) (- x (:x a))]
                             avy [0 (- (:y b) (:y a)) (- y (:y a))]
                             bvx [0 (- (:x c) (:x b)) (- x (:x b))]
                             bvy [0 (- (:y c) (:y b)) (- y (:y b))]
                             cvx [0 (- (:x a) (:x c)) (- x (:x c))]
                             cvy [0 (- (:y a) (:y c)) (- y (:y c))]]]
                   (if (and (<= (first (calc/cross avx avy)) 0)
                            (<= (first (calc/cross bvx bvy)) 0)
                            (<= (first (calc/cross cvx cvy)) 0))
                     true
                     false)))))

(defn angle-is-convex?
  "returns true if a given vertex is less than 180 degrees"
  [vertex]
  (let [a (:a vertex)
        b (:b vertex)
        c (:c vertex)]
    (neg? (first (calc/cross [0 (- (:x a) (:x b)) (- (:x c) (:x b))]
                             [0 (- (:y a) (:y b)) (- (:y c) (:y b))])))))

(defn triangulate-polygon
  "builds a collection of triangles from a given polygon"
  [p]
  (let [triangles (atom {:verticies []})
        polygon (atom {:verticies p})
        i (atom 0)]
    (loop []
      (if (> (count (:verticies @polygon)) 3)
        (do
          (loop [index 0]
            (swap! i inc)
            (let [point (get (:verticies @polygon) index)
                  neighbors (get-neighboring-verticies index (:verticies @polygon))
                  prev (:prev neighbors)
                  next (:next neighbors)
                  convex (angle-is-convex? {:a prev :b point :c next})
                  diagonal (area-contains-no-other-vertex? {:a prev :b point :c next} (:verticies @polygon))]

              (if (and convex diagonal)
                (do
                  (swap! triangles assoc-in [:verticies] (conj (:verticies @triangles) {:a prev :b point :c next}))
                  (swap! polygon assoc-in [:verticies] (into []
                                                             (remove nil?
                                                                     (mapv
                                                                      (fn [x] (when (not= index (first x))
                                                                                (second x)))
                                                                      (map-indexed vector
                                                                                   (:verticies @polygon)))))))

                (when (< index (- (count (:verticies @polygon)) 1))
                  (recur (+ index 1))))))
          (when (< @i 1000)
            (recur)))

        (let [a (get (:verticies @polygon) 0)
              b (get (:verticies @polygon) 1)
              c (get (:verticies @polygon) 2)]
          (swap! triangles assoc-in [:verticies] (conj (:verticies @triangles)
                                                       {:a a :b b :c c}))
          (swap! polygon assoc-in [:verticies] []))))

    (:verticies @triangles)))

(defn draw-triangle-map-average
  [data]
  (doseq [tri (:triangles @(:triangle-map data))]

    (let [pixels (:pixels tri)
          node-keys (:nodes tri)
          a ((:node-a node-keys) (:nodes @(:triangle-map data)))
          b ((:node-b node-keys) (:nodes @(:triangle-map data)))
          c ((:node-c node-keys) (:nodes @(:triangle-map data)))
          nodes {:node-a a :node-b b :node-c c}
          triangle-center (calc/calculateTriangleCenter nodes)

          distance-to-center (calc/calculateDistanceFromCenter triangle-center)
          average? false
          ;; (cond
          ;;   (>= distance-to-center 300) (if (< (calc/calculateRandomInt 100) 4)
          ;;                                 true
          ;;                                 false)
          ;;   (>= distance-to-center 150) (if (< (calc/calculateRandomInt 100) 3)
          ;;                                 true
          ;;                                 false)
          ;;   (>= distance-to-center 75) (if (< (calc/calculateRandomInt 100) 2)
          ;;                                true
          ;;                                false)
          ;;   (>= distance-to-center 30) (if (< (calc/calculateRandomInt 100) 1)
          ;;                                true
          ;;                                false)
          ;;   :else false)

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

(defn buildTriangles
  "Recursively builds triangles to a given iteration"
  [data]

  (if (>= (count (:area data)) 3)
    (let [triangles (triangulate-polygon (:area data))]

      (doseq [t triangles]
        (divide-triangles
         (:triangle-map data)
         (:depth data)
         (insert-node (:triangle-map data)
                      (build-triangle-node
                       (:x (:a t)) (:y (:a t)) (:node-count @(:triangle-map data))))
         (insert-node (:triangle-map data)
                      (build-triangle-node
                       (:x (:b t)) (:y (:b t)) (:node-count @(:triangle-map data))))
         (insert-node (:triangle-map data)
                      (build-triangle-node
                       (:x (:c t)) (:y (:c t)) (:node-count @(:triangle-map data)))))))
    (println "The area provided is does not contain 3 or more points"))
  data)