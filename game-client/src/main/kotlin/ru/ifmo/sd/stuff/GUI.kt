package ru.ifmo.sd.stuff

import ru.ifmo.sd.httpapi.models.*
import ru.ifmo.sd.stuff.ColoredSymbol.*
import java.awt.BorderLayout
import java.awt.Color.*
import java.awt.Container
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.*
import java.awt.event.KeyListener
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*
import javax.swing.text.Style

enum class MoveEvent {
    UP, DOWN, LEFT, RIGHT, NONE
}

class GUI(title: String, gameConfiguration: JoinGameInfo) : JFrame(), KeyListener {
    private var prevPos: Position? = null // TODO: костыль
    private var currPos = gameConfiguration.playerPos
        set(value) {
            prevPos = currPos
            field = value
        }
    private var isDead = false
    private val mainPanel = JPanel()
    private val headerPanel = JPanel()
    private val mapPanel = JPanel()
    private val infoPanel = JPanel()
    private val infoLabel = JLabel("")
    private var map = SymbolMap(gameConfiguration)
    private var mapTextPane = MapTextPane()
    private var isMultiplayer = true
    private var currPlayer: String? = null
    private var prevGameState: GameState? = null

    init {
        createUI(title)
        gameStateThread()
        startRepaintThread()
    }

