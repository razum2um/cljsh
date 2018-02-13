(ns cljsh.postclj
  (:require [clojure.pprint :as pprint]))

(clojure.core/defn pprint [code] (pprint/with-pprint-dispatch pprint/code-dispatch (pprint/pprint code)))