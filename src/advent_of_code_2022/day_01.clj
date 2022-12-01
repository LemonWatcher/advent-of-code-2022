(ns advent-of-code-2022.day-01
  "Advent of Code 2022, Day 1: Calorie Counting"
  (:require [clojure.test :refer :all]
            [clojure.java.io :refer [resource]]
            [clojure.string :refer [split-lines blank?]]
            [criterium.core :refer [quick-bench]])
  (:import [java.util Collections]))

;; # Part One
;;
;; The Elves take turns writing down the number of Calories contained
;; by the various meals, snacks, rations, etc. that they've brought
;; with them, one item per line. Each Elf separates their own
;; inventory from the previous Elf's inventory (if any) by a blank
;; line.
;;
;; For example, suppose the Elves finish writing their items' Calories
;; and end up with the following list:
;;
;;     1000
;;     2000
;;     3000
;;
;;     4000
;;
;;     5000
;;     6000
;;
;;     7000
;;     8000
;;     9000
;;
;;     10000
;;
;; This list represents the Calories of the food carried by five Elves:

(def input
  "Read calorie data from the input file."
  (->> "day_01.txt"
       resource
       slurp
       split-lines
       (partition-by blank?)
       (remove #{'("")})
       (map (partial map parse-long))
       delay))

;; The first Elf is carrying food with 1000, 2000, and 3000 Calories, a total of 6000 Calories.
;; The second Elf is carrying one food item with 4000 Calories.
;; The third Elf is carrying food with 5000 and 6000 Calories, a total of 11000 Calories.
;; The fourth Elf is carrying food with 7000, 8000, and 9000 Calories, a total of 24000 Calories.
;; The fifth Elf is carrying one food item with 10000 Calories.

(def test-inventories
  [[1000 2000 3000]
   [4000]
   [5000 6000]
   [7000 8000 9000]
   [10000]])

(defn total-calories
  "Calculate the total calories for each Elf."
  [inventories]
  (map (partial reduce +) inventories))

(deftest test-total-calories
  (is (= [6000 4000 11000 24000 10000] (total-calories test-inventories))))

;; In case the Elves get hungry and need extra snacks, they need to
;; know which Elf to ask: they'd like to know how many Calories are
;; being carried by the Elf carrying the most Calories. In the example
;; above, this is 24000 (carried by the fourth Elf).

(defn best-stocked-elf
  "Returns the highest total calories in an Elf's inventory."
  [inventories]
  (reduce max (total-calories inventories)))

;; Find the Elf carrying the most Calories. How many total Calories is
;; that Elf carrying?

(deftest best-stocked-elf-test
  (is (= 70509 (best-stocked-elf @input))))

;; # Part Two
;;
;; By the time you calculate the answer to the Elves' question,
;; they've already realized that the Elf carrying the most Calories of
;; food might eventually run out of snacks.
;;
;; To avoid this unacceptable situation, the Elves would instead like
;; to know the total Calories carried by the top three Elves carrying
;; the most Calories. That way, even if one of those Elves runs out of
;; snacks, they still have two backups.

(defn top
  "Returns the highest `n` entries in `coll`. Slow sort-based implementation."
  [n coll]
  (take n (sort (Collections/reverseOrder) coll)))

(defn best-stocked-elves
  "Returns the totals of the three Elves carrying the most calories."
  [inventories]
  (top 3 (total-calories inventories)))

;; In the example above, the top three Elves are the fourth Elf (with
;; 24000 Calories), then the third Elf (with 11000 Calories), then the
;; fifth Elf (with 10000 Calories). The sum of the Calories carried by
;; these three elves is 45000.

(deftest best-stocked-elves-test
  (is (= [24000 11000 10000] (best-stocked-elves test-inventories))))

;; Find the top three Elves carrying the most Calories. How many
;; Calories are those Elves carrying in total?

(deftest available-calories-test
  (is (= 208567 (reduce + (best-stocked-elves @input)))))
