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
                     :height 0
                     :part-width 0
                     :part-height 0
                     :color 0}))

(def default-cart-data
  "Data to be included with a newly formed cart"
  (hash-map
   :position {:x 0 :y 0}
   :height 20
   :width 20
   :age 0
   :is-ready false
   :is-transitioning false
   :transition-count 0
   :parts []
   :speed 0
   :direction "EAST"))

(defn inc-color
  []
  (swap! all-cart assoc-in [:color] (if (>= (:color @all-cart) 360)
                                      0
                                      (inc (:color @all-cart)))))

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
  (let [half-width  (* (:width part) 1/2)
        half-height (* (:height part) 1/2)
        x1 (- (+ (:x (:position part)) (:x cart-position)) half-width)
        y1 (- (:y cart-position) half-height)
        y2 (+ (:y cart-position) half-height)
        x2 (+ (+ (:x (:position part)) (:x cart-position)) half-width)]
    (stroke (:color part) 360 360)
    (line x1 y1 x2 y2)
    (line x1 y2 x2 y1)))

(defn draw-cross
  "draws the cross type"
  [part cart-position]
  (let [half-width  (* (:width part) 1/2)
        half-height (* (:height part) 1/2)
        x1 (- (+ (:x (:position part)) (:x cart-position)) half-width)
        y1 (- (:y cart-position) half-height)
        y2 (+ (:y cart-position) half-height)
        x2 (+ (+ (:x (:position part)) (:x cart-position)) half-width)
        x-half (+ x1 half-height)
        y-half (+ y1 half-width)]
    (stroke (:color part) 360 360)
    (line x-half y1 x-half y2)
    (line x1 y-half x2 y-half)))

(defn draw-triple-line-horizontal
  "draws three horizontal lines type"
  [part cart-position]
  (let [half-width  (* (:width part) 1/2)
        fifth-height (* (:height part) 1/5)
        y        (:y cart-position)
        y-top    (- y fifth-height)
        y-bottom (+ y fifth-height)
        x1 (- (+ (:x (:position part)) (:x cart-position)) half-width)
        x2 (+ (+ (:x (:position part)) (:x cart-position)) half-width)]
    (stroke (:color part) 360 360)
    (line x1 y-top x2 y-top)
    (line x1 y x2 y)
    (line x1 y-bottom x2 y-bottom)))

(defn draw-triple-line-vertical
  "draws three vertical lines type"
  [part cart-position]
  (let [fifth-width  (* (:width part) 1/5)
        half-height (* (:height part) 1/2)
        x       (+ (:x (:position part)) (:x cart-position))
        x-left  (- x fifth-width)
        x-right (+ x fifth-width)
        y1 (- (:y cart-position) half-height)
        y2 (+ (:y cart-position) half-height)]
    (stroke (:color part) 360 360)
    (line x y1 x y2)
    (line x-left y1 x-left y2)
    (line x-right y1 x-right y2)))

