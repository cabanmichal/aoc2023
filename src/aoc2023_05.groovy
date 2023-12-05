#!/usr/bin/env groovy
/**
 * --- Day 5: You Give A Seed A Fertilizer ---
 * https://adventofcode.com/2023/day/5
 */

class Day05 {
    final static String INPUT = "../data/aoc2023_05_input.txt"

    static void main(String[] args) {
        Solver solver = new Solver(INPUT)

        // part 1
        println solver.seeds.collect { solver.getLocationOfSeed(it) }.min()  // 825516882

        // part 2 - brute force, better approach using ranges is needed which I don't fully understand
        BigInteger seed = -1
        BigInteger location = 0
        while (!solver.isSeedInSeedRanges(seed)) {
            if (location % 1000000 == 0) {
                println location / 1000000
            }
            seed = solver.getSeedForLocation(location++)
        }
        location--
        println location  // 136096660

    }
}

class Solver {
    List<BigInteger> seeds = []
    List<AlmanacMap> maps = []

    Solver(String inputPath) {
        List<String> items = new File(inputPath).text.split(/\r?\n\r?\n/)
        seeds = (items[0] - "seeds:").split()*.toBigInteger()

        items[1..-1].each { String item ->
            List<String> parts = item.split(/\r?\n/)
            AlmanacMap map = new AlmanacMap()
            parts[1..-1].each { String part -> map.addRange(part) }
            maps << map
        }
    }

    BigInteger getLocationOfSeed(BigInteger seed) {
        BigInteger location = seed
        maps.each { location = it.getDestination(location) }
        return location
    }

    BigInteger getSeedForLocation(BigInteger location) {
        BigInteger seed = location
        maps.reverseEach { seed = it.getSource(seed) }
        return seed
    }

    boolean isSeedInSeedRanges(BigInteger seed) {
        for (List<BigInteger> startCountPair in seeds.collate(2)) {
            if (seed >= startCountPair[0] && seed < (BigInteger) startCountPair.sum()) {
                return true
            }
        }
        return false
    }

}

class AlmanacMap {
    List<AlmanacRange> ranges = []

    void addRange(String input) {
        ranges << new AlmanacRange(input)
    }

    BigInteger getDestination(BigInteger source) {
        for (AlmanacRange range : ranges) {
            if (range.containsSource(source)) {
                return range.getDestination(source)
            }
        }
        return source
    }

    BigInteger getSource(BigInteger destination) {
        for (AlmanacRange range : ranges) {
            if (range.containsDestination(destination)) {
                return range.getSource(destination)
            }
        }
        return destination
    }
}

class AlmanacRange {
    static BigInteger INVALID = -1
    BigInteger destinationStart
    BigInteger sourceStart
    BigInteger rangeLength

    AlmanacRange(String input) {
        (destinationStart, sourceStart, rangeLength) = input.split()*.toBigInteger()
    }

    boolean containsSource(BigInteger source) {
        return (source >= sourceStart && source < sourceStart + rangeLength)
    }

    boolean containsDestination(BigInteger destination) {
        return (destination >= destinationStart && destination < destinationStart + rangeLength)
    }

    BigInteger getDestination(BigInteger source) {
        if (!containsSource(source)) {
            return INVALID
        }
        return source - sourceStart + destinationStart
    }

    BigInteger getSource(BigInteger destination) {
        if (!containsDestination(destination)) {
            return INVALID
        }
        return destination - destinationStart + sourceStart
    }
}