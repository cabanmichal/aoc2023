#!/usr/bin/env groovy
/**
 * --- Day 10: Pipe Maze ---
 * https://adventofcode.com/2023/day/10
 */
package day10

import groovy.transform.Canonical
import groovy.transform.Immutable

import static utils.Utils.getFile

class Day10 {
    final static Integer day = 10
    final static File file = getFile(day)

    static void main(String[] args) {

        // part 1
        Map<Position, Node> graph = getGraph(file)
        Node start = graph.find { it.value.name == "S"}.value
        Set<Node> loop = getCluster(start)
        println loop.size().intdiv(2)  // 7086

        // part 2 - unfortunately, I don't know how to determine if a node is inside or outside the loop
    }

    static Map<Position, Node> getGraph(File file) {
        Map<Position, Node> graph = [:]
        file.eachWithIndex { String line, int i ->
            line.eachWithIndex { String entry, int j ->

                Position p = [i, j]
                Node n = [entry, p]
                graph[p] = n

                p.getAdjacentPositions().each {
                    Node other = graph[it]
                    other?.addConnectedNode(n)
                    n.addConnectedNode(other)
                }
            }
        }

        return graph
    }

    static Set<Node> getCluster(Node node) {
        Set<Node> cluster = []
        Set<Node> toProcess = [node]
        while (toProcess) {
            Node current = toProcess.first()
            toProcess.remove(current)
            cluster.add(current)
            current.getConnectedNodes().each {
                if (!(it in cluster)) {
                    toProcess.add(it)
                }
            }
        }

        return cluster
    }
}

@Immutable
class Position {
    int row
    int column

    @Override
    String toString() {
        return "Position($row, $column)".toString()
    }

    List<Position> getAdjacentPositions() {
        List<Position> positions = [
                [row - 1, column] as Position,
                [row, column + 1] as Position,
                [row + 1, column] as Position,
                [row, column - 1] as Position
        ]

        return positions
    }
}

@Canonical
class Node {
    String name
    Position position
    private Set<Node> connected = []

    @Override
    String toString() {
        return "Node($name, Position($position.row, $position.column))".toString()
    }

    private boolean canConnectedNodeTo(Node other) {
        if (!other) return false

        int dr = other.position.row - position.row
        int dc = other.position.column - position.column
        if (".IO".contains(name) && ".IO".contains(other.name)
                && dr >= -1 && dr <= 1 && dc >= -1 && dc <= 1) return true
        return canConnectToLeft(other, dr, dc)
                || canConnectToRight(other, dr, dc)
                || canConnectToTop(other, dr, dc)
                || canConnectToBottom(other, dr, dc)
    }

    private boolean canConnectToLeft(Node other, int dr, int dc) {
        if (!other) return false
        if (dr != 0 || dc != -1) return false
        if (!"-7JS".contains(name)) return false
        return "-FLS".contains(other.name)
    }

    private boolean canConnectToRight(Node other, int dr, int dc) {
        if (!other) return false
        if (dr != 0 || dc != 1) return false
        if (!"-FLS".contains(name)) return false
        return "-7JS".contains(other.name)
    }

    private boolean canConnectToTop(Node other, int dr, int dc) {
        if (!other) return false
        if (dr != -1 || dc != 0) return false
        if (!"|JLS".contains(name)) return false
        return "|F7S".contains(other.name)
    }

    private boolean canConnectToBottom(Node other, int dr, int dc) {
        if (!other) return false
        if (dr != 1 || dc != 0) return false
        if (!"|F7S".contains(name)) return false
        return "|JLS".contains(other.name)
    }

    void addConnectedNode(Node other) {
        if (canConnectedNodeTo(other)) {
            connected.add(other)
        }
    }

    List<Node> getConnectedNodes() {
        return connected.collect()
    }
}
