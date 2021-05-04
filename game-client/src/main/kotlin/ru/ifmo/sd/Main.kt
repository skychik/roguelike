package ru.ifmo.sd

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import ru.ifmo.sd.httpapi.models.*
import ru.ifmo.sd.stuff.GUI
import ru.ifmo.sd.stuff.ServerAPI
import java.awt.EventQueue


internal var client: HttpClient? = null

fun main() {
    client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }
    ServerAPI.restart() // to clear all previous data
    val response = ServerAPI.join()

    println(response)

    EventQueue.invokeLater { createAndShowGUI(response) }
}

private fun createAndShowGUI(config: JoinGameInfo) {
    val frame = GUI("Roguelike", config)
    frame.isVisible = true
}