package ru.ifmo.sd.stuff

import ru.ifmo.sd.stuff.SymbolMap.Symbol.*

enum class MapSymbolColor {
    BLACK, GREEN, YELLOW, RED, BLUE, MAGENTA, ORANGE
}

data class ColoredSymbol(val content: Char, val color: MapSymbolColor = MapSymbolColor.BLACK)

class SymbolMap(level: Array<IntArray>) {
    enum class Symbol(val symbol: Char, val color: MapSymbolColor = MapSymbolColor.BLACK) {
        WALL('#'),
        PLAYER('@', MapSymbolColor.RED),
        NONE(' '),
    }

    val rows: List<MutableList<ColoredSymbol>> = level.map { arr ->
        arr.map { i ->
            ColoredSymbol(
                if (i == 0) NONE.symbol else WALL.symbol
            )
        }.toMutableList()
    }

    val rowSize: Int
        get() = rows.size
    val columnSize: Int
        get() = rows[0].size
}