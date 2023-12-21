#!/usr/bin/env groovy
/**
 * --- Day 21: Step Counter ---
 * https://adventofcode.com/2023/day/21
 */
package day21


import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TupleConstructor

import static utils.Utils.getFile

class Day21 {
    final static Integer day = 21

    static void main(String[] args) {
        def map = loadMap(getFile(day))
        Position start = null
        map.eachWithIndex { List<String> row, int r ->
            row.eachWithIndex { String entry, int c ->
                if (entry == "S") {
                    start = [r, c] as Position
                }
            }
        }

        // part 1
        println makeSteps(map, 64, start).size()  // 3841

    }

    static List<Position> makeSteps(List<List<String>> map, Integer steps, Position start) {
        List<Position> positions = [start]
        while (steps) {
            steps--
            positions = positions
                    .collectMany { it.neighbours }
                    .findAll {
                        it.r >= 0 && it.r < map.size()
                                && it.c >= 0 && it.c < map[it.r].size()
                                && map[it.r][it.c] != "#"
                    }
                    .toUnique { p1, p2 -> p1.r == p2.r ? p1.c <=> p2.c : p1.r <=> p2.r }
        }
        return positions
    }

    static List<List<String>> loadMap(File file) {
        List<List<String>> map = []
        file.eachLine { map << it.toList() }
        return map
    }
}

@EqualsAndHashCode
@ToString(excludes = "neighbours")
@TupleConstructor
class Position {
    Integer r, c

    List<Position> getNeighbours() {
        List<Position> neighbours = []
        [[-1, 0], [0, 1], [1, 0], [0, -1]].each {
            neighbours << ([r + it[0], c + it[1]] as Position)
        }

        return neighbours
    }
}
