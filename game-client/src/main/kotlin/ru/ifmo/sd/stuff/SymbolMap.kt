package ru.ifmo.sd.stuff

import ru.ifmo.sd.stuff.ColoredSymbol.*
import ru.ifmo.sd.world.configuration.GameConfiguration
import java.awt.Color
import java.awt.Color.*


enum class ColoredSymbol(val char: Char, val color: Color = BLACK) {
    WALL(0x2591.toChar(), GRAY),
    PLAYER(0x267F.toChar()/*, RED*/),
    NONE(' '),
}

class SymbolMap(config: GameConfiguration) {
    val rows: List<MutableList<ColoredSymbol>> = config.level.map { arr ->
        arr.map { i -> if (i == 0) NONE else WALL }.toMutableList()
    }

    init {
        rows[config.playerPos.row][config.playerPos.column] = PLAYER
    }

    val rowSize: Int
        get() = rows.size
    val columnSize: Int
        get() = rows[0].size
}