(clojure.core/ns cljsh.utils)

(clojure.core/defn clsp [] (seq (.getURLs (java.lang.ClassLoader/getSystemClassLoader))))

(clojure.core/defn file-exists-under [fname under] (let [f (clojure.java.io/file under fname)] (if (.exists f) f)))

(clojure.core/defn file-in-clsp [fname] (->> (clsp) (filter (fn [uri] (-> uri .getProtocol (= "file")))) (some (partial file-exists-under fname))))

(clojure.core/defn var->sym-filename [v] (let [sym (.sym v) ns (.name (.ns v)) fname (str (clojure.string/replace (clojure.string/replace ns #"\." "/") #"-" "_") ".clj")] (or (file-in-clsp fname) (str "src/" fname))))