(ns joyofclojure.chapter2)

(def make-list2+ #(list %1 %2 %&))

(defn what-do-do []
  (do
    (def x 5)
    (def y 4)
    (+ x y)
    [x y]))

(defn sum [s x]
  (if (< x 100)
    (recur (+ s x) (+ x 2))
    s))

;(println (sum 0 1))

(defn sun [s x]
  (if (< x 100)
    (recur (+ s (+ x 2)) (+ x 2))
    s))

;(println (sun 0 1))

(defn sun [s x]
  (let [y (+ x 2)]
    (if (< x 100)
      (recur (+ s y) y)
      s)))

(defn see-unquote []
  (let [x '(2 3)]
    `(1 ~x)))

(defn see-unquote-splicing []
  (let [x '(2 3)]
    `(1 ~@x)))

(defn create-instance []
  (new java.awt.Point 0 1))

(defn new-hashmap []
  (new java.util.HashMap {"foo" 42 "bar" 9 "baz" "quux"}))

(defn access-instance-variable
  []
  (.-x (create-instance)))


(defn setting-instance-variable []
  (let [origin (new java.awt.Point 0 0)]
    (set! (.-x origin) 15)
    (str origin)))

(defn see-throw
  [string]
  (throw (Exception. string)))

(defn throw-catch
  [f]
  (try
    (f)
    (catch ArithmeticException e "No dividing by zero")
    (catch Exception e (str "You are no good "(.getMessage e)))
    (finally (println "returning ..."))))

(defn report-ns []
  (str "The current namespace is " *ns*))

(def evil-false
  (new Boolean "false"))

(defn true-false
  []
  (if (Boolean/valueOf "false")
    :truthy
    :falsey))

