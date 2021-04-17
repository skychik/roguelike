package ru.ifmo.sd.stuff

enum class MapSymbolColor {
    BLACK, GREEN, YELLOW, RED, BLUE, MAGENTA, ORANGE
}

data class Symbol(val content: Char, val color: MapSymbolColor = MapSymbolColor.BLACK)

data class SymbolMap(val rows: List<List<Symbol>> = List(rowSize) { List(columnSize) { Symbol(' ') } } ) {
    companion object {
        const val rowSize = 32
        const val columnSize = 64
    }
}