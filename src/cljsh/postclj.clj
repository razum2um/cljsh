(ns cljsh.postclj
  (:require [clojure.pprint :as pprint]
            [clojure.zip :as z]))

(clojure.core/defn pprint [code]
  (pprint/with-pprint-dispatch
    pprint/code-dispatch
    (pprint/pprint code)))

(clojure.core/defn coll-zip [root]
  (z/zipper
   sequential?
   seq
   (fn [node children]
     (let [coll (if (vector? node) (vec children) children)]
       (with-meta coll (meta node))))
   root))

(clojure.core/defn replace-sym [code match replacement]
  (loop [loc (coll-zip code)]
    (if (z/end? loc)
      (z/root loc)
      (recur
        (z/next
          (if (-> loc z/node (= match))
            (z/replace loc replacement)
            loc))))))
