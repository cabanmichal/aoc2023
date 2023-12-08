#!/usr/bin/env groovy
/**
 * --- Day 3: Gear Ratios ---
 * https://adventofcode.com/2023/day/3
 */
package day03

import groovy.transform.Immutable

import static utils.Utils.getFile

class Day03 {
    final static Integer day = 3

    static void main(String[] args) {
        Schematic schematic = new Schematic(getFile(day))

        println schematic.partNumbers.findAll { it.isValidPartNumber() }.sum { it.value }  // 536576
        println schematic.parts.sum { it.gearRatio() }  // 75741499
    }
}


class Schematic {
    List<PartNumber> partNumbers = []
    List<Part> parts = []
    private List<List<SchemeEntry>> schematic = []

    Schematic(File schematicFile) {
        loadSchematic(schematicFile)
        loadPartNumbers()
        loadParts()
        connectPartsAndPartNumbers()
    }

    private void loadSchematic(File schematicFile) {
        schematicFile.eachWithIndex { String line, int row ->

            List<SchemeEntry> sameRowEntries = []

            line.eachWithIndex { String chr, int col ->
                Position position = new Position(row: row, column: col)
                sameRowEntries << new SchemeEntry(rawValue: chr, position: position)
            }

            schematic << sameRowEntries
        }
    }

    private void loadPartNumbers() {
        schematic.each { List<SchemeEntry> entries ->

            List<SchemeEntry> numberEntries = []
            String number = ""

            entries.each { SchemeEntry entry ->
                if (entry.rawValue.isNumber()) {
                    number += entry.rawValue
                    numberEntries << entry
                }
                if ((!entry.rawValue.isNumber() || entry.position.column == entries.size() - 1)
                        && number) {
                    PartNumber partNumber = new PartNumber()
                    partNumber.value = number.toInteger()
                    partNumber.positions.addAll(numberEntries.collect { it.position })

                    numberEntries.each {
                        entry.partNumber = partNumber
                    }

                    partNumbers << partNumber

                    number = ""
                    numberEntries.clear()
                }
            }
        }
    }

    private void loadParts() {
        schematic.each { List<SchemeEntry> entries ->
            entries.each { SchemeEntry entry ->
                if (!entry.rawValue.isNumber() && entry.rawValue != ".") {
                    Part part = new Part(value: entry.rawValue, position: entry.position)
                    entry.part = part
                    parts << part
                }
            }
        }
    }

    private void connectPartsAndPartNumbers() {
        for (PartNumber partNumber : partNumbers) {
            for (Position position : partNumber.getAdjacentPositions(schematic.size(), schematic[0].size())) {
                SchemeEntry entry = schematic[position.row][position.column]
                if (entry.part) {
                    entry.part.addPartNumber(partNumber)
                    partNumber.addPart(entry.part)
                }
            }
        }
    }

}

class PartNumber {
    int value
    List<Position> positions = []
    private List<Part> parts = []

    void addPart(Part part) {
        if (!parts.contains(part)) {
            parts << part
        }
    }

    boolean isValidPartNumber() {
        return parts.size() > 0
    }

    List<Position> getAdjacentPositions(int maxRow, int maxColumn) {
        Set<Position> adjacentPositions = []
        for (Position position : positions ) {
            for (Position adjacent : position.getAdjacentPositions(maxRow, maxColumn)) {
                if (adjacent != position) {
                    adjacentPositions.add(adjacent)
                }
            }
        }

        return adjacentPositions.toList()
    }
}


class Part {
    String value
    Position position
    private List<PartNumber> partNumbers = []

    boolean isGear() {
        return value == "*" && partNumbers.size() == 2
    }

    int gearRatio() {
        if (isGear()) {
            return partNumbers.inject(1, { prod, num -> prod *= num.value })
        }
        return 0
    }

    void addPartNumber(PartNumber partNumber) {
        if (!partNumbers.contains(partNumber)) {
            partNumbers << partNumber
        }
    }
}


class SchemeEntry {
    String rawValue
    PartNumber partNumber
    Part part
    Position position
}

@Immutable
class Position {
    int row
    int column

    List<Position> getAdjacentPositions(int maxRow, int maxColumn) {
        List<Position> positions = []
        List<Integer> diffs = [-1, 0, 1]

        for (int dRow in diffs) {
            int nRow = row + dRow
            if (nRow < 0 || nRow >= maxRow) {
                continue
            }
            for (int dCol in diffs) {
                int nCol = column + dCol
                if (nCol < 0 || nCol >= maxColumn) {
                    continue
                }
                if (dRow || dCol) {
                    positions << new Position(row: nRow, column: nCol)
                }
            }
        }

        return positions
    }
}