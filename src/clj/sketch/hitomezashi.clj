(ns sketch.hitomezashi
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]]
            [sketch.divider :as divider]
            [sketch.color :as color])
  (:use [incanter.core :only [$=]])
  (:use [clojure.math.combinatorics :only [combinations cartesian-product]])
  (:use [clojure.pprint])
  (:use [clojure.set :only [union]])
  (:use [clojure.contrib.map-utils :only [deep-merge-with]])
  (:import [org.apache.commons.math3.distribution ParetoDistribution])

  (:import [processing.core PShape PGraphics]))

()