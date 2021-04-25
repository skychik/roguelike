package ru.ifmo.sd.client

import ru.ifmo.sd.stuff.MapSymbolColor
import ru.ifmo.sd.stuff.Symbol
import ru.ifmo.sd.stuff.SymbolMap
import java.awt.BorderLayout
import java.awt.event.KeyEvent
import javax.swing.*
import javax.swing.text.Style



class GUI(title: String) : JFrame() {
    private enum class State {
        STARTING, INPUT_WAITING, PENDING_REQUEST
    }

    private val mainPanel = JPanel()
    private val headerPanel = JPanel()
    private val mapPanel = JPanel()
    private val infoPanel = JPanel()
    private var map = SymbolMap(mapPreviewSymbols)
    private var keyListener = MyKeyListener()

    private var lock = Object()
    private var state = State.STARTING

    init {
        createUI(title)
    }

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
        this.addKeyListener(keyListener)

        requestFocus()
        requestFocusInWindow()

        state = State.INPUT_WAITING
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
        val infoLabel = JLabel("loading...")
        infoLabel.isFocusable = false
        headerPanel.add(infoLabel)

        mapPanel.setSize(550, 550)
        mainPanel.add(mapPanel, BorderLayout.WEST)
        mapPanel.add(MapTextPane)
        createMap()

        infoPanel.setSize(250, 550)
        mainPanel.add(infoPanel, BorderLayout.EAST)

        val hpLabel = JLabel("HP: ")
        hpLabel.isFocusable = false
        infoPanel.add(hpLabel, BorderLayout.WEST)
    }

    private fun makeNotFocusable() {
        mainPanel.isFocusable = false
        headerPanel.isFocusable = false
        mapPanel.isFocusable = false
        infoPanel.isFocusable = false
        MapTextPane.isFocusable = false
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