(defproject sketch "1.0"
  :description "Generative Art"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [quil "3.1.0" :exclusions [org.clojure/clojure]]
                 [org.apache.commons/commons-math3 "3.3"]
                 [incanter "1.9.3"]]
  :jvm-opts ["-Xms1200m" "-Xmx1200M" "-server"]
  :source-paths ["src/clj"]
  :java-source-paths ["src/java"]
  :aot [sketch.dynamic]
  :main sketch.runcore)
