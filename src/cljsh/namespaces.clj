(ns cljsh.namespaces
  (:require [cljsh.utils :as utils]
            [slam.hound :as hound]))



(clojure.core/defn rewrite-ns [v]
  (-> v utils/var->sym-filename hound/swap-in-reconstructed-ns-form))