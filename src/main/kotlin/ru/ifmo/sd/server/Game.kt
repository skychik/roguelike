package ru.ifmo.sd.server

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.random.Random

class Game {
    fun initialize(reset: Boolean = true) {
        // TODO
    }

    fun run() {
        while (true) {
            runBlocking { delay(10) }
            logic()
        }
    }

    private fun logic() {
//        moveNpcs()
//        pickItems()
    }
}