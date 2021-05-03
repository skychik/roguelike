package ru.ifmo.sd

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import ru.ifmo.sd.httpapi.models.*
import ru.ifmo.sd.stuff.GUI
import java.awt.EventQueue


internal var client: HttpClient? = null

suspend fun main() {
    client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }
    val response = apiJoin()

    println(response)

    EventQueue.invokeLater { createAndShowGUI(response) }
}

internal suspend fun apiJoin(): JoinGameInfo {
    return client!!.post("http://localhost:8080/join") {
        contentType(ContentType.Application.Json)
        body = LevelConfiguration(length = 3, width = 3)
    }
}

internal suspend fun apiMove(oldPos: Position, newPos: Position): GameMove {
    println("apiMove oldPos=$oldPos, newPos=$newPos")
    return client!!.post("http://localhost:8080/move") {
        contentType(ContentType.Application.Json)
        body = PlayerMove(oldPos, newPos)
    }
}

private fun createAndShowGUI(config: JoinGameInfo) {
    val frame = GUI("Roguelike", config)
    frame.isVisible = true
}