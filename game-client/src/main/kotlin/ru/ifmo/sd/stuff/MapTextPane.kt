package ru.ifmo.sd.stuff

import ru.ifmo.sd.world.representation.Position
import java.awt.Color
import java.awt.Color.*
import javax.swing.JTextPane
import javax.swing.text.Style
import javax.swing.text.StyleConstants
import java.awt.Graphics;

class MapTextPane : JTextPane() {
    private val FONT_FAMILY = "Monospaced"
    private val FONT_SIZE = 24
    val colorMap: MutableMap<Color, Style> = mutableMapOf()
    private val colors = arrayOf(BLACK, RED, YELLOW, GREEN, MAGENTA, ORANGE, BLUE, GRAY)

    init {
        isEditable = false
        for (color in colors) {
            val newStyle = addStyle(color.toString(), null)
            StyleConstants.setFontFamily(newStyle, FONT_FAMILY)
            StyleConstants.setFontSize(newStyle, FONT_SIZE)
            colorMap[color] = newStyle
            StyleConstants.setForeground(newStyle, color)
        }
    }
}