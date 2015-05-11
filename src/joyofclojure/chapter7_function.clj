(ns joyofclojure.chapter7-function
  (:require [clojure.test :refer [run-tests]]))

(defn fnth [n]
  (apply comp
         (cons first
               (take (dec n) (repeat rest)))))

(map (comp
       keyword
       #(.toLowerCase %)
       name)
     '(:a B c))


(=
  ((partial + 5)
    100
    200)
  (#(apply + 5 %&)
    100
    200))

((complement even?) 1)

(defn join
  {:test (fn []
           (assert
             (= (join "," [1 2 3]) "1,3,3")))}
  [sep s]
  (apply str (interpose sep s)))

;(run-tests)


;;;;;;;;;;; high order functions ;;;;;;;;;;;;;;;

; sorting, function as argument
(sort [1 -42 0 39])
(sort ["z" "x" "a" "aa"])
(sort [(java.util.Date.) (java.util.Date. 100)])
(sort [[1 2 3]
       [-1 0 1]
       [1 2 -1]])

(sort > [1 2 3])

; Here some failure examples for sort on page 141

(sort-by second [[:a 7] [:c 13] [:b 21]])

(sort-by str ["z" "a" "x" 1 2 3])

(sort-by :age [{:age 33} {:age 22} {:age 11}])

(def plays [{:band "a" :plays 321 :loved 7}
            {:band "b" :plays 310 :loved 8}
            {:band "c" :plays 25 :loved 1}])

(def sort-by-loved-ratio
  (partial sort-by #(/ (:plays %) (:loved %))))

; function as return value

(defn columns [column-names]
  (fn [row]
    (vec (map #(% row) column-names))))

(sort-by (columns [:plays :loved :bands]) plays)



; referencial transparency

(defn keys-apply [f ks m]
  (let [only (select-keys m ks)]
    (zipmap (keys only)
            (map f (vals only)))))

(keys-apply #(.toUpperCase %) #{:band} (plays 0))

(defn manip-map [f ks m]
  (merge m (keys-apply f ks m)))

(manip-map #(int (/ % 2)) #{:plays :loved} (plays 0))

(defn slope
  [& {:keys [p1 p2]
      :or {p1 [0 0] p2 [1 1]}}]
  (float (/ (- (p2 1) (p1 1))
            (- (p2 0) (p1 0)))))

;;;;;;;;;;;;; pre and post conditions;;;;;;;;;;;

(defn slope [p1 p2]
  {:pre [(not= p1 p2) (vector? p1) (vector? p2)]
   :post [(float? %)]}
  (/ (- (p2 1) (p1 1))
     (- (p2 0) (p1 0))))

(defn put-things [m]
  (into m {:meat "beef" :veggie "broccoli"}))

(defn vegan-constraints [f m]
  {:pre [(:veggie m)]
   :post [(:veggie %) (nil? (:meat %))]}
  (f m))

(defn balanced-diet [f m]
  {:post [(:meat %) (:veggie %)]}
  (f m))

(defn finicky [f m]
  {:post [(= (:meat %) (:meat m))]}
  (f m))


;;;;;;;;;;; closures ;;;;;;;;;;;;;;;;;

(def times-two
  (let [x 2]
    (fn [y] (* y x))))

(def add-and-get
  (let [ai (java.util.concurrent.atomic.AtomicInteger.)]
    (fn [y] (.addAndGet ai y))))

; closure closed by function, over locals

(defn times-n [n]
  (let [x n]
    (fn [y] (* y x))))

(def times-four (times-n 4))

; closure closed by function, over arguments

(defn times-n2 [n]
  (fn [y] (* y n)))

(def times-four2 (times-n2 4))

(defn divisible [denom]
  (fn [num]
    (zero? (rem num denom))))

(filter even? (range 10))

(filter (divisible 4) (range 10))

(defn filter-divisible [denom s]
  (filter
    (fn [num]
      (zero? (rem num denom)))
    s))

(filter-divisible 4 (range 10))

(defn filter-divisible [denom s]
  (filter #(zero? (rem % denom)) s))

(filter-divisible 5 (range 20))



