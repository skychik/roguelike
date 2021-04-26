package ru.ifmo.sd.stuff

import ru.ifmo.sd.stuff.SymbolMap.Symbol.*
import ru.ifmo.sd.world.configuration.GameConfiguration
import ru.ifmo.sd.world.representation.Position
import java.awt.BorderLayout
import java.awt.Color
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.*
import javax.swing.text.Style

enum class MoveEvent {
    UP, DOWN, LEFT, RIGHT, NONE
}

class GUI : JFrame, KeyListener {
    constructor(title: String, gameConfiguration: GameConfiguration) : super() {
        this.mainPanel = JPanel()
        this.headerPanel = JPanel()
        this.mapPanel = JPanel()
        this.infoPanel = JPanel()
        this.map = SymbolMap(gameConfiguration.level)
        this.mapTextPane = MapTextPane()
        this.currPos = gameConfiguration.playerPos
//        this.keyListener = MyKeyListener()
        createUI(title)
    }

//    private enum class State {
//        STARTING, INPUT_WAITING, PENDING_REQUEST
//    }


    private var currPos: Position
    private val mainPanel: JPanel
    private val headerPanel: JPanel
    private val mapPanel: JPanel
    private val infoPanel: JPanel
    private var map: SymbolMap
    private var mapTextPane: MapTextPane
//    private var keyListener: MyKeyListener

//    private var lock = Object()
//    private var state = State.STARTING

    private fun createUI(title: String) {
        setTitle(title)

//        createMenuBar()
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

//        state = State.INPUT_WAITING
        start()
    }

    private fun start() {
        Thread {
            while (true) {
//                if (state === GAME_OVERED) break
//                if (state === GAME_PUSHED) {
//                    synchronized(lock) {
//                        try {
//                            lock.wait()
//                        } catch (e: InterruptedException) {
//                            break
//                        }
//                    }
//                }

                try {
                    Thread.sleep(30)
                } catch (e: InterruptedException) {
                    break
                }
//                when (state) {
//                    GAME_BARRIER_SELECT -> bselector.draw(bufferScreen)
//                    GAME_MAP_SELECT -> {
//                    }
//                    GAME_RUNING -> runningDraw()
//                    GAME_FAILED, GAME_SUCCEED -> if (gresult != null) gresult.draw(bufferScreen)
//                }

                mainPanel.repaint()
            }
        }.start()
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
//        val infoLabel = JLabel("loading...")
//        infoLabel.isFocusable = false
//        headerPanel.add(infoLabel)

        mapPanel.setSize(550, 550)
        mainPanel.add(mapPanel, BorderLayout.WEST)
        mapPanel.add(mapTextPane)
        createMap()

        infoPanel.setSize(250, 550)
        mainPanel.add(infoPanel, BorderLayout.EAST)

//        val hpLabel = JLabel("HP: ")
//        hpLabel.isFocusable = false
//        infoPanel.add(hpLabel, BorderLayout.WEST)
    }

    private fun makeNotFocusable() {
        mainPanel.isFocusable = false
        headerPanel.isFocusable = false
        mapPanel.isFocusable = false
        infoPanel.isFocusable = false
        mapTextPane.isFocusable = false
    }

