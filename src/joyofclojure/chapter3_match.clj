(ns joyofclojure.chapter3-match
  (:require [clojure.core.match :refer [match]]))

(defn match-literals
  [x y z]
  (match [x y z]
    [_ false true] 1
    [false true _] 2
    [_ _ false] 3
    [_ _ true] 4
    :else 5))

;(match-lietrals true true true)

(defn match-single-literal
  [x]
  (match x
    true 1
    false 2
    :else 5))

;(match-single-literal false)

(defn name-values
  [x y]
  (match [x y]
    [1 b] b
    [a 2] a
    :else nil))

(defn match-sequence
  [x]
  (match [x]
    [([1] :seq)] :a0
    [([1 2] :seq)] :a1
    [([1 2 nil nil] :seq)] :a3
    :else nil))

(defn match-vector
  [x]
  (match [x]
    [[_ _ 2]] :a0
    [[1 2 3]] :a2
    [[1 1 3]] :a3
    :else nil))

(defn rest-patterns
  [x]
  (match [x]
    [([1] :seq)] :a0
    [([1 & a] :seq)] [:a1 a]
    :else nil))

;(rest-pattern '(1 2 3))

(defn match-map
  [x]
  (match [x]
    [{:a 1 :b 2}] :a0
    [{:a 2 :b b}] [:a0 b]
    :else 'c))

(defn match-map-only
  [x]
  (match x
    ({:a 1 :b 2} :only [:a :b]) :yes
    {:a 1 :b 2 :c _} :no
    :else nil))

(defn or-patterns
  [x y z]
  (match [x y z]
    [1 (:or 2 3) 4] :a0
    [(:or 5 6 7) _ b] [:a0 b]
    :else nil))

(defn guards-on-patterns
  [x y]
  (match [x y]
    [_ (_ :guard odd?)] :a
    [(b :guard #(even? %)) _] [:b b]
    :else nil))

(defn function-application
  [x y]
  (match [x y]
    [(1 :<< inc) (_ :guard odd?)] :a
    :else nil))

(extend-type java.util.Date
  clojure.core.match.protocols/IMatchLookup
  (val-at [this k not-found]
    (case k
      :day (.getDay this)
      :month (.getMonth this)
      :year (.getYear this)
      not-found)))


(defn match-date-via-map
  [year month date & [hour minute]]
  (match (java.util.Date. year month date hour minute)
    {:year 2009 :month a} a
    {:year (:or 2010 2011) :month b} b
    :else :no-match))

