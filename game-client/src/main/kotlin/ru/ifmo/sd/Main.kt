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
import ru.ifmo.sd.httpapi.models.MoveEventData
import ru.ifmo.sd.stuff.GUI
import ru.ifmo.sd.world.configuration.GameConfiguration
import ru.ifmo.sd.world.configuration.GameConfigurationSerializable
import ru.ifmo.sd.world.representation.Position
import ru.ifmo.sd.world.representation.units.GameUnit
import ru.ifmo.sd.world.representation.units.Player
import java.awt.EventQueue


private var client: HttpClient? = null

suspend fun main() {
    client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }
    val response = makeNewGameConfiguration()

    println(response.deserializeBack())

    EventQueue.invokeLater { createAndShowGUI(response.deserializeBack()) }

//    client.close()
}

internal suspend fun makeNewGameConfiguration(): GameConfigurationSerializable {
    return client!!.post("http://localhost:8080/start") {
        contentType(ContentType.Application.Json)
        body = LevelConfiguration(levelLength = 3, levelWidth = 3)
    }
}

internal suspend fun makeMove(newPos: Position, unit: GameUnit = Player(0)) {
    client!!.post<String>("http://localhost:8080/move") {
        contentType(ContentType.Application.Json)
        body = MoveEventData(unit, newPos)
    }
}

private fun createAndShowGUI(config: GameConfiguration) {
    val frame = GUI("Roguelike", config)
    frame.isVisible = true
}