    private fun createUI(title: String) {
        setTitle(title)

        createLayout()
        createMenuBar()
        makeNotFocusable()

        defaultCloseOperation = EXIT_ON_CLOSE
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                try {
                    disconnect(currPos)
                } catch (e: Exception) {
                    println(e.localizedMessage)
                }
                ServerAPI.client!!.close()
                e.window.dispose()
            }
        })
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

    private fun startRepaintThread() {
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

    private fun gameStateThread() {
        Thread {
            while (true) {
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    break
                }
                if (!isYourTurn()) {
                    val gameState = getGameState()
                    if (prevGameState != gameState) {
                        println("New state $gameState")
                        prevGameState = gameState
                        currPlayer = gameState.currentMovePlayerName
                        isDead = !gameState.isAlive
                        infoLabel.text = "$currPlayer is making a move"
                        map = SymbolMap(gameState)
                        reloadMapTextPane()
                    }
                } else {
                    infoLabel.text = "Your turn!"
                }
            }
        }.start()
    }

    private fun createLayout() {
        add(mainPanel)
        mainPanel.layout = BorderLayout()

        headerPanel.setSize(800, 50)
        mainPanel.add(headerPanel, BorderLayout.NORTH)
        headerPanel.add(infoLabel)


        mapPanel.setSize(550, 550)
        mainPanel.add(mapPanel, BorderLayout.WEST)
        mapPanel.add(mapTextPane)
        reloadMapTextPane()

        infoPanel.setSize(250, 550)
        mainPanel.add(infoPanel, BorderLayout.EAST)

//        val hpLabel = JLabel("HP: ")
//        infoPanel.add(hpLabel, BorderLayout.WEST)
    }

    private fun createMenuBar() {
        val menubar = JMenuBar()
        val menu = JMenu("Menu")
//        file.mnemonic = KeyEvent.VK_ESCAPE

        val multiplayerItem = if (isMultiplayer) {
            val item = JMenuItem("Exit multiplayer")
            item.addActionListener {
                proceedChangingGameMod()
            }
            item
        } else {
            val item = JMenuItem("Join multiplayer")
            item.addActionListener {
                var shouldChooseAnotherNickname = false
                while (true) {
                    val message = "Type your nickname" + if (shouldChooseAnotherNickname) " (choose another one)" else ""
                    ServerAPI.nickname = JOptionPane.showInputDialog(this, message)
                    var success = true
                    try {
                        join()
                    } catch (e: Exception) {
                        success = false
                        shouldChooseAnotherNickname = true
                    }
                    if (success) break
                }
                proceedChangingGameMod()
            }
            item
        }

        val restartItem = JMenuItem("Restart")
//        eMenuItem.mnemonic = KeyEvent.VK_R
        restartItem.toolTipText = "Restart game"
        restartItem.addActionListener {
            isDead = false
            infoLabel.text = ""
            remakeMap(restart = true)
        }

        menu.add(multiplayerItem)
        menu.add(restartItem)
        menubar.add(menu)

        jMenuBar = menubar
    }

    private fun makeNotFocusable(container: Container = this) {
        container.isFocusable = false
        for (c in container.components) {
            if (c is Container) makeNotFocusable(c)
        }
    }

    private fun reloadMapTextPane() {
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
        if (isDead) return
        if (e != null) {
            println("Some key pressed, current=$moved, keyCode=${e.keyCode}, didMove=$didMove, currPos=$currPos")
        } else {
            println("e == null")
        }
        if (!didMove && isYourTurn()) {
            var newPos = currPos
            when (e?.keyCode) {
                null -> {
                    moved = MoveEvent.NONE
                }
                VK_UP, VK_W -> {
                    newPos = Position(currPos.row - 1, currPos.column)
                    if (checkMoveIsValid(currPos, newPos)) {
                        didMove = true
                        moved = MoveEvent.UP
                        currPos = newPos
                    }
                }
                VK_DOWN, VK_S -> {
                    newPos = Position(currPos.row + 1, currPos.column)
                    if (checkMoveIsValid(currPos, newPos)) {
                        didMove = true
                        moved = MoveEvent.DOWN
                        currPos = newPos
                    }
                }
                VK_LEFT, VK_A -> {
                    newPos = Position(currPos.row, currPos.column - 1)
                    if (checkMoveIsValid(currPos, newPos)) {
                        didMove = true
                        moved = MoveEvent.LEFT
                        currPos = newPos
                    }
                }
                VK_RIGHT, VK_D -> {
                    newPos = Position(currPos.row, currPos.column + 1)
                    if (checkMoveIsValid(currPos, newPos)) {
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

                if ((currPos.row == map.rowSize - 1 && currPos.column == map.columnSize - 2) ||
                    (currPos.row == 0 && currPos.column == 1)) {
                    // stepped away from the level
                    remakeMap()
                } else {
                    val gameMove = move(prevPos!!, currPos)
                    currPlayer = null
                    println("gameMove=$gameMove")
                    if (gameMove.playerPosition == Position(-1, -1)) {
                        // player is dead
                        isDead = true
                        infoLabel.text = "You are dead!"
                        replacePaneSymbol(prevPos!!, NONE)
                    } else {
                        // player moved
                        replaceSymbol(prevPos!!, gameMove.playerPosition)
                        map.applyDiff(gameMove.events)
                        if (map.enemyAmount == 0) {
                            ServerAPI.increaseMapSize()
                            remakeMap()
                        } else {
                            // reload pane
                            val prevPosSaved = prevPos
                            currPos = gameMove.playerPosition
                            prevPos = prevPosSaved
                            reloadMapTextPane()
                        }
                    }
                }
            }
        }
    }

    private fun remakeMap(restart: Boolean = false) {
        disconnect(currPos)
        if (restart) {
            ServerAPI.resetMapSize()
        }
//        start() // may break previous player's progress
        val newConfig = join()
        map = SymbolMap(newConfig)
        val prevPosSaved = prevPos
        currPos = newConfig.playerPos
        prevPos = prevPosSaved
        reloadMapTextPane()
    }

    private fun proceedChangingGameMod() {
        disconnect(currPos)
        isMultiplayer = !isMultiplayer
        val newConfig = join()
        map = SymbolMap(newConfig)
        val prevPosSaved = prevPos
        currPos = newConfig.playerPos
        prevPos = prevPosSaved
        reloadMapTextPane()
    }

    private fun checkMoveIsValid(oldPos: Position, newPos: Position): Boolean {
        return newPos.row < map.rowSize && newPos.row >= 0 &&
                newPos.column < map.columnSize && newPos.column >= 0 &&
                map.rows[newPos.row][newPos.column] != WALL && map.rows[newPos.row][newPos.column] != PLAYER
    }

    private fun replaceSymbol(oldPos: Position, newPos: Position, replaceSymb: ColoredSymbol = NONE): Boolean {
        if (checkMoveIsValid(oldPos, newPos)) {
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

    /////////////////// ServerAPI helpers

    private fun join(): JoinGameInfo {
        return if (isMultiplayer) ServerAPI.joinMultiplayer()
        else ServerAPI.joinLocal()
    }

    private fun getGameState(): GameState {
        return if (isMultiplayer) ServerAPI.getGameStateMultiplayer()
        else ServerAPI.getGameStateLocal()
    }

    private fun move(oldPos: Position, newPos: Position): GameMove {
        return if (isMultiplayer) ServerAPI.moveMultiplayer(oldPos, newPos)
        else ServerAPI.moveLocal(oldPos, newPos)
    }

    private fun disconnect(currPos: Position) {
        if (isMultiplayer) ServerAPI.disconnectMultiplayer(currPos)
        else ServerAPI.disconnectLocal(currPos)
    }

    private fun isYourTurn(): Boolean {
        return currPlayer == ServerAPI.nickname
    }
}