package ru.ifmo.sd.stuff

import ru.ifmo.sd.httpapi.models.JoinGameInfo
import ru.ifmo.sd.httpapi.models.MazeEventData
import ru.ifmo.sd.stuff.ColoredSymbol.*
import java.awt.Color
import java.awt.Color.*


enum class ColoredSymbol(val char: Char, val color: Color = BLACK) {
    WALL(0x2591.toChar(), GRAY),
    PLAYER(0x267F.toChar()/*, RED*/),
    ENEMY(0x2639.toChar()),
    NONE(' '),
}

class SymbolMap(config: JoinGameInfo) {
    val rows: List<MutableList<ColoredSymbol>> = config.maze.levelMaze.map { arr ->
        arr.map { i -> mazeObjToSymbol(i) }.toMutableList()
    }

    init {
        rows[config.playerPos.row][config.playerPos.column] = PLAYER
    }

    val rowSize: Int
        get() = rows.size
    val columnSize: Int
        get() = rows[0].size

    private fun mazeObjToSymbol(mazeObj: Int): ColoredSymbol = when (mazeObj) {
        1 -> WALL
        2 -> PLAYER
        3 -> ENEMY
        else -> NONE
    }

    internal fun applyDiff(events: List<MazeEventData>) {
        events.forEach { e ->
            val pos = e.position
            rows[pos.row][pos.column] = mazeObjToSymbol(e.newMazeObj)
        }
    }
}