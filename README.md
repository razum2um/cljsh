# cljsh

A library which makes your REPL the best IDE ever and promotes totally "editorless" approach.

The ambitious goal is eliminate the code editor at all. REPL should be full autonomous: any declaration should be properly synchronized into the filesystem including proper dependency tree. The runtime is our friend. The code should be self-aware.

In the future you'll start a repl (even without a project), start hacking, compose your `-main` function and *just* have your project in the fs properly saved, ready to be compiled to jar :)

## Done

- `defnc` (define-code) macro allows you preserve fn definition
- `save` fn dumps the code and updates it properly

## Usage

```sh
(ns cljsh.example)
(use 'cljsh.repl)

;; define fns using this macro instean of defn
(defnc f1 [] (println (str "Any crazy code" (or true "even macros" "(you know 'or' is a macro)"))))
(defnc f2 [] (println (str "Now use f1:" (f1))))

;; at some point just save it to the filesystem
(-> f1 var save)
(-> f2 var save) ;; no dependency management right now, sorry, note the dependency-first order

;; now hack on any fn
(defnc f1 [] "Refactored")

;; and just save the function again!
(-> f1 var save)

;; check filesystem contents:
(-> f2 var var->sym-filename slurp println)
```

the output and the content of `src/cljsh/example.clj` should be like this:

```clj
(clojure.core/ns cljsh.example)

(clojure.core/defn f1 [] "Refactored")

(clojure.core/defn f2 [] (println (str "Now use f1:" (f1))))
```

Of course this ns is ready to be used. Check like this:

```
(require 'cljsh.example :reload)
(f2)
;; This will print:
;; Now use f1:Refactored
```

## Known limitations

Reader macros are written already expanded by repl reader (e.g. `#(pr %)` -> `(fn* [foo#] (pr foo#))`, also `'` -> `quote`, `#'` -> `var`).
Use function, not reader macros to get pretty dumped fns (expanded are saved also correctly, but look ugly)

## Is this any good?

Yes. This library is being developed *absolutely without any editor*.

I started with `clojure` and `com.cemerick/pomegranate` adding dependencies on the fly. Nothing else. Even no shell commands (used `lucid.git` to commit & push)!

## License

Copyright © 2017 Vlad Bokov

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
