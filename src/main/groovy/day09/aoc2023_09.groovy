#!/usr/bin/env groovy
/**
 * --- Day 9: Mirage Maintenance ---
 * https://adventofcode.com/2023/day/9
 */
package day09

import static utils.Utils.getFile

class Day09 {
    final static Integer day = 9

    static void main(String[] args) {
        Integer sumOfNextValues = 0
        Integer sumOfPrevValues = 0

        getFile(day).eachLine { String line ->
            Tuple2 values = getPrevAndNextValue(line.split().collect {it.toInteger()})
            sumOfPrevValues += values[0]
            sumOfNextValues += values[1]
        }

        println sumOfNextValues  // 1782868781
        println sumOfPrevValues  // 1057
    }

    static Tuple2<Integer, Integer> getPrevAndNextValue(List<Integer> values) {
        List<Integer> firstValues = []
        List<Integer> diffs = values.collect()
        Integer nextValue = 0

        while (!diffs.every {it == 0 }) {
            firstValues << diffs[0]
            nextValue += diffs[-1]
            List<Integer> newDiffs = []

            for (int i = 0; i < diffs.size() - 1; i++) {
                newDiffs << diffs[i+1] - diffs[i]
            }

            diffs = newDiffs.collect()
        }

        Integer prevValue = 0
        firstValues.reverseEach {prevValue = it - prevValue }

        return new Tuple2(prevValue, nextValue)
    }

}
