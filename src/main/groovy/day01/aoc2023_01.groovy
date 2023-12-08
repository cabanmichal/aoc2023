#!/usr/bin/env groovy
/**
 * --- Day 1: Trebuchet?! ---
 * https://adventofcode.com/2023/day/1
 */
package day01

import java.util.regex.Pattern

import static utils.Utils.getFile

class Day01 {
    final static Integer day = 1
    final static Pattern pattern1 = ~/(\d)/
    final static Pattern pattern2 = ~/(?=(one|two|three|four|five|six|seven|eight|nine|\d))/

    static void main(String[] args) {
        File inputFile = getFile(day)
        println solve(inputFile, pattern1)  // 55172
        println solve(inputFile, pattern2)  // 54925
        assert solveLine("fivezg8jmf6hrxnhgxxttwoneg", pattern2) == 51
    }

    static int solveLine(String line, Pattern pattern) {
        Map map = [one  : "1",
                   two  : "2",
                   three: "3",
                   four : "4",
                   five : "5",
                   six  : "6",
                   seven: "7",
                   eight: "8",
                   nine : "9"]

        List numbers = (line =~ pattern).collect { map.get(it[1] as String, it[1] as String) }
        String number = numbers[0] + numbers[-1]

        return number.toInteger()
    }

    static int solve(File inputFile, Pattern pattern) {

        int total = 0
        inputFile.eachLine {
            int value = solveLine(it, pattern)
            total += value
        }

        return total
    }
}
