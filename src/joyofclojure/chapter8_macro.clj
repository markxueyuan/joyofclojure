(ns joyofclojure.chapter8-macro
  (:require [clojure.walk :as walk]))



(list + 1 2)

(eval (list + 1 2))

(list (symbol "+") 1 2)

(eval (list (symbol "+") 1 2))

(defn contextual-eval [ctx expr]
  (eval
    `(let [~@(mapcat (fn [[k v]]
                       [k `'~v])
                     ctx)]
       ~expr)))

(contextual-eval '{a 1 b 2} '(+ a b))


(contextual-eval '{a 1 b 3} '(+ a b))

(contextual-eval '{a 1 b (+ 3 4)} '(+ a b))

(contextual-eval {'b 2} '(- b))


(defn contextual-eval [ctx expr]
  (eval
    `(let [~@(mapcat (fn [[k v]]
                       [k v])
                     ctx)]
       ~expr)))


(defmacro do-until [& clauses]
  (when clauses
    (list 'clojure.core/when
          (first clauses)
          (if (next clauses)
            (second clauses)
            (throw (IllegalArgumentException. "do-util requires an even number of forms")))
          (cons 'do-until (nnext clauses)))))

(macroexpand-1 '(do-until true (prn 1) false (prn 2)))

(macroexpand '(do-until true (prn 1) false (prn 2)))

(walk/macroexpand-all '(do-until true (prn 1) false (prn 2)))

(do-until true (prn 1) false (prn 2))

(defmacro unless [condition & body]
  `(if (not ~condition)
     (do ~@body)))

(unless true (println "nope!"))

(unless false (println "true!"))

(defmacro def-watched [name & value]
  `(do
     (def ~name ~@value)
     (add-watch (var ~name)
                :reload
                (fn [~'key ~'r old# new#]
                  (println old# " -> " new#)))))


;;; a domain built as an example of what is a non-trivial, decent macro

(declare handle-things)

(defmacro domain [name & body]
  `{:tag :domain
    :attrs {:name (str '~name)}
    :content [~@body]})

(defmacro grouping [name & body]
  `{:tag :grouping
    :attrs {:name (str '~name)}
    :content [~@(handle-things body)]})

(declare grok-attrs grok-props)

(defn handle-things [things]
  (for [t things]
    {:tag :thing
     :attrs (grok-attrs (take-while (comp not vector?) t))
     :content (if-let [c (grok-props (drop-while (comp not vector?) t))]
                [c]
                [])}))






