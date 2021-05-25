package ru.ifmo.sd.stuff

import ru.ifmo.sd.httpapi.models.GameState
import ru.ifmo.sd.httpapi.models.JoinGameInfo
import ru.ifmo.sd.httpapi.models.MazeEventData
import ru.ifmo.sd.stuff.ColoredSymbol.*
import java.awt.Color
import java.awt.Color.*


enum class ColoredSymbol(val char: Char, val color: Color = BLACK) {
    WALL(0x2591.toChar(), GRAY),
    PLAYER(0x2689.toChar()/*, RED*/),
    PASSIVE_ENEMY(0x2640.toChar()),
    AGGRESSIVE_ENEMY(0x263F.toChar()),
    COWARD_ENEMY(0x2649.toChar()),
    NONE(' '),
}

class SymbolMap {

    val rows: List<MutableList<ColoredSymbol>>
    val rowSize: Int
        get() = rows.size
    val columnSize: Int
        get() = rows[0].size
    var enemyAmount: Int
        private set

    constructor(config: JoinGameInfo) {
        this.rows = config.maze.levelMaze.map { arr ->
            arr.map { i -> mazeObjToSymbol(i) }.toMutableList()
        }
        this.enemyAmount = config.maze.levelMaze.sumBy { arr -> arr.sumBy { i -> if (isEnemy(i)) 1 else 0 } }
        rows[config.playerPos.row][config.playerPos.column] = PLAYER
    }

    constructor(config: GameState) {
        this.rows = config.maze.levelMaze.map { arr ->
            arr.map { i -> mazeObjToSymbol(i) }.toMutableList()
        }
        this.enemyAmount = config.maze.levelMaze.sumBy { arr -> arr.sumBy { i -> if (isEnemy(i)) 1 else 0 } }
    }

    private fun mazeObjToSymbol(mazeObj: Int): ColoredSymbol = when (mazeObj) {
        0 -> NONE
        1 -> WALL
        2 -> PLAYER
        3 -> PASSIVE_ENEMY
        4 -> AGGRESSIVE_ENEMY
        5 -> COWARD_ENEMY
        else -> throw IllegalArgumentException("No such index in maze: $mazeObj")
    }

    internal fun applyDiff(events: List<MazeEventData>) {
        for (e in events) {
            val pos = e.position

            val wasEnemy = isEnemy(rows[pos.row][pos.column])
            val willBeEnemy = isEnemy(e.newMazeObj)
            if (wasEnemy && !willBeEnemy) enemyAmount -= 1
            if (!wasEnemy && willBeEnemy) enemyAmount += 1

            rows[pos.row][pos.column] = mazeObjToSymbol(e.newMazeObj)
        }
    }

    private fun isEnemy(i: Int): Boolean = i == 3 || i == 4 || i == 5
    private fun isEnemy(i: ColoredSymbol): Boolean = i == PASSIVE_ENEMY || i == AGGRESSIVE_ENEMY || i == COWARD_ENEMY
}