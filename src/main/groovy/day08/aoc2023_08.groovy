#!/usr/bin/env groovy
/**
 * --- Day 8: Haunted Wasteland ---
 * https://adventofcode.com/2023/day/8
 */
package day08

import java.util.regex.Matcher

import static utils.Utils.getFile

class Day08 {
    final static Integer day = 8
    static boolean debug = false

    static void main(String[] args) {
        def (String partI, String partN) = getFile(day).text.split("\r?\n\r?\n")
        Map<String, Tuple2<String, String>> network = loadNetwork(partN)

        // part 1
        println countSteps("AAA", {String candidate -> candidate == "ZZZ"}, cycle(partI), network)  // 18113

        // part 2
        List<Integer> steps = network.keySet().findAll { it.endsWith("A")}.collect {
            countSteps(it, {String candidate -> candidate.endsWith("Z")}, cycle(partI), network)
        }
        println steps.inject(1G) { result, value -> lcm(result, value as BigInteger)}  // 12315788159977
    }

    static int countSteps(String start, Closure<Boolean> isGoalFound, Closure<String> getInstruction,
                          final Map<String, Tuple2<String, String>> network) {
        int steps = 0
        String current = start
        if (debug) println "$current ->"

        while (!isGoalFound(current)) {
            steps++
            Tuple2<String, String> choices = network[current]
            String instruction = getInstruction()
            current = (instruction == "L") ? choices[0] : choices[1]
        }
        if (debug) println "-> $current"
        return steps
    }

    static Map<String, Tuple2<String, String>> loadNetwork(String input) {
        Map<String, Tuple2<String, String>> network = [:]
        input.eachLine { String line ->
            Matcher m = (line =~ /([A-Z0-9]+) = \(([A-Z0-9]+), ([A-Z0-9]+)\)/)
            def (String _, String a, String b, String c) = m[0]
            network[a] = new Tuple2(b, c)
        }

        return network
    }

    static Closure<String> cycle(String instructions) {
        int i = 0
        Closure<String> gen = {
            String ret = instructions[i++]
            i %= instructions.size()
            return ret
        }
        return gen
    }

    static BigInteger gcd(BigInteger a, BigInteger b) {
        if (b == 0) return a
        return gcd(b, a % b)
    }

    static BigInteger lcm(BigInteger a, BigInteger b) {
        return a * b / gcd(a, b)
    }
}

