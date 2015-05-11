(ns joyofclojure.chapter7-A-star-pathfinding)

(def world [[1   1   1   1   1   1   1]
            [999 999 999 999 999 999 1]
            [1   1   1   1   1   1   1]
            [1 999 999 999 999 999 999]
            [1   1   1   1   1   1   1]
            [1 999 999 999 999 999 999]
            [1   1   1   1   1   1   1]])

(defn neighbors
  ([size yx]
    (neighbors [[-1 0] [1 0] [0 -1] [0 1]]
               size
               yx))
  ([deltas size yx]
    (filter (fn [new-yx]
              (every? #(< -1 % size) new-yx))
            (map #(vec (map + yx %))
                 deltas))))

;(neighbors 5 [0 0])

;(neighbors 5 [2 2])

;; heuristics

(defn estimate-cost [step-cost-est size y x]
  (* step-cost-est
     (- (+ size size) y x 2)))

;(estimate-cost 900 5 0 0)
;(estimate-cost 900 5 4 4)

(defn path-cost [node-cost cheapest-nbr]
  (+ node-cost
     (or (:cost cheapest-nbr) 0)))

;(path-cost 900 {:cost 1})

(defn total-cost [newcost step-cost-est size y x]
  (+ newcost
     (estimate-cost step-cost-est size y x)))

;(total-cost 0 900 5 0 0)

;(total-cost 1000 900 5 3 4)

;(total-cost (path-cost 900 {:cost 1}) 900 5 3 4)

;; retrieve minimum value;

(defn min-by [f coll]
  (when (seq coll)
    (reduce (fn [min other]
              (if (> (f min) (f other))
                other
                min))
            coll)))

;(min-by :cost [{:cost 100} {:cost 36} {:cost 9}])

;; the algorithm

(defn astar [start-yx step-est cell-costs]
  (let [size (count cell-costs)]
    (loop [steps 0
           routes (vec (repeat size (vec (repeat size nil))))
           work-todo (sorted-set [0 start-yx])]
      (if (empty? work-todo)
        [(peek (peek routes)) :steps steps]
        (let [[_ yx :as work-item] (first work-todo)
              rest-work-todo (disj work-todo work-item)
              nbr-yxs (neighbors size yx)
              cheapest-nbr (min-by :cost
                                   (keep #(get-in routes %)
                                         nbr-yxs))
              newcost (path-cost (get-in cell-costs yx)
                                 cheapest-nbr)
              oldcost (:cost (get-in routes yx))]
          (println steps)
          (if (and oldcost (>= newcost oldcost))
            (recur (inc steps) routes rest-work-todo)
            (recur (inc steps) (assoc-in routes yx
                                         {:cost newcost
                                          :yxs (conj (:yxs cheapest-nbr [])
                                                     yx)})
                   (into rest-work-todo
                         (map
                           (fn [w]
                             (let [[y x] w]
                               [(total-cost newcost step-est size y x) w]))
                           nbr-yxs)))))))))

(astar [0 0] 900 world)





