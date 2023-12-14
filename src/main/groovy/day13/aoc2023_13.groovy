#!/usr/bin/env groovy
/**
 * --- Day 13: Point of Incidence ---
 * https://adventofcode.com/2023/day/13
 */
package day13

import static utils.Utils.getFile

class Day13 {
    final static Integer day = 13

    static void main(String[] args) {
        def patterns = loadPatterns(getFile(day))
        Long score = 0
        patterns.each {
            score += getReflection(transposePattern(it))
            score += getReflection(it) * 100
        }
        println score
    }

    static List<List<String>> loadPatterns(File inputFile) {
        List<List<String>> patterns = []
        inputFile.text.split(/\r?\n\r?\n/).each {
            patterns << it.split(/\r?\n/).toList()
        }
        return patterns
    }

    static Integer getReflection(List<String> pattern) {
        for (int i = 1; i < pattern.size(); i++) {
            def above = pattern[0..<i].reverse()
            def below = pattern[i..-1]
            def minLen = (above.size() < below.size()) ? above.size() : below.size()
            above = above[0..<minLen]
            below = below[0..<minLen]
            if (above == below) return i
        }
        return 0
    }

    static List<String> transposePattern(List<String> pattern) {
        return pattern.collect { it.toList() }.transpose().collect { it.join("") }
    }

}
