(ns sketch.cart
  (:require
   [quil.core :refer :all]
   [sketch.shapes :as shape]
   [sketch.calculations :as calc]
   [sketch.path :as path]
   [sketch.cart :as cart]
   [clojure.set :as set]))

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

(defn unique-random-numbers
  [n]
  (let [a-set (set (take n (repeatedly #(rand-int n))))]
    (concat a-set (set/difference (set (take n (range)))
                                  a-set))))

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
  [cart n] 
  (assoc-in cart [:id] n))

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

(defn draw-diamond
  "draws the diamond type"
  [part cart-position]
  (let [half-width  (* (:width part) 1/2)
        half-height (* (:height part) 1/2)
        x1 (- (+ (:x (:position part)) (:x cart-position)) half-width)
        y1 (- (:y cart-position) half-height)
        y2 (+ (:y cart-position) half-height)
        x2 (+ (+ (:x (:position part)) (:x cart-position)) half-width)
        x-half (+ x1 half-width)
        y-half (+ y1 half-height)]
    (stroke (:color part) 360 360)
    (line x-half y1 x2 y-half)
    (line x2 y-half x-half y2)
    (line x-half y2 x1 y-half)
    (line x1 y-half x-half y1)))

(defn draw-diag-left
  "draws the left slope line type"
  [part cart-position]
  (let [half-width  (* (:width part) 1/2)
        half-height (* (:height part) 1/2)
        x1 (- (+ (:x (:position part)) (:x cart-position)) half-width)
        y1 (- (:y cart-position) half-height)
        y2 (+ (:y cart-position) half-height)
        x2 (+ (+ (:x (:position part)) (:x cart-position)) half-width)]
    (stroke (:color part) 360 360)
    (line x1 y1 x2 y2)))

(defn draw-diag-right
  "draws the right slope line type"
  [part cart-position]
  (let [half-width  (* (:width part) 1/2)
        half-height (* (:height part) 1/2)
        x1 (- (+ (:x (:position part)) (:x cart-position)) half-width)
        y1 (- (:y cart-position) half-height)
        y2 (+ (:y cart-position) half-height)
        x2 (+ (+ (:x (:position part)) (:x cart-position)) half-width)]
    (stroke (:color part) 360 360)
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

(defn draw-box-sm
  "draws the box type"
  [part cart-position]
  (let [half-width  (/ (:width  part) 2)
        half-height (/ (:height part) 2)
        x1 (- (+ (:x (:position part)) (:x cart-position)) half-width)
        y1 (- (:y cart-position) half-height)
        y2 (+ (:y cart-position) half-height)
        x2 (+ (+ (:x (:position part)) (:x cart-position)) half-width)] 
    (stroke (:color part) 360 360)
    (line x1 y1 x2 y1)
    (line x2 y1 x2 y2)
    (line x2 y2 x1 y2)
    (line x1 y2 x1 y1)))

(defn draw-box-lg
  "draws the box type"
  [part cart-position]
  (let [x1 (- (+ (:x (:position part)) (:x cart-position)) (:width part))
        y1 (- (:y cart-position) (:height part))
        y2 (+ (:y cart-position) (:height part))
        x2 (+ (+ (:x (:position part)) (:x cart-position)) (:width part))]
    (stroke (:color part) 360 360)
    (line x1 y1 x2 y1)
    (line x2 y1 x2 y2)
    (line x2 y2 x1 y2)
    (line x1 y2 x1 y1)))

(defn draw-box-x
  "draws the box type"
  [part cart-position]
  (draw-box-lg part cart-position)
  (draw-x part cart-position))

(defn draw-circle-sm
  "draws the circle-sm type"
  [part cart-position]
  (no-fill)
  (stroke (:color part) 360 360)
  (ellipse (+ (:x (:position part)) (:x cart-position)) 
           (:y cart-position)
           (* (:width part) 1/2)
           (* (:width part) 1/2)))

(defn draw-dot
  "draws dot type"
  [part cart-position]
  (fill (:color part) 360 360)
  (stroke (:color part) 360 360) 
  (ellipse (+ (:x (:position part)) (:x cart-position)) 
           (:y cart-position)
           (* (:width part) 1/16)
           (* (:width part) 1/16)))

(defn draw-double-dot-v
  "draws the two vertical dots type"
  [part cart-position]
  (fill (:color part) 360 360)
  (stroke (:color part) 360 360)
  (let [quarter-y (* (:height part) 1/2)
        r         (* (:width part) 1/16)
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
  (let [quarter-x (* (:width part) 1/2)
        r         (* (:width part) 1/16)
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
  (let [x-left     (- (:x (:position part)) (:x cart-position) (* (:width part) 1/2))
        x-right    (+ (:x (:position part)) (:x cart-position) (* (:width part) 1/2))
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

(defn draw-grid
  "draws grid automa grid"
  [w h]
  (stroke 360 360 360)
  (stroke-weight 1)
  (doseq [x (range w)]
    (line (* (:part-width @all-cart) x) 0
          (* (:part-width @all-cart) x) (:height @all-cart)))
  (doseq [y (range h)]
    (line 0 (* (:part-height @all-cart) y) 
          (:height @all-cart) (* (:part-height @all-cart) y)))
  (stroke-weight 3))

(defn draw-cart-center
  "draws grid automa grid"
  []
  (stroke 360 0 360)
  (stroke-weight 1)
  (doseq [cart (:carts @all-cart)]
    (ellipse (:x (:position (:data cart))) (:y (:position (:data cart))) 2 2))
  (stroke-weight 3)
  (stroke 360 360 360))

(defn get-container-position [x w]
  (cond
    (= x 0) (- (* (:part-width @all-cart) w 1/2))
    (= x 1) 0
    (= x 2) (* (:part-width @all-cart) w 1/2)
    :else (println "failed to get container poistion")))

(defn get-accent-position [x w]
  (cond 
    (= x 0) (- (* (:part-width @all-cart) w 1/4))
    (= x 1) (- (* (:part-width @all-cart) w 1/8))
    (= x 2) (- (* (:part-width @all-cart) w))
    (= x 3) (- (* (:part-width @all-cart) w 1/2))
    (= x 4) (* (:part-width @all-cart) w 1/2) 
    (= x 5) (* (:part-width @all-cart) w)
    (= x 6) (* (:part-width @all-cart) w 1/8)
    (= x 7) (* (:part-width @all-cart) w 1/4)
    :else (println "failed to get accent poistion")))

(defn build-cart-part
  "builds a cart according the random ints given"
  [cart a b shade]
  (let [type (cond
               (= a 0)  "BOX-LG"
               (= a 1)  "BOX-SM"
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
               (= a 14) "X"
               (= a 15) "DIAG-LEFT"
               (= a 16) "DIAG-RIGHT"
               (= a 17) "DIAMOND"
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
        w (/ (:part-width @all-cart) 10)
        x 0
        x-coord (if (or (= type "BOX-LG")
                        (= type "BOX-SM")
                        (= type "CIRCLE-SM")
                        (= type "CIRCLE-LG")
                        (= type "CIRCLE-DOT")
                        (= type "BOX-X")
                        (= type "X")
                        (= type "DIAG-LEFT")
                        (= type "DIAG-RIGHT")
                        (= type "DIAMOND"))
                  (get-container-position (rand-int 3) w)
                  (get-accent-position (rand-int 8) w))]
   
    (assoc-in cart [:data :parts]
              (conj (:parts (:data cart))
                    (Part. type 
                           color 
                           {:x x-coord :y (/ (:y (:position (:data cart))) 2)} 
                           (:part-width @all-cart) 
                           (:part-height @all-cart))))))

(defn get-direction
  "returns string direction that matches the provided int"
  [dir]
  (cond
    (= dir 0) "NORTH"
    (= dir 1) "NORTH"
    (= dir 2) "NORTH"
    (= dir 3) "NORTH" 
    (= dir 4) "NORTH"
    (= dir 5) "EAST"
    (= dir 6) "WEST"
    (= dir 7) "SOUTH"
    :else (println "failed to assign a direction")))

(defn build-cart
  "builds a cart according the random ints given"
  [parts]
  (let [cart (Cart. -1 default-cart-data)
        rand-speed (+ (rand-int (:min-speed @all-cart)) 
                      (:max-speed  @all-cart))
        color (:color @all-cart)
        ;; rand-color (rand-int 8)
        row-count (/ (:height @all-cart) (:part-height @all-cart))
        col-count (/ (:width  @all-cart) (:part-width  @all-cart))
        rand-row (rand-int row-count)
        rand-col (rand-int col-count)
        rand-y (* rand-row (:part-height @all-cart))
        rand-y (if (< rand-y 0)
                 0
                 rand-y)
        rand-x (* rand-col (:part-width  @all-cart))
        rand-x (if (< rand-x 0)
                 0
                 rand-x)
        rand-direction (rand-int 8)
        cart (assoc-in cart [:data :direction]  (get-direction rand-direction))
        cart (assoc-in cart [:data :speed]       rand-speed)
        cart (assoc-in cart [:data :position :y] rand-y)
        cart (assoc-in cart [:data :position :x] rand-x)
        cart (assoc-in cart [:data :start-x]     rand-x)
        cart (assoc-in cart [:data :start-y]     rand-y)]
    (loop [p 0
           c cart]
      (if (<= p (- (count parts) 1))
        (let [type (get parts p) 
              rand-shade 0 ;;(- 20 (rand-int 40)) 
              ]
          (recur (inc p) (build-cart-part c type color rand-shade)))
        c))))

(defn change-cart-color
  "changes every cart part color"
  [cart color]
  (assoc-in cart [:data :parts] (vec (for [p (:parts (:data cart))]
                                 (assoc-in p [:color] color)))))

(defn cart-generator
  "generates a give number of uniques carts"
  []
  (doseq [c (range (:count @all-cart))]
    (let [;;parts (vec (take 5 (unique-random-numbers 15)))
          parts (vec (take (:part-count @all-cart) 
                           (repeatedly #(rand-int 18))))
          cart  (build-cart parts)
          cart (assign-cart-id cart c)] 
      (swap! all-cart assoc-in  [:carts] (conj (:carts @all-cart) cart)))))

(defn draw-carts
  "renders all carts to the screen"
  [] 
  (doseq [cart (:carts @all-cart)]
    (doseq [part (:parts (:data cart))]
      (cond
        (= (:type part) "X")                      (draw-x part (:position (:data cart)))
        (= (:type part) "CROSS")                  (draw-cross part (:position (:data cart)))
        (= (:type part) "BOX-LG")                 (draw-box-lg part (:position (:data cart))) 
        (= (:type part) "BOX-SM")                 (draw-box-sm part (:position (:data cart)))
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
        (= (:type part) "DIAG-LEFT")              (draw-diag-left part (:position (:data cart)))
        (= (:type part) "DIAG-RIGHT")             (draw-diag-right part (:position (:data cart)))
        (= (:type part) "DIAMOND")                (draw-diamond part (:position (:data cart)))
        :else (println "failed to draw" (:type part))))))

(defn move-cart
  "inc a carts coord in the direction specified"
  [cart]
  (let [cart (if (:is-transitioning (:data cart))
               (if (>= (:transition-count (:data cart)) (- (/ (:part-width @all-cart) 2) 2))
                 (let [cart (assoc-in cart [:data :is-transitioning] false)]
                   cart)
                 (update-in cart [:data :transition-count] inc))
               (let [rand-direction (rand-int 8)
                     cart (assoc-in cart [:data :is-transitioning] true)
                     cart (assoc-in cart [:data :transition-count] 0)
                     cart (assoc-in cart [:data :age] 0)
                     cart (assoc-in cart [:data :direction] (get-direction rand-direction))]
                 cart))
        cart (cond
               (= (:direction (:data cart)) "EAST")  (let [cart (change-cart-color cart 23)]
                                                       (assoc-in cart [:data :position :x] (+ (:x (:position (:data cart))) 2)))
               (= (:direction (:data cart)) "WEST")  (let [cart (change-cart-color cart 131)]
                                                       (assoc-in cart [:data :position :x] (- (:x (:position (:data cart))) 2)))
               (= (:direction (:data cart)) "SOUTH") (let [cart (change-cart-color cart 131)]
                                                       (assoc-in cart [:data :position :y] (+ (:y (:position (:data cart))) 2)))
               (= (:direction (:data cart)) "NORTH") (let [cart (change-cart-color cart 131)]
                                                       (assoc-in cart [:data :position :y] (- (:y (:position (:data cart))) 2)))
               :else (println "failed to move cart"))
        cart (if (> (:x (:position (:data cart))) (+ (:width @all-cart) (:edge-extention @all-cart)))
               (let [cart (assoc-in cart [:data :position :x] (- (:edge-extention @all-cart)))
                     cart (assoc-in cart [:data :position :y] (- (:y (:position (:data cart))) 
                                                                 (* (:part-height @all-cart) 2)))
                     cart (assoc-in cart [:data :transition-count] -1)
                     cart (assoc-in cart [:data :is-transitioning] true)]
                 cart)
               cart)
        cart (if (< (:x (:position (:data cart)))  (- (:edge-extention @all-cart)))
               (let [cart (assoc-in cart [:data :position :x] (+ (:width @all-cart) (:edge-extention @all-cart)))
                     cart (assoc-in cart [:data :position :y] (+ (:y (:position (:data cart))) 
                                                                 (* (:part-height @all-cart) 2)))
                     cart (assoc-in cart [:data :transition-count] -1)
                     cart (assoc-in cart [:data :is-transitioning] true)]
                 cart)
               cart)
        cart (if (> (:y (:position (:data cart))) (+ (:height @all-cart) (:edge-extention @all-cart)))
               (let [cart (assoc-in cart [:data :position :y] (- (:edge-extention @all-cart)))
                     cart (assoc-in cart [:data :position :x] (- (:x (:position (:data cart))) 
                                                                 (* (:part-width @all-cart) 2)))
                     cart (assoc-in cart [:data :transition-count] -1)
                     cart (assoc-in cart [:data :is-transitioning] true)]
                 cart)
               cart)
        cart (if (< (:y (:position (:data cart))) (- (:edge-extention @all-cart)))
               (let [cart (assoc-in cart [:data :position :y] (+ (:height @all-cart) (:edge-extention @all-cart)))
                     cart (assoc-in cart [:data :position :x] (+ (:x (:position (:data cart))) 
                                                                 (* (:part-width @all-cart) 2)))
                     cart (assoc-in cart [:data :transition-count] -1)
                     cart (assoc-in cart [:data :is-transitioning] true)]
                 cart)
               cart)
        rand-continue (rand-int 500)
        cart (if (and (not (:is-transitioning (:data cart)))
                      (= (:transition-count (:data cart)) (- (/ (:part-width @all-cart) 2) 2))
                      (< rand-continue 200))
               (let [cart (assoc-in cart [:data :transition-count] -1)
                     cart (assoc-in cart [:data :is-transitioning] true)]
                 cart)
               cart)]
    cart))

(defn update-carts
  "updates the cart's age and toggles to ready"
  [] 
  (swap! all-cart
         assoc-in [:carts] (vec (for [c (:carts @all-cart)]
                                  (if (or (> (:age (:data c)) (:speed (:data c)))
                                          (:is-transitioning (:data c)))
                                    (move-cart c)
                                    (let [c (update-in c [:data :age ] inc)
                                          color (:color (first (:parts (:data c))))
                                          c (cond
                                              (= color 360)  (change-cart-color c 0)
                                              (< color 23)   (change-cart-color c (+ color 1/4))
                                              (> color 23)   (change-cart-color c 0)
                                              :else c)]
                                      c))))) 
  
  (when (:draw-gridlines @all-cart)
    (draw-grid (/ (:width  @all-cart) (:part-width  @all-cart))
               (/ (:height @all-cart) (:part-height @all-cart)))) 
  (draw-carts) 
  (when (:draw-cart-center @all-cart)
    (draw-cart-center))
  )

