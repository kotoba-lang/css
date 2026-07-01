(ns css.core-test
  (:require [clojure.string :as str]
            [clojure.test :refer [deftest is]]
            [css.core :as css]))

(deftest renders-values
  (is (= "border-radius: 12px; opacity: 0.8; padding: 12px 22px; color: white;"
         (css/style {:border-radius 12 :opacity 0.8 :padding [12 22] :color :white}))))

(deftest renders-rules-and-keyframes
  (is (= ".card { color: white; }" (css/rule ".card" {:color :white})))
  (is (= "@keyframes fade { 0% { opacity: 0; } 100% { opacity: 1; } }"
         (css/kf :fade [[0 {:opacity 0}] [100 {:opacity 1}]]))))

(deftest renders-sheet
  (let [sheet (css/css {:rules {".card" {:padding 12}}
                        :media {"(max-width: 600px)" {".card" {:padding 8}}}})]
    (is (str/includes? sheet ".card { padding: 12px; }"))
    (is (str/includes? sheet "@media (max-width: 600px)"))))

(deftest operator-theme-is-non-empty
  (is (map? css/operator-theme))
  (is (pos? (count css/operator-theme)))
  (is (contains? css/operator-theme "body"))
  (is (contains? css/operator-theme ".card"))
  (is (contains? css/operator-theme ".ok")))

(deftest merge-theme-layers-domain-rules
  (let [sheet (css/merge-theme {"x-special" {:color :red}})]
    (is (contains? sheet :rules))
    ;; base theme rules survive
    (is (contains? (:rules sheet) "body"))
    ;; domain rule layered on top
    (is (contains? (:rules sheet) "x-special"))))

(deftest merge-theme-domain-overrides-base
  (let [sheet (css/merge-theme {"body" {:background "#000"}})
        body-rule (get-in sheet [:rules "body"])]
    (is (= "#000" (:background body-rule)))))
