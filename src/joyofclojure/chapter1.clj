(ns joyofclojure.chapter1
  (:import (joyofclojure.chapter1 Concatenatable)))


(defn consistency1
  []
  (for [x [:a :b]
        y (range 5)
        :when (odd? y)]
    [x y]))

(defn consistency2
  []
  (doseq [x [:a :b]
          y (range 5)
          :when (odd? y)]
    (prn x y)))

(defn APL-like
  ([a op b]
    (op a b))
  ([a op1 b op2 c]
    (op1 a (op2 b c)))
  ([a op1 b op2 c op3 d]
    (op1 a (op2 b (op3 c d)))))

(defn Smalltalk-like
  ([a op b]
    (op a b))
  ([a op1 b op2 c]
    (op2 c (op1 a b)))
  ([a op1 b op2 c op3 d]
    (op3 d (op2 c (op1 a b)))))

(defprotocol Concatenatable
  (cat [this other]))

(extend-type String
  Concatenatable
  (cat [this other]
    (.concat this other)))

(extend-type java.util.List
  Concatenatable
  (cat [this other]
    (concat this other)))



