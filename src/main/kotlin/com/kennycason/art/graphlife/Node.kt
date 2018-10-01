package com.kennycason.art.graphlife

class Node(var state: Boolean,
           var nextState: Boolean = false, // ued when computing next state
           var point: Point,
           val neighbors: MutableList<Node> = mutableListOf()) {

    @Suppress("RedundantIf")
    fun calculateNextStateDiscrete() {
        val aliveNeighbors = neighbors.count { node -> node.state }

        if (state) {
            if (aliveNeighbors < 2 || aliveNeighbors > 3) { // more than 3 neighbors, die overpopulation. less than 2 neighbors, die underpopulation.
                nextState = false
            }
            else {
                nextState = true // stay alive
            }
        }
        else {
            if (aliveNeighbors == 3) { // if has 3, then revive
                nextState = true
            }
            else {
                nextState = false // stay dead
            }
        }
    }

    fun calculateNextStateContinuous() {
        val aliveNeighbors = neighbors.count { node -> node.state }
        val totalNeighbors = neighbors.size.toFloat()
        val aliveRatio = aliveNeighbors / totalNeighbors

        if (state) {
            // less than 2 neighbors in gol = 1 out of 8 ~ 12.5%, die underpopulation
            if (aliveRatio < 0.125)  {
                nextState = false
            }
            // more than 3 neighbors in gol = 4 or more of 8 ~ 50%, die overpopulation
            else if (aliveRatio > 0.5)  {
                nextState = false
            }
            else {
                // stay alive
                nextState = state
            }
        }
        else {
            // if has 3 neighbors of 8, > 2/8(25%) and < 4/8(50%) then revive
            if (aliveRatio > 0.25 && aliveRatio < 0.5) {
                nextState = true
            }
            else {
                // stay dead
                nextState = state
            }
        }
    }

    fun updateState() {
        state = nextState
    }

}