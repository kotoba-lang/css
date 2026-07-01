(ns css.core
  "CSS as EDN data."
  (:require [clojure.string :as str]))

(def unitless
  #{:animation-iteration-count :border-image-outset :border-image-slice
    :border-image-width :box-flex :box-flex-group :box-ordinal-group
    :column-count :columns :flex :flex-grow :flex-positive :flex-shrink
    :flex-negative :flex-order :grid-column :grid-row :font-weight
    :line-clamp :line-height :opacity :order :orphans :scale :tab-size
    :widows :z-index :zoom})

(defn css-name [x]
  (cond
    (keyword? x) (name x)
    (symbol? x) (name x)
    :else (str x)))

(defn value-str [prop v]
  (cond
    (nil? v) nil
    (number? v) (if (or (unitless prop) (zero? v)) (str v) (str v "px"))
    (vector? v) (str/join " " (keep #(value-str prop %) v))
    (keyword? v) (name v)
    :else (str v)))

(defn declarations [m]
  (str/join " "
            (keep (fn [[k v]]
                    (when-some [s (value-str k v)]
                      (str (css-name k) ": " s ";")))
                  m)))

(def style declarations)

(defn rule [selector decls]
  (str selector " { " (declarations decls) " }"))

(defn media [query rules]
  (str "@media " query " { "
       (str/join " " (for [[sel decls] rules] (rule sel decls)))
       " }"))

(defn keyframes
  [nm frames]
  (str "@keyframes " (css-name nm) " { "
       (str/join " " (for [[at decls] frames]
                       (str at "% { " (declarations decls) " }")))
       " }"))

(def kf keyframes)

(defn css
  [{:keys [rules keyframes] :as sheet}]
  (str/join "\n"
            (concat
             (for [[sel decls] rules] (rule sel decls))
             (for [[nm frames] keyframes] (keyframes nm frames))
             (for [[query rules] (:media sheet)] (media query rules)))))

(defn style-node [sheet]
  [:style [:hiccup/raw (css sheet)]])
