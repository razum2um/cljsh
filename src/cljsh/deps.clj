(clojure.core/ns cljsh.deps)

(clojure.core/defn add-dep-to-project [dep-sym] (-> "project.clj" z/of-file (z/find-value z/next :dependencies) z/next (z/append-child (with-meta dep-sym {})) z/root-string (->> (spit "project.clj"))))

(clojure.core/defn clsp! [x] (cemerick.pomegranate/add-dependencies :coordinates x :repositories (merge cemerick.pomegranate.aether/maven-central {"clojars" "http://clojars.org/repo"})))

(clojure.core/defn clsp-release! [dep-name] (let [resolved-deps (clsp! [[dep-name "RELEASE"]])] (->> resolved-deps keys (filter (fn [coordinate] (-> coordinate first (= dep-name)))) first)))

(clojure.core/defn install! [dep-name] (-> dep-name clsp-release! add-dep-to-project))