    private fun createMap() {
        for (i in 0 until map.rowSize) {
            if (i != 0) {
                insertText("\n", mapTextPane.colorMap[MapSymbolColor.BLACK]!!)
            }
            for (j in 0 until map.columnSize) {
                if (i == currPos.row && j == currPos.column) {
                    val style = mapTextPane.colorMap[MapSymbolColor.RED]!!
                    insertText(PLAYER.symbol.toString(), style)
                } else {
                    val symbol = map.rows[i][j]
                    val style = mapTextPane.colorMap[symbol.color]!!
                    insertText(symbol.content.toString(), style)
                }
            }
        }
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

    override fun keyPressed(e: KeyEvent?) {
        if (e != null) {
            println("Some key pressed, current=$moved, keyCode=${e.keyCode}, didMove=$didMove, currPos=$currPos")
        } else {
            println("e == null")
        }
        if (!didMove) {
            if (e == null) {
                moved = MoveEvent.NONE
                return
            }

            // TODO: rewrite to "when (e.keyCode)"
            // note: may be not working when several keys pressed at the same time
            if (e.keyCode == KeyEvent.VK_UP || e.keyCode == KeyEvent.VK_W) {
                if (currPos.row > 0) {
                    val currRow = currPos.row - 1
                    val currColumn = currPos.column
                    val currSymb = map.rows[currRow][currColumn]
                    if (currSymb.content == NONE.symbol) {
                        didMove = true
                        moved = MoveEvent.UP
                        map.rows[currPos.row][currPos.column] = ColoredSymbol(NONE.symbol)
                        map.rows[currRow][currColumn] = ColoredSymbol(PLAYER.symbol, currSymb.color)
                        replaceSymbolAt(currPos.row, currPos.column, NONE.symbol)
                        replaceSymbolAt(currPos.row - 1, currPos.column, PLAYER.symbol)
                        currPos = Position(currPos.row - 1, currPos.column)
                    }
                }
            }
            if (e.keyCode == KeyEvent.VK_DOWN || e.keyCode == KeyEvent.VK_S) {
                val currRow = currPos.row + 1
                val currColumn = currPos.column
                val currSymb = map.rows[currRow][currColumn]
                if (currPos.row < map.rowSize - 1 && currSymb.content == NONE.symbol) {
                    didMove = true
                    moved = MoveEvent.DOWN
                    map.rows[currPos.row][currPos.column] = ColoredSymbol(NONE.symbol)
                    map.rows[currRow][currColumn] = ColoredSymbol(PLAYER.symbol, currSymb.color)
                    replaceSymbolAt(currPos.row, currPos.column, NONE.symbol)
                    replaceSymbolAt(currPos.row + 1, currPos.column, PLAYER.symbol)
                    currPos = Position(currPos.row + 1, currPos.column)
                }
            }
            if (e.keyCode == KeyEvent.VK_LEFT || e.keyCode == KeyEvent.VK_A) {
                if (currPos.column > 0) {
                    val currRow = currPos.row
                    val currColumn = currPos.column - 1
                    val currSymb = map.rows[currRow][currColumn]
                    if (currSymb.content == NONE.symbol) {
                        didMove = true
                        moved = MoveEvent.LEFT
                        map.rows[currPos.row][currPos.column] = ColoredSymbol(NONE.symbol)
                        map.rows[currRow][currColumn] = ColoredSymbol(PLAYER.symbol, currSymb.color)
                        replaceSymbolAt(currPos.row, currPos.column, NONE.symbol)
                        replaceSymbolAt(currPos.row, currPos.column - 1, PLAYER.symbol)
                        currPos = Position(currPos.row, currPos.column - 1)
                    }
                }
            }
            if (e.keyCode == KeyEvent.VK_RIGHT || e.keyCode == KeyEvent.VK_D) {
                val currRow = currPos.row
                val currColumn = currPos.column + 1
                val currSymb = map.rows[currRow][currColumn]
                if (currPos.column < map.columnSize - 1 && currSymb.content == NONE.symbol) {
                    didMove = true
                    moved = MoveEvent.RIGHT
                    map.rows[currPos.row][currPos.column] = ColoredSymbol(NONE.symbol)
                    map.rows[currRow][currColumn] = ColoredSymbol(PLAYER.symbol, currSymb.color)
                    replaceSymbolAt(currPos.row, currPos.column, NONE.symbol)
                    replaceSymbolAt(currPos.row, currPos.column + 1, PLAYER.symbol)
                    currPos = Position(currPos.row, currPos.column + 1)
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
            }
        }
    }

    private fun replaceSymbolAt(row: Int, column: Int, symbol: Char) {
        mapTextPane.isEditable = true
        val index = row * (map.columnSize + 1) + column
        mapTextPane.select(index, index + 1)
        println("selected text='${mapTextPane.selectedText}'")
        mapTextPane.replaceSelection(symbol.toString())
        if (symbol == PLAYER.symbol) {
            mapTextPane.selectedTextColor = Color.RED
        } else {
            mapTextPane.selectedTextColor = Color.BLACK
        }
        mapTextPane.isEditable = false
    }

    override fun keyReleased(e: KeyEvent?) {}
    override fun keyTyped(e: KeyEvent?) {}
}


//private val mapPreviewText = listOf(
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//    "qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09qwert 09",
//)
//
//val mapPreviewSymbols: List<List<Symbol>> =
//    mapPreviewText.map { s ->
//        s.map { c -> Symbol(c, MapSymbolColor.BLACK) }
//    }