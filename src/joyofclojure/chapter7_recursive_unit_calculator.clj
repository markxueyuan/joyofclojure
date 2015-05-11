(ns joyofclojure.chapter7-recursive-unit-calculator)

(def simple-metric
  {:meter 1
   :km 1000
   :cm 1/100
   :mm [1/10 :cm]})

; how many meters are in 3 kilometers, 10 meters, 80 centimeters, 10 millimeters?

(-> (* 3 (:km simple-metric))
    (+ (* 10 (:meter simple-metric)))
    (+ (* 80 (:cm simple-metric)))
    (+ (* (:cm simple-metric)
          (* 10 (first (:mm simple-metric)))))
    float)

(defn convert [context descriptor]
  (reduce
    (fn [result [mag unit]]
      (+ result
         (let [val (get context unit)]
           (if (vector? val)
             (* mag (convert context val))
             (* mag val)))))
    0
    (partition 2 descriptor)))


(float (convert simple-metric [3 :km 10 :meter 80 :cm 10 :mm]))

(convert {:bit 1 :byte 8 :nibble [1/2 :byte]} [32 :nibble])