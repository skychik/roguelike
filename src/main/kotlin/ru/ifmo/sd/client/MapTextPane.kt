package ru.ifmo.sd.client

import ru.ifmo.sd.stuff.MapSymbolColor
import java.awt.Color
import javax.swing.JTextPane
import javax.swing.text.Style
import javax.swing.text.StyleConstants

object MapTextPane : JTextPane() {
    private val FONT_FAMILY = "Monospaced"
    private val FONT_SIZE = 12
    val colorMap: MutableMap<MapSymbolColor, Style> = mutableMapOf()

    init {
        isEditable = false
        for (color in MapSymbolColor.values()) {
            val newStyle = addStyle(color.toString(), null)
            StyleConstants.setFontFamily(newStyle, FONT_FAMILY)
            StyleConstants.setFontSize(newStyle, FONT_SIZE)
            colorMap[color] = newStyle
        }
        StyleConstants.setForeground(colorMap[MapSymbolColor.BLACK], Color.BLACK)
        StyleConstants.setForeground(colorMap[MapSymbolColor.GREEN], Color.GREEN)
        StyleConstants.setForeground(colorMap[MapSymbolColor.RED], Color.RED)
        StyleConstants.setForeground(colorMap[MapSymbolColor.YELLOW], Color.YELLOW)
        StyleConstants.setForeground(colorMap[MapSymbolColor.MAGENTA], Color.MAGENTA)
        StyleConstants.setForeground(colorMap[MapSymbolColor.ORANGE], Color.ORANGE)
        StyleConstants.setForeground(colorMap[MapSymbolColor.BLUE], Color.BLUE)
    }
}