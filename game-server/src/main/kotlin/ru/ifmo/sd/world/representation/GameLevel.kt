package ru.ifmo.sd.world.representation

import kotlin.math.abs

class GameLevel(private val level: Array<IntArray>) {
    val gameLevel: Array<IntArray>
        get() = this.level

    fun isAvailable(oldPos: Position, newPos: Position): Boolean {
        return isAdjacent(oldPos, newPos) && insideMaze(newPos) && level[newPos.row][newPos.column] == 0
    }

    private fun insideMaze(newPos: Position): Boolean {
        return newPos.row < level.size && newPos.column < level[0].size
    }

    private fun isAdjacent(oldPos: Position, newPos: Position): Boolean {
        return abs(oldPos.row - newPos.row) <= 1 && abs(oldPos.column - newPos.column) <= 1
    }
}
