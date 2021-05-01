package ru.ifmo.sd.stuff

import kotlinx.coroutines.runBlocking
import ru.ifmo.sd.makeMove
import ru.ifmo.sd.makeNewGameConfiguration
import ru.ifmo.sd.stuff.ColoredSymbol.*
import ru.ifmo.sd.world.configuration.GameConfiguration
import ru.ifmo.sd.world.representation.Position
import java.awt.BorderLayout
import java.awt.Color.*
import java.awt.Container
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.*
import java.awt.event.KeyListener
import javax.swing.*
import javax.swing.text.Style

enum class MoveEvent {
    UP, DOWN, LEFT, RIGHT, NONE
}

class GUI(title: String, gameConfiguration: GameConfiguration) : JFrame(), KeyListener {
    private var currPos = gameConfiguration.playerPos
    private val mainPanel = JPanel()
    private val headerPanel = JPanel()
    private val mapPanel = JPanel()
    private val infoPanel = JPanel()
    private var map = SymbolMap(gameConfiguration)
    private var mapTextPane = MapTextPane()

    init {
        createUI(title)
        start()
    }

    private fun createUI(title: String) {
        setTitle(title)

        createLayout()
        makeNotFocusable()

        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(800, 600)
        isResizable = false
        setLocationRelativeTo(null)

        // TODO should work regardless of component being focused
        isFocusable = true
        focusTraversalKeysEnabled = false
        this.addKeyListener(this)

        requestFocus()
        requestFocusInWindow()
    }

    private fun start() {
        Thread {
            while (true) {

                try {
                    Thread.sleep(30)
                } catch (e: InterruptedException) {
                    break
                }

                mainPanel.repaint()
            }
        }.start()
    }

    private fun createLayout() {
        add(mainPanel)
        mainPanel.layout = BorderLayout()

        headerPanel.setSize(800, 50)
        mainPanel.add(headerPanel, BorderLayout.NORTH)
//        val infoLabel = JLabel("loading...")
//        infoLabel.isFocusable = false
//        headerPanel.add(infoLabel)

        mapPanel.setSize(550, 550)
        mainPanel.add(mapPanel, BorderLayout.WEST)
        mapPanel.add(mapTextPane)
        createMapTextPane()

        infoPanel.setSize(250, 550)
        mainPanel.add(infoPanel, BorderLayout.EAST)

//        val hpLabel = JLabel("HP: ")
//        hpLabel.isFocusable = false
//        infoPanel.add(hpLabel, BorderLayout.WEST)
    }

    private fun makeNotFocusable(container: Container = this) {
        container.isFocusable = false
        for (c in container.components) {
            if (c is Container) makeNotFocusable(c)
        }
    }

    private fun createMapTextPane() {
        mapTextPane.document.remove(0, mapTextPane.document.length)
        for (i in 0 until map.rowSize) {
            if (i != 0) {
                insertText("\n", mapTextPane.colorMap[BLACK]!!)
            }
            for (j in 0 until map.columnSize) {
                val symbol = map.rows[i][j]
                val style = mapTextPane.colorMap[symbol.color]!!
                insertText(symbol.char.toString(), style)
            }
        }
        // TODO: make lineSpacing = 0
//        mapTextPane.margin = Insets(0, 0, 0, 0)
//        mapTextPane.selectAll()
//        val aSet = SimpleAttributeSet(mapTextPane.paragraphAttributes)
//        val doc = mapTextPane.styledDocument
//        mapTextPane.setParagraphAttributes(aSet, false)
//        doc.setParagraphAttributes(0, doc.length, aSet, false)
//        StyleConstants.setLineSpacing(aSet, 0F)
//        mapTextPane.select(0, 0)
    }

    private fun insertText(string: String, style: Style) {
        try {
            val doc = mapTextPane.document
            doc.insertString(doc.length, string, style)
        } catch (e: Exception) {
            e.printStackTrace() // TODO
        }
    }

    /////////////////////////////////////

    var moved = MoveEvent.NONE
    var didMove = false

    override fun keyReleased(e: KeyEvent?) {}
    override fun keyTyped(e: KeyEvent?) {}
    override fun keyPressed(e: KeyEvent?) {
        if (e != null) {
            println("Some key pressed, current=$moved, keyCode=${e.keyCode}, didMove=$didMove, currPos=$currPos")
        } else {
            println("e == null")
        }
        if (!didMove) {
            when (e?.keyCode) {
                null -> {
                    moved = MoveEvent.NONE
                }
                VK_UP, VK_W -> {
                    val newPos = Position(currPos.row - 1, currPos.column)
                    if (replaceSymbol(currPos, newPos)) {
                        didMove = true
                        moved = MoveEvent.UP
                        currPos = newPos
                    }
                }
                VK_DOWN, VK_S -> {
                    val newPos = Position(currPos.row + 1, currPos.column)
                    if (replaceSymbol(currPos, newPos)) {
                        didMove = true
                        moved = MoveEvent.DOWN
                        currPos = newPos
                    }
                }
                VK_LEFT, VK_A -> {
                    val newPos = Position(currPos.row, currPos.column - 1)
                    if (replaceSymbol(currPos, newPos)) {
                        didMove = true
                        moved = MoveEvent.LEFT
                        currPos = newPos
                    }
                }
                VK_RIGHT, VK_D -> {
                    val newPos = Position(currPos.row, currPos.column + 1)
                    if (replaceSymbol(currPos, newPos)) {
                        didMove = true
                        moved = MoveEvent.RIGHT
                        currPos = newPos
                    }
                }
            }

            if (didMove) {
                println("Should move=$moved")

                didMove = false
                this.validate()
                this.mapPanel.validate()
                this.mapTextPane.validate()
                this.repaint()
                this.mapPanel.repaint()
                this.mapTextPane.repaint()

                runBlocking {
                    if (currPos.row == map.rowSize - 1 && currPos.column == map.columnSize - 2) {
                        val newConfig = makeNewGameConfiguration().deserializeBack()
                        map = SymbolMap(newConfig)
                        createMapTextPane()
                        currPos = newConfig.playerPos
                    } else {
                        makeMove(currPos)
                    }
                }
            }
        }
    }

    private fun replaceSymbol(oldPos: Position, newPos: Position, replaceSymb: ColoredSymbol = NONE): Boolean {
        if (newPos.row < map.rowSize && newPos.row >= 0 &&
            newPos.column < map.columnSize && newPos.column >= 0 &&
            map.rows[newPos.row][newPos.column] == NONE
        ) {
            val oldSymb = map.rows[oldPos.row][oldPos.column]
            map.rows[oldPos.row][oldPos.column] = replaceSymb
            map.rows[newPos.row][newPos.column] = oldSymb
            replacePaneSymbol(oldPos, replaceSymb)
            replacePaneSymbol(newPos, oldSymb)
            return true
        }
        return false
    }

    private fun replacePaneSymbol(pos: Position, symbol: ColoredSymbol) {
        mapTextPane.isEditable = true
        val index = pos.row * (map.columnSize + 1) + pos.column
        mapTextPane.select(index, index + 1)
//        println("selected text='${mapTextPane.selectedText}'")
        mapTextPane.selectionColor = symbol.color
        mapTextPane.replaceSelection(symbol.char.toString())
        mapTextPane.isEditable = false
    }
}