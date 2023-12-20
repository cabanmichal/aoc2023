#!/usr/bin/env groovy
/**
 * --- Day 19: Aplenty ---
 * https://adventofcode.com/2023/day/19
 */
package day19


import static utils.Utils.getFile

class Day19 {
    final static Integer day = 19

    static void main(String[] args) {
        def parts = getFile(day).text.split(/\r?\n\r?\n/)
        def workflows = parts[0]
                .split(/\r?\n/)
                .collect { new WorkFlow(it) }
        def ratings = parts[1]
                .split(/\r?\n/)
                .collect { new Rating(it) }

        Map<String, WorkFlow> wfMap = [:]
        workflows.each { wfMap[it.name] = it }

        // part 1
        println ratings.findAll { getRatingDestination(it, wfMap) == "A" }
                .inject(0) { sum, rating -> sum += rating.rating }  // 389114

        // part 2
    }

    static String getRatingDestination(Rating rating, Map<String, WorkFlow> workFlowMap) {
        def current = "in"
        while (current != "A" && current != "R") {
            current = workFlowMap[current].nextWorkFlow(rating)
        }

        return current
    }
}

class WorkFlow {
    String name
    List<Command> commands
    String goal

    WorkFlow(String wfString) {
        def matcher = wfString =~ /(?i)([a-z]+)\{(.+),([a-z]+)}/
        if (!matcher.matches()) throw new IllegalArgumentException("String in incorrect format")
        name = matcher.group(1)
        commands = matcher.group(2).split(/,/).collect { new Command(it) }
        goal = matcher.group(3)
    }

    String nextWorkFlow(Rating rating) {
        for (def c : commands) {
            if (c.valueOk((rating[c.attribute] as Integer))) {
                return c.goal
            }
        }
        return goal
    }
}

class Command {
    String attribute
    private Closure<Boolean> test
    Integer value
    String goal

    Command(String commandString) {
        def matcher = commandString =~ /(?i)([a-z]+)([><])([0-9]+):([a-z]+)/
        if (!matcher.matches()) throw new IllegalArgumentException("String in incorrect format")
        attribute = matcher.group(1)
        switch (matcher.group(2)) {
            case "<":
                test = { a, b -> a < b }
                break
            case ">":
                test = { a, b -> a > b }
                break
        }
        value = matcher.group(3).toInteger()
        goal = matcher.group(4)
    }

    boolean valueOk(Integer value) {
        return test(value, this.value)
    }

}

class Rating {
    Integer x, m, a, s

    Rating(String ratingString) {
        ratingString[1..<-1].split(/,/).each {
            def parts = it.split(/=/)
            def attr = parts[0]
            def val = parts[1].toInteger()
            switch (attr) {
                case "x":
                    x = val
                    break
                case "m":
                    m = val
                    break
                case "a":
                    a = val
                    break
                case "s":
                    s = val
                    break
            }

        }
    }

    Integer getRating() {
        return x + m + a + s
    }

}