package ru.ifmo.sd.client

import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JFrame

enum class MoveEvent {
    UP, DOWN, LEFT, RIGHT, NONE
}

open class MyKeyListener : KeyAdapter() {
    var moved = MoveEvent.NONE
    var didMove = false

    override fun keyPressed(e: KeyEvent?) {
        if (e != null) {
            println("Some key pressed, current=$moved, keyCode=${e.keyCode}, didMove=$didMove")
        } else {
            println("e == null")
        }
        if (!didMove) {
            if (e == null) {
                moved = MoveEvent.NONE
                return
            }
            // note: may be not working when several keys pressed at the same time
            if (e.keyCode == KeyEvent.VK_UP || e.keyCode == KeyEvent.VK_W) moved = MoveEvent.UP
            if (e.keyCode == KeyEvent.VK_DOWN || e.keyCode == KeyEvent.VK_S) moved = MoveEvent.DOWN
            if (e.keyCode == KeyEvent.VK_LEFT || e.keyCode == KeyEvent.VK_A) moved = MoveEvent.LEFT
            if (e.keyCode == KeyEvent.VK_RIGHT || e.keyCode == KeyEvent.VK_D) moved = MoveEvent.RIGHT
            didMove = true

            println("Key pressed=$moved")
        }
    }
}