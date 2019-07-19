(defproject cljsh "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-beta1"] [rewrite-clj "0.6.1"] [com.cemerick/pomegranate "0.4.0" :exclusions [org.slf4j/slf4j-api org.tcrawley/dynapath]] [razum2um/dynapath "1.0.1"] [cljfmt "0.6.4" :exclusions [rewrite-clj]] [aprint "0.1.3"] [slamhound "1.5.5"]])
