#!/usr/bin/env groovy
/**
 * --- Day 4: Scratchcards ---
 * https://adventofcode.com/2023/day/4
 */

class Day04 {
    final static String INPUT = "../data/aoc2023_04_input.txt"

    static void main(String[] args) {

        List<Scratchcard> scratchcards = []
        new File(INPUT).eachLine { line -> scratchcards << new Scratchcard(line)
        }

        // part 1
        println scratchcards.sum { it.getPoints() }  // 21821

        // part 2
        scratchcards.each { Scratchcard card ->
            int nOfWinningNumbers = card.getMatchedNumbers().size()
            for (int i = card.id; i < card.id + nOfWinningNumbers; i++) {
                scratchcards[i].count += card.count
            }
        }

        println scratchcards.sum { it.count }  // 5539496
    }
}

class Scratchcard {
    final int id
    int count = 1
    final List<Integer> winningNumbers
    final List<Integer> cardNumbers

    Scratchcard(String card) {
        String _, sId, winning, our
        (_, sId, winning, our) = (card =~ /^Card +(\d+): +(.+?) \| +(.+)$/)[0]

        id = sId.toInteger()
        winningNumbers = winning.split()*.toInteger()
        cardNumbers = our.split()*.toInteger()
    }

    List<Integer> getMatchedNumbers() {
        Set<Integer> winning = winningNumbers.toSet()
        return cardNumbers.findAll { it in winning }
    }

    int getPoints() {
        int matchedCount = getMatchedNumbers().size()
        return (matchedCount) ? 2**(matchedCount - 1) : 0
    }
}