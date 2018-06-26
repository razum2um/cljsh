(clojure.core/ns cljsh.complement
  (:require [compliment.sources.namespaces-and-classes :as namespaces-and-classes]
            [robert.hooke :as hooke]))

(clojure.core/defn always-complete-short-class [f x] (or (f x) :root))

(clojure.core/defn patch []
  (hooke/add-hook
    #'namespaces-and-classes/analyze-import-context
    #'always-complete-short-class))
