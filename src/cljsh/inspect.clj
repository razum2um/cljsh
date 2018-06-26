(ns cljsh.inspect
  (:require [clojure.pprint :refer [print-table]]
            [clojure.reflect :as r]))

(clojure.core/defn inspectable? [x]
  (and (-> x :name str (.contains "$") not)
       (contains? (:flags x) :public)))

(clojure.core/defn public-inspect [x]
  (print-table
    (filter inspectable?
      (map
       #(apply dissoc % [:exception-types :declaring-class])
        (:members (r/reflect x))))))
