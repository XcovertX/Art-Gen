(ns sketch.cart
  (:require
   [quil.core :refer :all]
   [sketch.shapes :as shape]
   [sketch.calculations :as calc]
   [sketch.path :as path]))

(defrecord Cart [id data])
(defrecord Part [type color position width height])

(def all-cart (atom {:carts []
                  :count 0
                  :direction "RIGHT"
                  :height 0}))
(def col-a 0)

(def default-cart-data
  "Data to be included with a newly formed cart"
  (hash-map
   :position (path/Position2D 0 0)
   :height height
   :width 10
   :age 0
   :is-ready false
   :parts []
   :speed 0))

(defn inc-cart-age
  "increments a given node's age"
  [cart]
  (update-in cart [:data :age] inc))

(defn assign-cart-id
  "gives unique id to cart"
  [cart] 
  (assoc-in cart [:id] (:count @all-cart)))

(defn draw-x
  "draws the x type"
  [part]
  (let [half-width  (/ (:x (:position part)) 2)
        half-height (/ (:y (:position part)) 2)
        x1 (- (:x (:position part)) half-width)
        y1 (- (:y (:position part)) half-height)
        y2 (+ (:y (:position part)) half-height)
        x2 (+ (:x (:position part)) half-width)]
    (stroke (:color part) 360 360)
    (line x1 y1 x2 y2)
    (line x1 y2 x2 y1)))

(defn draw-box
  "draws the box type"
  [part]
  (let [half-width  (/ (:x (:position part)) 2)
        half-height (/ (:y (:position part)) 2)
        x1 (- (:x (:position part)) half-width)
        y1 (- (:y (:position part)) half-height)
        y2 (+ (:y (:position part)) half-height)
        x2 (+ (:x (:position part)) half-width)]
    (stroke (:color part) 360 360)
    (line x1 y1 x2 y1)
    (line x2 y1 x2 y2)
    (line x2 y2 x1 y2)
    (line x1 y2 x1 y1)))

(defn draw-box-x
  "draws the box type"
  [part]
  (draw-box part)
  (draw-x part))

(defn draw-circle-sm
  "draws the circle-sm type"
  [part]
  (no-fill)
  (stroke (:color part) 360 360)
  (ellipse (:x (:position part)) (:y (:position part)) 3 3))

(defn draw-dot
  "draws the circle-sm type"
  [part]
  (fill (:color part) 360 360)
  (stroke (:color part) 360 360)
  (ellipse (:x (:position part)) (:y (:position part)) 3 3))

(defn draw-circle-lg
  "draws the circle-lg type"
  [part]
  (no-fill)
  (stroke (:color part) 360 360)
  (ellipse (:x (:position part)) (:y (:position part)) (:width part) (:height part)))

(defn draw-circle-dot
  "draws the circle-sm type"
  [part]
  (no-fill)
  (stroke (:color part) 360 360)
  (ellipse (:x (:position part)) (:y (:position part)) (:width part) (:height part))
  (draw-dot part))

(defn build-cart-part
  "builds a cart according the random ints given"
  [cart a b c]
  (let [type (cond
               (= a 0) "X"
               (= a 1) "BOX"
               (= a 2) "CIRCLE-SM"
               (= a 3) "CIRCLE-LG"
               (= a 4) "CIRCLE-DOT"
               (= a 5) "BOX-X"
               :else   "DOT")
        color (cond
                (= b 0) 0
                (= b 1) 33
                (= b 2) 102
                (= b 3) 200
                (= b 4) 340
                :else 360)
        w (/ width 5)
        x (:x (:position (:data cart)))
        x-coord (cond
                  (= c 0) (- x (* 2 w))
                  (= c 1) (- x w)
                  (= c 2) x
                  (= c 3) (+ x w)
                  (= c 4) (+ x (* 2 w))
                  :else 360)]
    (assoc-in cart [:data :parts]
              (conj (:parts (:data cart))
                    (Part. type color x-coord (:height (:data cart)) (:height (:data cart)))))))

(defn build-cart
  "builds a cart according the random ints given"
  [r1 r2 r3 r4]
  (let [cart (Cart. -1 default-cart-data)
        cart (assign-cart-id cart)]
    (loop [p 0
           c cart]
      (if (< p r4)
        (recur (p inc) (build-cart-part c r1 r2 r3))
        c))))

(defn cart-generator
  "generates a give number of uniques carts"
  [count]
  (doseq [c (range count)]
    (let [rand1 (rand-int 6)
          rand2 (rand-int 5)
          rand3 (rand-int 5)
          rand4 (rand-int 5)
          cart  (build-cart rand1 rand2 rand3 rand4)]
      (swap! all-cart assoc-in  [:carts] (conj (:carts @all-cart) cart))
      (swap! all-cart update-in [:count] inc))))

(defn draw-carts
  "renders all carts to the screen"
  []
  (doseq [cart (:cart @all-cart)]
    (cond
      (= (:type cart) "X")          (draw-x cart)
      (= (:type cart) "BOX")        (draw-box cart)
      (= (:type cart) "BOX-X")      (draw-box-x cart)
      (= (:type cart) "CIRCLE-SM")  (draw-circle-sm cart)
      (= (:type cart) "CIRCLE-LG")  (draw-circle-lg cart)
      (= (:type cart) "CIRCLE-DOT") (draw-circle-dot cart))))

