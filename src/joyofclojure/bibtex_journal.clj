(ns joyofclojure.bibtex-journal
  (:require [clojure.string :as string]
            [clojure.java.io :as io])
  (:import (java.io Reader))
  (:import (org.jbibtex BibTeXDatabase
                        BibTeXEntry
                        BibTeXFormatter
                        BibTeXParser
                        BibTeXString
                        StringValue
                        Key
                        ParseException))
  (:import (java.util Map)))



(defn read-bibtex
  [file-name]
  (let [parser (BibTeXParser.)
        reader (io/reader file-name)
        database (.parse parser reader)
        objects (.getObjects database)]
    objects))

(defn alter-bibtex-entries
  [objects func]
  (doseq [obj objects]
    (when (instance? BibTeXEntry obj)
      (func obj)))
  objects)

(defn capitalize-author-and-title
  [entry]
  ((alter-field "author" big-capital) entry)
  ((alter-field "title" big-capital) entry))



(defn alter-field
  [field-name func]
  (fn [entry]
    (let [key (Key. field-name)]
      (when-let [field (.getField entry key)]
        (let [style (.getStyle field)
              strs (.toUserString field)
              new-strs (func strs)
              new-value (StringValue. new-strs style)]
          (doto entry
            (.removeField key)
            (.addField key new-value)))))))


(defn big-capital
  [the-str]
  (let [strs (string/split the-str #" ")
        excpt #{"a" "an" "on" "and" "for" "the" "of" "in"}]
    (string/join " "
                 (map
                   (fn [s]
                     (if (excpt s)
                       s
                       (apply str
                              (.toUpperCase (str (first s)))
                              (rest s))))
                   strs))))


(defn infuse-database
  [objects]
  (let [database (BibTeXDatabase.)]
    (doseq [obj objects]
      (.addObject database obj))
    database))

(defn formatting
  [database file-name]
  (with-open [writer (io/writer file-name)]
    (let [formatter (BibTeXFormatter.)]
      (.format formatter database writer))))



(-> (read-bibtex "D:/data/MyCollection.bib")
    (alter-bibtex-entries capitalize-author-and-title)
    infuse-database
    (formatting "D:/data/MyCollectionModified.bib"))

