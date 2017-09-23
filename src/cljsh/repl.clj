(clojure.core/ns cljsh.repl)

(clojure.core/defn walk1 [inner outer form] (cond (list? form) (with-meta (outer (apply list (map inner form))) (meta form)) (instance? clojure.lang.IMapEntry form) (with-meta (outer (vec (map inner form))) (meta form)) (seq? form) (with-meta (outer (doall (map inner form))) (meta form)) (instance? clojure.lang.IRecord form) (with-meta (outer (reduce (fn [r x] (conj r (inner x))) form form)) (meta form)) (coll? form) (with-meta (outer (into (empty form) (map inner form))) (meta form)) :else (outer form)))

(clojure.core/defn prewalk1 [f form] (walk1 (partial prewalk1 f) identity (f form)))

(clojure.core/defn var->sym-filename [v] (let [sym (.sym v) ns (.name (.ns v))] (str "src/" (clojure.string/replace ns #"\." "/") ".clj")))

(clojure.core/defn rewrite [v] (when-let [upd (some-> v var->sym-filename z/of-file (z/find-token z/next (fn [n] (and (-> n z/sexpr #{(quote defn) (quote clojure.core/defn) (quote defnc)}) (-> n z/right z/sexpr (= (.sym v)))))) z/up (z/replace (-> v meta :code)))] (spit (-> v var->sym-filename) (z/root-string upd)) (z/sexpr upd)))

(clojure.core/defn save [v] (let [sym-filename (->> v var->sym-filename)] (clojure.java.io/make-parents sym-filename) (when-not (-> sym-filename clojure.java.io/file .exists) (spit sym-filename (-> v meta :ns-code))) (when-not (rewrite v) (spit sym-filename "\n\n" :append true) (spit sym-filename (-> v meta :code) :append true))))

(clojure.core/defn without-line-meta [s] (if (instance? clojure.lang.IMeta s) (vary-meta s (fn [m] (apply dissoc m [:line :column]))) s))
(defmacro defnc [name & body] `(do (defn ~name ~@body) (.setMeta (var ~name) (assoc (meta (var ~name)) :ns-code (list 'ns (.name (.ns (var ~name)))) :code (list 'defn (.sym (var ~name)) ~@(map (fn [x] (list 'quote (prewalk1 without-line-meta x))) body))))))