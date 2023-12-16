#!/usr/bin/env groovy
/**
 * --- Day 16: The Floor Will Be Lava ---
 * https://adventofcode.com/2023/day/16
 */
package day16

import groovy.transform.EqualsAndHashCode
import groovy.transform.Immutable

import static utils.Utils.getFile

class Day16 {
    final static Integer day = 16

    static void main(String[] args) {
        List<List<String>> contraption = []
        getFile(day).eachLine { contraption << it.toList() }

        // part 1
        println countEnergized(energize(([0, 0, 0, 1] as Beam), contraption))  // 7434

        // part 2
        Integer rMin, rMax, cMin, cMax
        rMin = 0
        cMin = 0
        rMax = contraption.size() - 1
        cMax = contraption[rMin].size() - 1
        List<Integer> countsOfEnergized = []

        (cMin..cMax).each {  // top and bottom row
            countsOfEnergized << countEnergized(energize(([rMin, it, 1, 0] as Beam), contraption))
            countsOfEnergized << countEnergized(energize(([rMax, it, -1, 0] as Beam), contraption))
        }

        (rMin..rMax).each {  // left and right column
            countsOfEnergized << countEnergized(energize(([it, cMin, 0, 1] as Beam), contraption))
            countsOfEnergized << countEnergized(energize(([it, cMax, 0, -1] as Beam), contraption))
        }

        println countsOfEnergized.max()  // 8183
    }

    static Set<Beam> energize(Beam init, final List<List<String>> contraption) {
        Set<Beam> beams = [init]
        Set<Beam> seen = []

        while (beams) {
            Beam beam = beams.first()
            beams.remove(beam)

            while (beam.r >= 0 && beam.r < contraption.size()
                    && beam.c >= 0 && beam.c < contraption[0].size()
                    && !(beam in seen)) {

                seen.add(beam)
                String part = contraption[beam.r][beam.c]
                beam = beam.move(part, beams)
            }
        }

        return seen
    }

    static Integer countEnergized(Set<Beam> beams) {
        return beams.collect { [it.r, it.c] }.toSet().size()
    }
}


@Immutable
@EqualsAndHashCode
class Beam {
    Integer r
    Integer c
    Integer dr
    Integer dc

    Beam move(String contraptionPart, Set<Beam> beams) {
        if (contraptionPart == "-") {
            if (dc == -1 || dc == 1) {
                return [r, c + dc, dr, dc]
            } else {
                beams.add([r, c + 1, 0, 1] as Beam)
                return [r, c - 1, 0, -1]
            }
        }
        if (contraptionPart == "|") {
            if (dr == -1 || dr == 1) {
                return [r + dr, c + dc, dr, dc]
            } else {
                beams.add([r + 1, c, 1, 0] as Beam)
                return [r - 1, c, -1, 0]
            }
        }
        if (contraptionPart == "/") {
            if (dr == -1) {
                return [r, c + 1, 0, 1]
            }
            if (dr == 1) {
                return [r, c - 1, 0, -1]
            }
            if (dc == -1) {
                return [r + 1, c, 1, 0]
            }
            if (dc == 1) {
                return [r - 1, c, -1, 0]
            }
        }
        if (contraptionPart == "\\") {
            if (dr == -1) {
                return [r, c - 1, 0, -1]
            }
            if (dr == 1) {
                return [r, c + 1, 0, 1]
            }
            if (dc == -1) {
                return [r - 1, c, -1, 0]
            }
            if (dc == 1) {
                return [r + 1, c, 1, 0]
            }
        }
        return [r + dr, c + dc, dr, dc]
    }
}
