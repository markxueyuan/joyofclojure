(ns joyofclojure.chapter7-bot)

(def bearings
  [{:x 0 :y 1}
   {:x 1 :y 0}
   {:x 0 :y -1}
   {:x -1 :y 0}])

(defn forward [x y bearing-num]
  (let [cp (bearings bearing-num)]
    [(+ x (:x cp))
     (+ y (:y cp))]))

(defn bot [x y bearing-num]
  {:coords [x y]
   :bearing ([:north :east :south :west] bearing-num)
   :forward (fn []
              (let [cp (bearings bearing-num)]
                (bot (+ x (:x cp))
                     (+ y (:y cp))
                     bearing-num)))
   :turn-right (fn []
                 (bot x y (mod (+ 1 bearing-num) 4)))
   :turn-left (fn []
                (bot x y (mod (- bearing-num 1) 4)))})


(:coords (bot 5 5 0))
(:bearing (bot 5 5 0))

(:coords ((:forward (bot 5 5 0))))

(:bearing ((:turn-left (bot 5 5 0))))

(:bearing ((:forward ((:forward ((:turn-left (bot 5 5 0))))))))

(:coords ((:forward ((:forward ((:turn-right (bot 5 5 0))))))))

(defn mirror-bot [x y bearing-num]
  {:coords [x y]
   :bearing ([:north :east :south :west] bearing-num)
   :forward (fn []
              (let [cp (bearings bearing-num)]
                (mirror-bot (- x (:x cp))
                            (- y (:y cp))
                            bearing-num)))
   :turning-right (fn [] (mirror-bot x y (mod (- bearing-num 1) 4)))
   :turning-left (fn [] (mirror-bot x y (mod (+ 1 bearing-num) 4)))})





