package ru.ifmo.sd.stuff

import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import ru.ifmo.sd.client
import ru.ifmo.sd.httpapi.models.*

object ServerAPI {
    const val address = "http://localhost:8080"
    private var mapSize = Pair(4, 4)

    internal fun increaseMapSize() {
        if (mapSize.first < 9) {
            mapSize = Pair(mapSize.first + 1, mapSize.second + 2)
        }
    }

    internal fun resetMapSize() {
        mapSize = Pair(4, 4)
    }

    internal fun join(): JoinGameInfo {
        return runBlocking {
            println("apiJoin")
            return@runBlocking client!!.post<JoinGameInfo>("$address/join") {
                contentType(ContentType.Application.Json)
                body = LevelConfiguration(length = mapSize.first, width = mapSize.second)
            }
        }
    }

    internal fun move(oldPos: Position, newPos: Position): GameMove {
        return runBlocking {
            println("apiMove oldPos=$oldPos, newPos=$newPos")
            return@runBlocking client!!.post<GameMove>("$address/move") {
                contentType(ContentType.Application.Json)
                body = PlayerMove(oldPos, newPos)
            }
        }
    }

    internal fun restart() {
        runBlocking {
            println("apiRestart")
            client!!.get<String>("$address/restart")
        }
    }
}