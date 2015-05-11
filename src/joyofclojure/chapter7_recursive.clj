(ns joyofclojure.chapter7-recursive
	(:import (org.omg.CORBA ARG_IN)))


; calculating greatest common denominator

(defn gcd [x y]
	(cond
		(> x y) (recur (- x y) y)
		(< x y) (recur x (- y x))
		:else x))

;; mutual call and trampoline

(defn elevator [command]
	(letfn [(ff-open [[_ & r]]
           "When the elevator is open on the 1st floor
           it can either close or be done."
           #(case _
             :close (ff-closed r)
             :done true
             false))
          (ff-closed [[_ & r]]
           "When the elevator is closed on the 1st floor
           it can either open or go up"
           #(case _
             :open (ff-open r)
             :up (sf-closed r)
             false))
          (sf-closed [[_ & r]]
           "When the elevator is closed on the 2nd floor
           it can either go down or open."
           #(case _
             :down (ff-closed r)
             :open (sf-open r)
             false))
          (sf-open [[_ & r]]
           #(case _
             :close (sf-closed r)
             :done true
             false))]
    (trampoline ff-open command)))

(elevator [:close :up :open :close :down :open :done])


;; Continuation-passing style (CPS)
;; not prevalent in clojure, but more so in the functional tradition

;; It is a hybid between recursion and mutual recursion, but with its own set of idioms.

;factorial

(defn fac-cps [n k]
  (letfn [(cont [v] (k (* v n)))]                           ;next
    (if (zero? n)                                           ;accept
      (k 1)                                                 ;return
      (recur (dec n) cont))))

(defn fac [n]
  (fac-cps n identity))

(defn mk-cps [accept? kend kont]
  (fn [n]
    ((fn [n k]
       (let [cont (fn [v]                                   ;next
                    (k ((partial kont v) n)))]
         (if (accept? n)                                    ;accept
           (k 1)                                            ;return
           (recur (dec n) cont))))
      n kend)))

(def factorial
  (mk-cps zero? identity #(* %1 %2)))

(factorial 10)

(def tri
  (mk-cps #(== 1 %) identity #(+ %1 %2)))

(tri 10)





