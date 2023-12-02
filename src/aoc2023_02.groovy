#!/usr/bin/env groovy
import java.util.regex.Pattern

/**
 * --- Day 2: Cube Conundrum ---
 * https://adventofcode.com/2023/day/2
 */

class Day02 {
    final static String INPUT = "../data/aoc2023_02_input.txt"

    static void main(String[] args) {
        List games = []
        new File(INPUT).eachLine {
            games << Game.fromGameInfo(it)
        }

        // part 1
        int sumOfIdsOfPossibleGames = 0
        CubeSet configuration = new CubeSet(red: 12, green: 13, blue: 14)

        for (Game game : games) {
            if (game.isGamePossible(configuration)) {
                sumOfIdsOfPossibleGames += game.gameId
            }
        }
        println sumOfIdsOfPossibleGames  // 2377

        // part 2
        int sumOfPowersOfMinimumSets = 0

        for (Game game : games) {
            sumOfPowersOfMinimumSets += game.getMinimumCubeSet().getSetPower()
        }
        println sumOfPowersOfMinimumSets  // 71220
    }
}

class Game {
    List<CubeSet> sets = []
    int gameId = -1

    static Game fromGameInfo(String gameInfo) {
        Pattern pIdCubes = ~/Game (\d+): (.+)*/
        Pattern pCubeInfo = ~/(\d+) (red|green|blue)/
        String gId, cubesInfo, cubeCount, cubeColor

        Game instance = new Game()

        (gId, cubesInfo) = (gameInfo =~ pIdCubes)[0][1, 2]
        instance.gameId = gId.toInteger()

        for (String round : cubesInfo.split(";")) {

            CubeSet cubeSet = new CubeSet()

            for (String cubeInfo : round.split(",")) {
                (cubeCount, cubeColor) = (cubeInfo =~ pCubeInfo)[0][1, 2]
                cubeSet[cubeColor] = cubeCount.toInteger()
            }

            instance.sets << cubeSet
        }

        return instance
    }

    boolean isGamePossible(CubeSet configuration) {
        for (CubeSet gameSet in sets) {
            if (gameSet.red > configuration.red
                    || gameSet.green > configuration.green
                    || gameSet.blue > configuration.blue) {
                return false
            }
        }
        return true
    }

    CubeSet getMinimumCubeSet() {
        int red = 0
        int green = 0
        int blue = 0

        for (CubeSet currentSet : sets) {
            if (currentSet.red > red) {
                red = currentSet.red
            }
            if (currentSet.green > green) {
                green = currentSet.green
            }
            if (currentSet.blue > blue) {
                blue = currentSet.blue
            }
        }

        return new CubeSet(red: red, green: green, blue: blue)
    }
}

class CubeSet {
    int red
    int green
    int blue

    int getSetPower() {
        return red * green * blue
    }
}