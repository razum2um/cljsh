(ns cljsh.namespaces
  (:require [cljsh.utils :as utils]
            [slam.hound :as hound])
  (:import (clojure.lang LispReader)))



(clojure.core/defn rewrite-ns [v]
  (-> v utils/var->sym-filename hound/swap-in-reconstructed-ns-form))

(clojure.core/defn try-read [rdr]
  (try
    (LispReader/read rdr nil)
    (catch RuntimeException _ :cljsh.namespaces/nil)))