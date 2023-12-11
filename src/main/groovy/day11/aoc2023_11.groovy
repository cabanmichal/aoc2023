#!/usr/bin/env groovy
/**
 * --- Day 11: Cosmic Expansion ---
 * https://adventofcode.com/2023/day/11
 */
package day11

import groovy.transform.Canonical

import static utils.Utils.getFile

class Day11 {
    final static Integer day = 11

    static void main(String[] args) {
        def grid = loadGrid(getFile(day))

        [2, 1_000_000].each {
            List<List<Position>> universe = getExpandedUniverse(grid, it)
            List<Position> galaxies = getGalaxies(grid, universe)
            println calculateTotalDistance(galaxies)  // 9647174, 377318892554
        }
    }

    static List<List<String>> loadGrid(File file) {
        List<List<String>> grid = []
        file.eachLine { grid << it.toList() }

        return grid
    }

    static List<List<Position>> getExpandedUniverse(List<List<String>> grid, int factor) {

        List<Integer> colGalaxyCounter = [0] * grid[0].size()
        List<Integer> rowGalaxyCounter = [0] * grid.size()
        List<List<Position>> positionGrid = []

        // build the universe and identify rows/columns without galaxies
        grid.eachWithIndex { List<String> row, int i ->
            List<Position> positionsInRow = []

            row.eachWithIndex { String entry, int j ->
                positionsInRow << new Position(row: i, column: j)

                if (entry == "#") {
                    colGalaxyCounter[j]++
                    rowGalaxyCounter[i]++
                }
            }

            positionGrid << positionsInRow
        }

        // expand the universe
        int colExpansion = 0
        int rowExpansion = 0

        positionGrid.eachWithIndex { List<Position> row, int i ->
            if (rowGalaxyCounter[i] == 0) rowExpansion += factor - 1

            colExpansion = 0
            row.eachWithIndex { Position entry, int j ->
                if (colGalaxyCounter[j] == 0) colExpansion += factor - 1
                entry.row += rowExpansion
                entry.column += colExpansion
            }
        }

        return positionGrid
    }

    static List<Position> getGalaxies(List<List<String>> grid, List<List<Position>> universe) {
        List<Position> galaxies = []
        grid.eachWithIndex { List<String> row, int i ->
            row.eachWithIndex { String entry, int j -> if (entry == "#") galaxies << universe[i][j]
            }
        }

        return galaxies
    }

    static List<List<Position>> pairsOfGalaxies(List<Position> galaxies) {
        List<List<Position>> galaxyPairs = []
        for (int i = 0; i < galaxies.size() - 1; i++) {
            for (int j = i + 1; j < galaxies.size(); j++) {
                galaxyPairs << [galaxies[i], galaxies[j]]
            }
        }

        return galaxyPairs
    }

    static Long calculateTotalDistance(List<Position> galaxies) {
        Long totalDistance = 0
        for (def pair : pairsOfGalaxies(galaxies)) {
            def (first, second) = pair

            Long rowDiff = Math.abs(first.row - second.row)
            Long colDiff = Math.abs(first.column - second.column)

            totalDistance += rowDiff + colDiff
        }

        return totalDistance
    }
}

@Canonical
class Position {
    Long row
    Long column
}
