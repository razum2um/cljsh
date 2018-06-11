(clojure.core/ns cljsh.utils)

(clojure.core/defn clsp []
  (for
    [prop
     (filter
       #(System/getProperty %)
       ["sun.boot.class.path" "java.ext.dirs" "java.class.path"])
     path
     (.split (System/getProperty prop) java.io.File/pathSeparator)]
    path))

(clojure.core/defn file-exists-under [fname under]
  (let [f (clojure.java.io/file under fname)] (if (.exists f) f)))

(clojure.core/defn file-in-clsp [fname]
  (->>
    (clsp)
    (some (partial file-exists-under fname))))

(clojure.core/defn var->sym-filename [v]
  (let [sym (.sym v)
        ns (.name (.ns v))
        fname (str
                (clojure.string/replace
                  (clojure.string/replace ns #"\." "/")
                  #"-"
                  "_")
                ".clj")]
    (or (file-in-clsp fname) (str "src/" fname))))
