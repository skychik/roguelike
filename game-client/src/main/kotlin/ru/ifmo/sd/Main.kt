package ru.ifmo.sd

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import ru.ifmo.sd.httpapi.models.LevelConfiguration
import ru.ifmo.sd.stuff.GUI
import ru.ifmo.sd.world.configuration.GameConfiguration
import ru.ifmo.sd.world.configuration.GameConfigurationSerializable
import ru.ifmo.sd.world.representation.units.GameUnit
import java.awt.EventQueue


suspend fun main() {
    val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }
    val response = client.post<GameConfigurationSerializable>("http://localhost:8080/start") {
        contentType(ContentType.Application.Json)
        body = LevelConfiguration(levelLength = 15, levelWidth = 30)
    }

    println(response.deserializeBack())

    EventQueue.invokeLater { createAndShowGUI(response.deserializeBack()) }

//    client.close()
}

private fun createAndShowGUI(config: GameConfiguration) {
    val frame = GUI("Roguelike", config)
    frame.isVisible = true
}