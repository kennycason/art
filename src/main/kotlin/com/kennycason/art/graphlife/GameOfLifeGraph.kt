package com.kennycason.art.graphlife

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.util.*

// graph creation randomly fails, caused when adding sorting, TODO debug why this is (overlapping points? error?)
class GameOfLifeGraph(private val dimension: Dimension) {
    private val nodes = mutableListOf<Node>()

    init {
        val nodeCount = 9 * 100
        (0 until nodeCount).forEach {
            nodes.add(Node(
                    state = random.nextInt(2) == 0,
                     point = Point(random.nextInt(dimension.width), random.nextInt(dimension.height))))
        }

        // connect "nearest" neighbors
        nodes.forEachIndexed { i, node ->
            if (node.neighbors.size == 8) {
                println("node $i already has 8 neighbors")
                return@forEachIndexed
            }

            val nearestNodes = findNearestNNodes(node)
            if (nearestNodes.size + node.neighbors.size < 8) {
                throw IllegalStateException("didn't find enough compatible nodes, found: ${nearestNodes.size + node.neighbors.size }")
            }

            nearestNodes.forEach { nearestNode ->
                nearestNode.neighbors.add(node)
                node.neighbors.add(nearestNode)
            }
            println("node $i finished: neighbors: ${node.neighbors.size}")
        }

        // verify graph
        val counts = mutableMapOf<Node, Int>()
        for (node in nodes) {
            for (neighbor in node.neighbors) {
                if (!counts.containsKey(neighbor)) {
                    counts[neighbor] = 0
                }
                counts[neighbor] = counts[neighbor]!! + 1
            }
        }
        for (count in counts) {
            if (count.value != 8) {
                throw IllegalStateException("nodes can only appear in 8 connections, count = ${count.value}")
            }
        }

        println("done")
    }

    fun iterate() {
        nodes.forEach { node ->
            node.calculateNextStateDiscrete()
            // node.calculateNextStateContinuous()
        }
        nodes.forEach { node ->
            node.updateState()
        }
    }

    fun draw(graphics: Graphics) {
        graphics.color = Color.WHITE
        nodes.forEach { node ->
            node.neighbors.forEach { neighbor ->
                graphics.drawLine(node.point.x, node.point.y, neighbor.point.x, neighbor.point.y)
            }
        }

        nodes.forEach { node ->
            graphics.color = when (node.state) {
                true -> Color.GREEN
                false -> Color.RED
            }
            graphics.fillOval(node.point.x - 2, node.point.y - 2, 10, 10)
        }
    }

    private fun findNearestNNodes(node: Node): List<Node> {
        if (node.neighbors.size == 8) { return emptyList() }

        val nodesToAdd = 8 - node.neighbors.size
        var i = 0
        return nodes
                .asSequence()
                .map {
                    val dx = node.point.x - it.point.x
                    val dy = node.point.y - it.point.y
                    val dist = Math.sqrt((dx * dx + dy * dy).toDouble())
                    Pair(dist, it)
                }
                .sortedWith(Comparator { o1, o2 -> o1.first.compareTo(o2.first) })
//                .sortedBy { it.first }
                .map { it.second }
                .filter { it.neighbors.size < 8 }
                .filter { it != node }
                .take(nodesToAdd)
                .toList()
    }

    companion object Graph {
        private val random = Random()
    }
}
