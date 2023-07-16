(ns sketch.cart
  (:require
   [quil.core :refer :all]
   [sketch.shapes :as shape]
   [sketch.calculations :as calc]
   [sketch.path :as path]
   [sketch.cart :as cart]))

(defrecord Cart [id data])
(defrecord Part [type color position width height])

(def all-cart (atom {:carts []
                     :count 0
                     :direction "EAST"
                     :width 0
                     :height 0}))

(def default-cart-data
  "Data to be included with a newly formed cart"
  (hash-map
   :position {:x 0 :y 0}
   :height 20
   :width 20
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
  [part cart-position]
  (let [half-width  (/ 20 2)
        half-height (/ 20 2)
        x1 (- (+ (:x (:position part)) (:x cart-position)) half-width)
        y1 (- (:y (:position part)) half-height)
        y2 (+ (:y (:position part)) half-height)
        x2 (+ (+ (:x (:position part)) (:x cart-position)) half-width)]
    (stroke (:color part) 360 360)
    (line x1 y1 x2 y2)
    (line x1 y2 x2 y1)))

(defn draw-box
  "draws the box type"
  [part cart-position]
  (let [half-width  (/ 20 2)
        half-height (/ 20 2)
        x1 (- (+ (:x (:position part)) (:x cart-position)) half-width)
        y1 (- (:y (:position part)) half-height)
        y2 (+ (:y (:position part)) half-height)
        x2 (+ (+ (:x (:position part)) (:x cart-position)) half-width)]
    (stroke (:color part) 360 360)
    (line x1 y1 x2 y1)
    (line x2 y1 x2 y2)
    (line x2 y2 x1 y2)
    (line x1 y2 x1 y1)))

(defn draw-box-x
  "draws the box type"
  [part cart-position]
  (draw-box part cart-position)
  (draw-x part cart-position))

(defn draw-circle-sm
  "draws the circle-sm type"
  [part cart-position]
  (no-fill)
  (stroke (:color part) 360 360)
  (ellipse (+ (:x (:position part)) (:x cart-position)) 
           (:y (:position part)) 3 3))

(defn draw-dot
  "draws the circle-sm type"
  [part cart-position]
  (fill (:color part) 360 360)
  (stroke (:color part) 360 360) 
  (ellipse (+ (:x (:position part)) (:x cart-position)) 
           (:y (:position part)) 3 3))

(defn draw-circle-lg
  "draws the circle-lg type"
  [part cart-position]
  (no-fill)
  (stroke (:color part) 360 360)
  (ellipse (+ (:x (:position part)) (:x cart-position))
           (:y (:position part)) 
           (:width part) 
           (:height part)))

(defn draw-circle-dot
  "draws the circle-sm type"
  [part cart-position]
  (no-fill)
  (stroke (:color part) 360 360) 
  (ellipse (+ (:x (:position part)) (:x cart-position)) 
           (:y (:position part)) 
           (:width part) 
           (:height part))
  (draw-dot part cart-position))

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
        w (/ (:width (:data cart)) 5)
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
                    (Part. type color {:x x-coord :y (:y (:position (:data cart)))} 10 2)))))

(defn build-cart
  "builds a cart according the random ints given"
  [part-count]
  (let [cart (Cart. -1 default-cart-data)
        cart (assign-cart-id cart)
        rand-speed (rand-int 25)
        rand-y (rand (:height @all-cart))
        cart (assoc-in cart [:data :speed] rand-speed)
        cart (assoc-in cart [:data :position :y] rand-y)]
    (loop [p 0
           c cart]
      (if (<= p part-count)
        (let [rand1 (rand-int 6)
              rand2 (rand-int 5)
              rand3 (rand-int 5)]
          (recur (inc p) (build-cart-part c rand1 rand2 rand3)))
        c))))

(defn cart-generator
  "generates a give number of uniques carts"
  [count]
  (doseq [c (range count)]
    (let [part-count (rand-int 5) 
          cart  (build-cart part-count)] 
      (swap! all-cart assoc-in  [:carts] (conj (:carts @all-cart) cart))
      (swap! all-cart update-in [:count] inc))))

(defn draw-carts
  "renders all carts to the screen"
  []
  (doseq [cart (:carts @all-cart)]
    (doseq [part (:parts (:data cart))]
      (cond
        (= (:type part) "X")          (draw-x part (:position (:data cart)))
        (= (:type part) "BOX")        (draw-box part (:position (:data cart)))
        (= (:type part) "BOX-X")      (draw-box-x part (:position (:data cart)))
        (= (:type part) "CIRCLE-SM")  (draw-circle-sm part (:position (:data cart)))
        (= (:type part) "CIRCLE-LG")  (draw-circle-lg part (:position (:data cart)))
        (= (:type part) "CIRCLE-DOT") (draw-circle-dot part (:position (:data cart)))
        :else (println "failed")))))

(defn move-cart
  "inc a carts coord in the direction specified"
  [cart]
  (cond
     (= (:direction @all-cart) "EAST")  (if (> (:x (:position (:data cart))) (:width @all-cart))
                                          (assoc-in cart [:data :position :x] 0)
                                          (assoc-in cart [:data :position :x] (+ (:x (:position (:data cart))) 5)))
     (= (:direction @all-cart) "WEST")  (if (< (:x (:position (:data cart))) 0)
                                          (assoc-in cart [:data :position :x] (:width @all-cart))
                                          (update-in cart [:data :position :x] dec))
     (= (:direction @all-cart) "SOUTH") (if (> (:y (:position (:data cart))) (:height @all-cart))
                                          (assoc-in cart [:data :position :y] 0)
                                          (update-in cart [:data :position :y] inc))
     (= (:direction @all-cart) "NORTH") (if (< (:y (:position (:data cart))) 0)
                                          (assoc-in cart [:data :position :y] (:height @all-cart))
                                          (update-in cart [:data :position :y] dec))))

(defn update-carts
  "updates the cart's age and toggles to ready"
  [] 
  (swap! all-cart
         assoc-in [:carts] (vec (for [c (:carts @all-cart)]
                                  (if (> (:age (:data c)) (:speed (:data c)))
                                    (move-cart (assoc-in c [:data :age] 0))
                                    (update-in c [:data :age] inc)))))
  (draw-carts))