(defn draw-box
  "draws the box type"
  [part cart-position]
  (let [half-width  (/ 20 2)
        half-height (/ 20 2)
        x1 (- (+ (:x (:position part)) (:x cart-position)) half-width)
        y1 (- (:y cart-position) half-height)
        y2 (+ (:y cart-position) half-height)
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
           (:y cart-position)
           (* (:width part) 1/3)
           (* (:width part) 1/3)))

(defn draw-dot
  "draws dot type"
  [part cart-position]
  (fill (:color part) 360 360)
  (stroke (:color part) 360 360) 
  (ellipse (+ (:x (:position part)) (:x cart-position)) 
           (:y cart-position)
           (* (:width part) 1/8)
           (* (:width part) 1/8)))

(defn draw-double-dot-v
  "draws the two vertical dots type"
  [part cart-position]
  (fill (:color part) 360 360)
  (stroke (:color part) 360 360)
  (let [quarter-y (* (:height part) 1/4)
        r         (* (:width part) 1/8)
        x  (+ (:x (:position part)) (:x cart-position))
        y-top    (- (:y cart-position) quarter-y)
        y-bottom (+ (:y cart-position) quarter-y)]
    (ellipse x
             y-top
             r
             r)
    (ellipse x
             y-bottom
             r
             r)))

(defn draw-double-dot-h
  "draws the two hortizontal dots type"
  [part cart-position]
  (fill (:color part) 360 360)
  (stroke (:color part) 360 360)
  (let [quarter-x (* (:width part) 1/4)
        r         (* (:width part) 1/8)
        x         (+ (:x (:position part)) (:x cart-position))
        x-left    (- x quarter-x)
        x-right   (+ x quarter-x)
        y (:y cart-position)]
    (ellipse x-left
             y
             r
             r)
    (ellipse x-right
             y
             r
             r)))

(defn draw-quad-dot-x
  "draws four dots in x shape"
  [part cart-position]
  (let [x-left     (- (:x (:position part)) (:x cart-position) (* (:width part) 1/4))
        x-right    (+ (:x (:position part)) (:x cart-position) (* (:width part) 1/4))
        left-part  (assoc-in part [:position :x] x-left)
        right-part (assoc-in part [:position :x] x-right)]
    (draw-double-dot-v left-part cart-position)
    (draw-double-dot-v right-part cart-position)))

(defn draw-quad-dot-cross
  "draws four dots in cross shape"
  [part cart-position]
  (draw-double-dot-v part cart-position)
  (draw-double-dot-h part cart-position))

(defn draw-circle-lg
  "draws the circle-lg type"
  [part cart-position]
  (no-fill)
  (stroke (:color part) 360 360)
  (ellipse (+ (:x (:position part)) (:x cart-position))
           (:y cart-position) 
           (:width part) 
           (:width part)))

(defn draw-circle-dot
  "draws the circle-sm type"
  [part cart-position]
  (no-fill)
  (stroke (:color part) 360 360) 
  (ellipse (+ (:x (:position part)) (:x cart-position)) 
           (:y cart-position) 
           (:width part) 
           (:width part))
  (draw-dot part cart-position))

(defn build-cart-part
  "builds a cart according the random ints given"
  [cart a b shade c]
  (let [type (cond
               (= a 0)  "X"
               (= a 1)  "BOX"
               (= a 2)  "CIRCLE-SM"
               (= a 3)  "CIRCLE-LG"
               (= a 4)  "CIRCLE-DOT"
               (= a 5)  "BOX-X"
               (= a 6)  "DOUBLE-DOT-HORIZONTAL"
               (= a 7)  "DOUBLE-DOT-VERTICAL"
               (= a 8)  "QUAD-DOT-CROSS"
               (= a 9)  "QUAD-DOT-X"
               (= a 10) "DOT"
               (= a 11) "CROSS"
               (= a 12) "THREE-LINES-VERTICAL"
               (= a 13) "THREE-LINES-HORIZONTAL"
               :else   (println "failed to select cart type"))
        color (cond
                (= b 0) (+ 0  shade)
                (= b 1) (+ 23  shade)
                (= b 2) (+ 13  shade)
                (= b 3) (+ 110 shade)
                (= b 4) (+ 120 shade)
                (= b 5) (+ 131 shade)
                (= b 6) (+ 144 shade)
                (= b 7) (+ 130 shade)
                :else (println "failed to select cart color"))
        w (/ (:part-width @all-cart) 9)
        x 0
        x-coord (cond
                  (= c 4) (- x (* (:part-width @all-cart) w 1/2))
                  (= c 1) (- x (* (:part-width @all-cart) w 1/4))
                  (= c 2) (- x (* (:part-width @all-cart) w 1/6))
                  (= c 3) (- x (* (:part-width @all-cart) w 1/8))
                  (= c 0) x 
                  (= c 5) (+ x (* (:part-width @all-cart) w 1/8))
                  (= c 6) (+ x (* (:part-width @all-cart) w 1/6))
                  (= c 7) (+ x (* (:part-width @all-cart) w 1/4))
                  (= c 8) (+ x (* (:part-width @all-cart) w 1/2))
                  :else (println "failed to select cart position"))]
   
    (assoc-in cart [:data :parts]
              (conj (:parts (:data cart))
                    (Part. type color 
                           {:x x-coord :y (:y (:position (:data cart)))} 
                           (:part-width @all-cart) 
                           (:part-height @all-cart))))))

(defn get-direction
  "returns string direction that matches the provided int"
  [dir]
  (cond
    (= dir 0) "EAST"
    (= dir 1) "WEST"
    (= dir 2) "NORTH"
    (= dir 3) "SOUTH" 
    (= dir 4) "NORTH"
    (= dir 5) "NORTH"
    (= dir 6) "NORTH"
    (= dir 7) "NORTH"
    :else (println "failed to assign a direction")))

(defn build-cart
  "builds a cart according the random ints given"
  [part-count]
  (let [cart (Cart. -1 default-cart-data)
        cart (assign-cart-id cart)
        rand-speed (+ (rand-int 500) 4)
        rand-color (rand-int 8)
        row-count (/ (:height @all-cart) (* (:part-height @all-cart) 1))
        col-count (/ (:width @all-cart)  (* (:part-width @all-cart) 1))
        rand-row (rand-int row-count)
        rand-col (rand-int col-count)
        rand-y 0 ;;(* rand-row (* (:part-height @all-cart) 1))
        rand-x 0 ;;(* rand-col (* (:part-width @all-cart) 1))
        rand-direction (rand-int 8)
        cart (assoc-in cart [:data :direction] (get-direction rand-direction))
        cart (assoc-in cart [:data :speed] rand-speed)
        cart (assoc-in cart [:data :position :y] rand-y)
        cart (assoc-in cart [:data :position :x] rand-x)
        cart (assoc-in cart [:data :start-x] rand-x)
        cart (assoc-in cart [:data :start-y] rand-y)]
    (loop [p 0
           c cart]
      (if (<= p part-count)
        (let [rand-type (rand-int 14)
              rand-shade 0 ;;(- 20 (rand-int 40)) 
              rand-part-position (rand-int 9)
              ]
          (recur (inc p) (build-cart-part c rand-type rand-color rand-shade rand-part-position)))
        c))))

(defn cart-generator
  "generates a give number of uniques carts"
  [count]
  (doseq [c (range count)]
    (let [part-count (+ (rand-int 3) 1)
          cart  (build-cart part-count)] 
      (swap! all-cart assoc-in  [:carts] (conj (:carts @all-cart) cart))
      (swap! all-cart update-in [:count] inc))))

(defn draw-carts
  "renders all carts to the screen"
  [] 
  (doseq [cart (:carts @all-cart)]
    (doseq [part (:parts (:data cart))]
      (cond
        (= (:type part) "X")                      (draw-x part (:position (:data cart)))
        (= (:type part) "CROSS")                  (draw-cross part (:position (:data cart)))
        (= (:type part) "BOX")                    (draw-box part (:position (:data cart)))
        (= (:type part) "BOX-X")                  (draw-box-x part (:position (:data cart)))
        (= (:type part) "CIRCLE-SM")              (draw-circle-sm part (:position (:data cart)))
        (= (:type part) "CIRCLE-LG")              (draw-circle-lg part (:position (:data cart)))
        (= (:type part) "CIRCLE-DOT")             (draw-circle-dot part (:position (:data cart)))
        (= (:type part) "DOUBLE-DOT-VERTICAL")    (draw-double-dot-v part (:position (:data cart)))
        (= (:type part) "DOUBLE-DOT-HORIZONTAL")  (draw-double-dot-h part (:position (:data cart)))
        (= (:type part) "QUAD-DOT-X")             (draw-quad-dot-x part (:position (:data cart)))
        (= (:type part) "QUAD-DOT-CROSS")         (draw-quad-dot-cross part (:position (:data cart)))
        (= (:type part) "DOT")                    (draw-dot part (:position (:data cart)))
        (= (:type part) "THREE-LINES-VERTICAL")   (draw-triple-line-vertical part (:position (:data cart)))
        (= (:type part) "THREE-LINES-HORIZONTAL") (draw-triple-line-horizontal part (:position (:data cart)))
        :else (println "failed to draw" (:type part))))))

(defn move-cart
  "inc a carts coord in the direction specified"
  [cart] 
  (let [cart (if (:is-transitioning (:data cart))
               (if (> (:transition-count (:data cart)) 6)
                 (let [cart (assoc-in cart [:data :transition-count] 0)
                       cart (assoc-in cart [:data :is-transitioning] false)
                       rand-continue (rand-int 500)]
                   (if (< rand-continue 200)
                     (assoc-in cart [:data :is-transitioning] true)
                     cart))
                 (update-in cart [:data :transition-count] inc))
               (let [cart (assoc-in cart [:data :is-transitioning] true)
                     cart (assoc-in cart [:data :transition-count] 1)
                     cart (assoc-in cart [:data :age] 0)
                     rand-direction (rand-int 8)
                     cart (assoc-in cart [:data :direction] (get-direction rand-direction))]
                 cart))]

    (let [cart (cond
                 (= (:direction (:data cart)) "EAST")  (assoc-in cart [:data :position :x] (+ (:x (:position (:data cart)))
                                                                                              (* (:part-width @all-cart) 1/3)))
                 (= (:direction (:data cart)) "WEST")  (assoc-in cart [:data :position :x] (- (:x (:position (:data cart)))
                                                                                              (* (:part-width @all-cart) 1/3)))
                 (= (:direction (:data cart)) "SOUTH") (assoc-in cart [:data :position :y] (+ (:y (:position (:data cart)))
                                                                                              (* (:part-height @all-cart) 1/3)))
                 (= (:direction (:data cart)) "NORTH") (assoc-in cart [:data :position :y] (- (:y (:position (:data cart)))
                                                                                              (* (:part-height @all-cart) 1/3)))
                 :else (println "failed to move cart"))
          cart (if (> (:x (:position (:data cart))) (:width @all-cart))
                 (assoc-in cart [:data :position :x] 0)
                 cart)
          cart (if (< (:x (:position (:data cart))) 0)
                 (assoc-in cart [:data :position :x] (:width @all-cart))
                 cart)
          cart (if (> (:y (:position (:data cart))) (:height @all-cart))
                 (assoc-in cart [:data :position :y] 0)
                 cart)
          cart (if (< (:y (:position (:data cart))) 0)
                 (assoc-in cart [:data :position :y] (:height @all-cart))
                 cart)]
      cart)))

(defn update-carts
  "updates the cart's age and toggles to ready"
  []
  (swap! all-cart
         assoc-in [:carts] (vec (for [c (:carts @all-cart)]
                                  (if (or (> (:age (:data c)) (:speed (:data c)))
                                          (:is-transitioning (:data c)))
                                    (move-cart c)
                                    (update-in c [:data :age] inc)))))
                                     
  (draw-carts))

