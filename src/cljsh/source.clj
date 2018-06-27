(ns cljsh.source
  (:require [cljsh.postclj :as postclj]
            [cljsh.utils :as utils]
            [clojure.java.io :as io]
            [clojure.zip :as z]))

(clojure.core/defn expandable? [loc]
  (let [n (z/node loc)] (and (symbol? n) (.getNamespace n))))

(clojure.core/defn append!
  ([v] (append! v (-> v meta :code)))
  ([v code]
    (let [sym-filename (utils/var->sym-filename v)]
      (io/make-parents sym-filename)
      (spit sym-filename "\n\n" :append true)
      (spit
        sym-filename
        (-> code postclj/pprint with-out-str)
        :append
        true))))

(clojure.core/defn macro->code [macro-quote]
  (let [macro-vquote (vec macro-quote) n (dec (count macro-vquote))]
    (seq (assoc macro-vquote n (-> macro-vquote (nth n) eval)))))

(defmacro replace-code [code replace? replace!]
  `(clojure.core/loop [loc# (clojure.zip/seq-zip
                             (clojure.core/seq ~code))]
    (if (clojure.zip/end? loc#)
      (clojure.zip/root loc#)
      (recur
        (clojure.zip/next
          (if (~replace? loc#) (~replace! loc#) loc#))))))


(clojure.core/defn expand-symbol-ns-inside-var [sym v]
  (let [var-ns (-> v .ns)
        alias-sym-ns (-> sym .getNamespace symbol)
        sym-ns (some-> var-ns .getAliases (get alias-sym-ns) ns-name)]
    (symbol (str (or sym-ns alias-sym-ns)) (.getName sym))))

(clojure.core/defn source [v]
  (or
    (-> v meta :code)
    (let [ns (some-> v .ns .name (str "/"))]
      (some->
        (str ns (.sym v))
        symbol
        clojure.repl/source-fn
        read-string))))

(clojure.core/defn source-expand-ns [v]
  (-> v
   source
   (replace-code
     expandable?
     #(z/edit % expand-symbol-ns-inside-var v))))