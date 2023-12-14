#!/usr/bin/env groovy
/**
 * --- Day 14: Parabolic Reflector Dish ---
 * https://adventofcode.com/2023/day/14
 */
package day14

import static utils.Utils.getFile

class Day14 {
    final static Integer day = 14

    static void main(String[] args) {
        List<List<String>> rocks

        // part 1 - naive approach not suitable for part 2
        rocks = loadRocks(getFile(day))
        slideNorth(rocks)
        println calculateNorthLoad(rocks)  // 109755
    }

    static List<List<String>> loadRocks(File file) {
        List<List<String>> rocks = []
        file.eachLine { rocks << it.toList() }
        return rocks
    }

    static Integer calculateNorthLoad(List<List<String>> rocks) {
        def totalLoad = 0

        rocks.transpose().each {
            def row = it as List<String>
            row.reverse().eachWithIndex { String stone, int i ->
                if (stone == "O") totalLoad += i + 1
            }
        }

        return totalLoad
    }

    static List<String> rollRocks(List<String> rocks) {
        rocks = rocks.collect()  // make a copy
        def wall = 0
        for (int i = 0; i < rocks.size(); i++) {
            if (rocks[i] == "O") {
                if (wall != i) {
                    rocks[wall] = "O"
                    rocks[i] = "."
                }
                wall++
            }
            if (rocks[i] == "#") {
                wall = i + 1
            }
        }
        return rocks
    }

    static void slideNorth(List<List<String>> rocks) {
        rocks.transpose().eachWithIndex { def row, int c ->
            def rolled = rollRocks(row as List<String>)
            rolled.eachWithIndex{ String stone, int r ->
                rocks[r][c] = stone
            }
        }
    }

    static void slideWest(List<List<String>> rocks) {
        rocks.eachWithIndex { def row, int r ->
            def rolled = rollRocks(row as List<String>)
            rolled.eachWithIndex{ String stone, int c ->
                rocks[r][c] = stone
            }
        }
    }

    static void slideSouth(List<List<String>> rocks) {
        rocks.transpose().eachWithIndex { def row, int c ->
            def rolled = rollRocks((row as List<String>).reverse())
            rolled.eachWithIndex{ String stone, int r ->
                rocks[rocks.size() - r - 1][c] = stone
            }
        }
    }

    static void slideEast(List<List<String>> rocks) {
        rocks.eachWithIndex { def row, int r ->
            def rolled = rollRocks((row as List<String>).reverse())
            rolled.eachWithIndex{ String stone, int c ->
                rocks[r][row.size() - c - 1] = stone
            }
        }
    }

    static void cycle(List<List<String>> rocks) {
        slideNorth(rocks)
        slideWest(rocks)
        slideSouth(rocks)
        slideEast(rocks)
    }
}

