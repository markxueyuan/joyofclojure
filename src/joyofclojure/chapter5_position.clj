(ns joyofclojure.chapter5-position)

(defn pos [e coll]
  (let [cmp (if (map? coll)
              #(= (second %1) %2)
              #(= %1 %2))]
    (loop [s coll idx 0]
      (when (seq s)
        (if (cmp (first s) e)
          (if (map? coll)
            (ffirst s)
            idx)
          (recur (rest s) (inc idx)))))))

(defn index [coll]
  (cond
    (map? coll) (seq coll)
    (set? coll) (map vector coll coll)
    :else (map vector (iterate inc 0) coll)))

(defn pos-better [e coll]
  (for [[i v] (index coll)
        :when (= e v)]
    i))

(defn pos-final [pred coll]
  (for [[i v] (index coll)
        :when (pred v)]
    i))