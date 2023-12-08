#!/usr/bin/env groovy
/**
 * --- Day 7: Camel Cards ---
 * https://adventofcode.com/2023/day/7
 */
package day07

import static utils.Utils.getFile

class Day07 {
    final static Integer day = 7
    static boolean debug = false

    static void main(String[] args) {
        println getScore()  // 252052080
        println getScore(true)  // 252898370

    }

    static Integer getScore(boolean useJokers = false) {
        List<Hand> hands = []
        getFile(day).eachLine { String line ->
            def (sHand, sBid) = line.split()
            Hand hand = new Hand(sHand as String)
            hand.useJokers = useJokers
            hand.bid = sBid.toInteger()
            hands << hand
        }

        hands.sort()
        int score = 0
        hands.eachWithIndex { Hand entry, int i ->
            if (debug) {
                println "${i + 1}\t${entry.hand}\t${entry.handType}\t${entry.handTypeJokers}\t${entry.hand.contains("J")}"
            }
            score += (i + 1) * entry.bid
        }

        return score
    }
}

enum HandType {
    FIVE, FOUR, FULL, THREE, PAIRS, PAIR, HIGH, NONE
}

class Hand implements Comparable<Hand> {
    final String handType
    final String handTypeJokers
    final String hand

    boolean useJokers = false
    Integer bid = 0

    private static Map<String, Integer> handValues = ["FIVE" : 9,
                                                      "FOUR" : 8,
                                                      "FULL" : 7,
                                                      "THREE": 6,
                                                      "PAIRS": 5,
                                                      "PAIR" : 4,
                                                      "HIGH" : 3]

    private static final Map<String, Integer> cardValues = ["A": 13,
                                                            "K": 12,
                                                            "Q": 11,
                                                            "J": 10,
                                                            "T": 9,
                                                            "9": 8,
                                                            "8": 7,
                                                            "7": 6,
                                                            "6": 5,
                                                            "5": 4,
                                                            "4": 3,
                                                            "3": 2,
                                                            "2": 1]

    Hand(String hand) {
        this.hand = hand
        handType = getHandType(hand).name()
        handTypeJokers = getHandType(hand, true).name()
    }

    static HandType getHandType(String hand, boolean useJokers = false) {
        Map handMap = hand.toList().groupBy { it }
        List sizes = handMap.values()*.size()

        if (5 in sizes) {
            return HandType.FIVE
        }

        if (4 in sizes) {
            if (useJokers && "J" in handMap) {
                return HandType.FIVE
            }
            return HandType.FOUR
        }

        if (3 in sizes && 2 in sizes) {
            if (useJokers && "J" in handMap) {
                return HandType.FIVE
            }
            return HandType.FULL
        }

        if (3 in sizes) {
            if (useJokers && "J" in handMap) {
                return HandType.FOUR
            }
            return HandType.THREE
        }

        if (sizes.count(2) == 2) {
            if (useJokers && "J" in handMap) {
                int jCount = handMap["J"].size()
                switch (jCount) {
                    case 2:
                        return HandType.FOUR
                    case 1:
                        return HandType.FULL
                }
            }
            return HandType.PAIRS
        }

        if (2 in sizes) {
            if (useJokers && "J" in handMap) {
                return HandType.THREE
            }
            return HandType.PAIR
        }

        if (useJokers && "J" in handMap) {
            return HandType.PAIR
        }
        return HandType.HIGH
    }

    Integer getCardValue(String card) {
        return (useJokers && "J" == card) ? 0 : cardValues[card]
    }

    @Override
    int compareTo(Hand other) {
        int thisHandValue = useJokers ? handValues[handTypeJokers] : handValues[handType]
        int otherHandValue = useJokers ? handValues[other.handTypeJokers] : handValues[other.handType]
        int handTypeCmp = thisHandValue <=> otherHandValue
        if (handTypeCmp) {
            return handTypeCmp
        }

        List<Integer> thisValues = hand.collect { getCardValue(it) }
        List<Integer> otherValues = other.hand.collect { getCardValue(it) }
        assert thisValues.size() == otherValues.size()

        for (int i = 0; i < thisValues.size(); i++) {
            if (thisValues[i] != otherValues[i]) {
                return thisValues[i] <=> otherValues[i]
            }
        }
        return 0
    }
}
