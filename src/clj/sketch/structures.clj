(ns sketch.structures
  (:require [quil.core :refer :all]
            [clojure.java.shell :refer [sh]]
            [sketch.calculations :as calc])
(:use [incanter.core :only [$=]])
(:use [clojure.math.combinatorics :only [combinations cartesian-product]])
(:use [clojure.pprint])
(:use [clojure.set :only [union]])
(:use [clojure.contrib.map-utils :only [deep-merge-with]])
(:import [org.apache.commons.math3.distribution ParetoDistribution])

(:import [processing.core PShape PGraphics]))

;; A namespace for all compatible data structures
;; Currently not used and exists as a start to reorganizing the app

;; Used as a global means to give unique IDs to new paths
(def pathIDCounter (atom 0))

;; Used as a global means to give unique IDs to new nodes
(def nodeIDCounter (atom 0))

;; ------------ Paths ---------------
(defrecord Path [ID nodes settings data])

(defrecord PathData [is-fixed
                     is-end
                     is-closed
                     to-remove
                     is-random
                     velocity
                     next-position
                     bounds
                     brownian
                     alignment
                     node-injection-interval
                     is-mature
                     is-divide-ready])

(def default-path-data
  "Data to be included with a path not provided with any"
  (hash-map :age 0))

(def default-path-settings
  "Settings to be included with a path not provided with any"
  (hash-map 
   :is-closed false
   :fill-color nil
   :stroke-color nil
   :draw-edges true
   :draw-nodes false
   :draw-fixed-nodes false
   :draw-all-random-injections? false
   :draw-new-random-injections? false
   :bug-finder-mode? true
   :uniform-node-settings? false))


;; ------------ Nodes ---------------
(defrecord Node [ID position settings data])

(defrecord NodeData [next-position age velocity])

(defrecord Position2D [x y])

(defrecord Position3D [x y z])

(def default-node-data
  "Data to be included with a node not provided with any"
  (hash-map
   :age 0
   :is-fixed true
   :is-end false
   :is-random false))

(def default-node-settings
  "default setting to be included with a node where settings is set to nil"
  (hash-map
   :fill-color nil
   :stroke-color nil
   :draw-edges true
   :draw-nodes true
   :draw-fixed-nodes false
   :draw-all-random-injections? false
   :bug-finder-mode? true
   :uniform-node-settings? false))



