package ru.ifmo.sd

import ru.ifmo.sd.stuff.*
import java.awt.BorderLayout
import java.awt.EventQueue
import java.awt.event.KeyEvent
import javax.swing.*
import javax.swing.text.*


class GUI(title: String) : JFrame() {
    val mainPanel = JPanel()
    val headerPanel = JPanel()
    val mapPanel = JPanel()
    val infoPanel = JPanel()
    var map = SymbolMap(mapPreviewSymbols)

    init {
        createUI(title)
    }

    private fun createUI(title: String) {
        setTitle(title)

//        createMenuBar()
        createLayout()

        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(800, 600)
        isResizable = false
        setLocationRelativeTo(null)
    }

    private fun createMenuBar() {
        val menubar = JMenuBar()
        val icon = ImageIcon("src/main/resources/exit.png")

        val file = JMenu("File")
        file.mnemonic = KeyEvent.VK_F

        val eMenuItem = JMenuItem("Exit", icon)
        eMenuItem.mnemonic = KeyEvent.VK_E
        eMenuItem.toolTipText = "Exit application"
        eMenuItem.addActionListener { System.exit(0) }

        file.add(eMenuItem)
        menubar.add(file)

        jMenuBar = menubar
    }

    private fun createLayout() {
        add(mainPanel)
        mainPanel.layout = BorderLayout()

        headerPanel.setSize(800, 50)
        mainPanel.add(headerPanel, BorderLayout.NORTH)
        headerPanel.add(JButton("hey"))

        mapPanel.setSize(550, 550)
        mainPanel.add(mapPanel, BorderLayout.WEST)
        mapPanel.add(MapTextPane)
        createMap()

        infoPanel.setSize(250, 550)
        mainPanel.add(infoPanel, BorderLayout.EAST)
        infoPanel.add(JButton("Okaey"), BorderLayout.WEST)
    }

    private fun createMap() {
        for (i in 0 until SymbolMap.rowSize) {
            if (i != 0) {
                insertText("\n", MapTextPane.colorMap[MapSymbolColor.BLACK]!!)
            }
            for (j in 0 until SymbolMap.columnSize) {
                val symbol = map.rows[i][j]
                val style = MapTextPane.colorMap[symbol.color]!!
                insertText(symbol.content.toString(), style)
            }
        }
    }

    private fun insertText(string: String, style: Style) {
        try {
            val doc = MapTextPane.document
            doc.insertString(doc.length, string, style)
        } catch (e: Exception) {
            e.printStackTrace() // TODO
        }
    }
}

private fun createAndShowGUI() {
    val frame = GUI("Roguelike")
    frame.isVisible = true
}

fun main() {
    EventQueue.invokeLater(::createAndShowGUI)
}

























private val mapPreviewText = listOf(
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
)

val mapPreviewSymbols: List<List<Symbol>> =
    mapPreviewText.map { s ->
        s.map { c -> Symbol(c, MapSymbolColor.BLACK) }
    }