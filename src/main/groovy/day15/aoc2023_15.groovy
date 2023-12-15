#!/usr/bin/env groovy
/**
 * --- Day 15: Lens Library ---
 * https://adventofcode.com/2023/day/15
 */
package day15

import groovy.transform.Canonical

import static utils.Utils.getFile

class Day15 {
    final static Integer day = 15

    static void main(String[] args) {

        // part 1
        println getFile(day).text.trim().split(/,/)
                .inject(0) {result, s -> result += seqHash(s)} // 506869

        // part 2
        def boxes = (0..<256).collect { new Box(it) }
        getFile(day).text.trim().split(/,/).each { String seq ->
            def s = new Seq(seq)
            boxes[seqHash(s.label)].processSeq(s)
        }
        println boxes.inject(0) { result, b -> result += b.getBoxScore() }  // 271384
    }

    static int seqHash(String seq) {
        def currentValue = 0
        seq.toCharArray().each {
            currentValue += it
            currentValue *= 17
            currentValue %= 256
        }

        return currentValue
    }
}

@Canonical
class Seq {
    String label
    int focalLength
    String seq

    Seq(String seq) {
        def parts = seq.split(/=/) as List<String>
        label = parts[0].replace("-", "")
        focalLength = parts[1]?.toInteger() ?: 0
        this.seq = seq
    }
}

@Canonical
class Box {
    Integer id
    List<Seq> lenses = []

    Box(Integer id) {
        this.id = id
    }

    void processSeq(Seq seq) {
        def i = lenses.findIndexOf { it.label == seq.label }
        if (seq.focalLength == 0) {
            if (i != -1) {
                lenses.remove(i)
            }
        } else if (i != -1) {
            lenses[i] = seq
        } else {
            lenses << seq
        }
    }

    int getBoxScore() {
        def score = 0
        lenses.eachWithIndex { Seq entry, int i ->
            score += (id + 1) * (i + 1) * entry.focalLength
        }

        return score
    }
}
