(ns joyofclojure.chapter3-graphics)

(defn xors [max-x max-y]
  (for [x (range max-x)
        y (range max-y)]
    [x y (bit-xor x y)]))

(def frame (java.awt.Frame.))

(for [meth (.getMethods java.awt.Frame)
      :let [name (.getName meth)]
      :when (re-find #"Vis" name)]
  name)

(.setVisible frame true)

(defn ajust-frame
  [frame]
  (doto frame
    (.setVisible true)
    (.setSize (java.awt.Dimension. 200 200))))

(def gfx (.getGraphics frame))

(defn draw-gfw
  [gfw]
  (doto gfw
    (.fillRect 100 100 50 75)
    (.setColor (java.awt.Color. 255 128 0))
    (.fillRect 100 150 75 50)))