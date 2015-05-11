(ns joyofclojure.chapter3)


(defn destructure-regex-matcher
  [date-string]
  (let [date-regex #"(\d{1,2})\/(\d{1,2})\/(\d{3,4})"
        rem (re-matcher date-regex date-string)]
    (when (.find rem)
      (let [[_ m d y] rem]
        {:month m :day d :year y}))))

