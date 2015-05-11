(ns joyofclojure.chapter6-lazy)

(defn rec-step [[x & xs]]
  (if x
    [x (rec-step xs)]
    []))


(def very-lazy (-> (iterate #(do (println "it is" %) (inc %)) 1)
                   rest
                   rest
                   rest))

(def less-lazy (-> (iterate #(do (println "it is" %) (inc %)) 1)
                   next
                   next
                   next))

(defn lazy-rec-step [s]
  (lazy-seq
    (if (seq s)
      [(first s) (lazy-rec-step (rest s))]
      [])))

(defn simple-range [i limit]
  (lazy-seq
    (when (< i limit)
      (cons i (simple-range (inc i) limit)))))

(defn triangle [n]
  (/ (* (+ 1 n) n) 2))

;(map triangle (range 1 11))

(def tri-nums (map triangle (iterate inc 1)))

(take 10 tri-nums)

(take 10 (filter even? tri-nums))

(nth tri-nums 99)

(double (reduce + (take 1000 (map / tri-nums))))

(take 2 (drop-while #(< % 10000) tri-nums))

(defn defer-expensive [cheap expensive]
  (if-let [good-enough (force cheap)]
    good-enough
    (force expensive)))

(defer-expensive (delay :cheap) (delay (do (Thread/sleep 5000) :expensive)))

(defer-expensive (delay false) (delay (do (Thread/sleep 5000) :expensive)))

(defn inf-triangles [n]
  {:head (triangle n)
   :tail (delay (inf-triangles (inc n)))})

(defn lazy-triangles [n]
  (lazy-seq
    {:head (triangle n)
     :tail (delay (inf-triangles (inc n)))}))

(defn head [l]
  (:head l))

(defn tail [l]
  (force (:tail l)))

(def tri-nums (inf-triangles 1))



(defn taker [n l]
  (loop [t n src l ret []]
    (if (zero? t)
      ret
      (recur (dec t) (tail src) (conj ret (head src))))))

(defn nthr [l n]
  (if (zero? n)
    (head l)
    (recur (tail l) (dec n))))


