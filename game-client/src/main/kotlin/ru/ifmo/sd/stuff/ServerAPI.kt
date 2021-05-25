package ru.ifmo.sd.stuff

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import ru.ifmo.sd.httpapi.models.*
import java.io.IOException
import java.net.ServerSocket


object ServerAPI {
    var client: HttpClient? = null
    private const val MULTIPLAYER_ADDRESS = "http://localhost:8080"
    private val LOCAL_ADDRESS
        get() = "http://localhost:${port}"
    private var mapSize = Pair(4, 4)
    private var port = 8081
    internal var nickname: String? = "null"
    private const val localNickname = "local"

    internal fun initLocalServer() {
        while (true) {
            println("Checking avaliabilty of port=${port}")
            if (isAvailable(port)) {
                break
            } else port++
        }
        println("Trying to start local server at port=${port}")
        val cmd = "nohup ../game-server/build/install/game-server/bin/game-server &" // TODO call .bat for windows
        val env = arrayOf("PORT=${port}", "JAVA_HOME=/Users/macbook/.jenv/versions/1.8") // TODO JAVA_HOME
        val proc = Runtime.getRuntime().exec(cmd, env) // TODO kill it after closing the client
        while (true) {
            var started = true
            try {
                joinLocal()
            } catch (e: Exception) {
                println(e.localizedMessage)
                started = false
            }
            if (started) break
        }
        println("Local server successfully started")
    }

//    internal fun startLocal() {
//        runBlocking {
//            println("apiStartLocal")
//            client!!.post<Unit>("$LOCAL_ADDRESS/start") {
//                contentType(ContentType.Application.Json)
//                body = LevelConfiguration(length = mapSize.first, width = mapSize.second)
//            }
//        }
//    }
//
//    internal fun startMultiplayer() {
//        runBlocking {
//            println("apiStartMult")
//            client!!.post<Unit>("$MULTIPLAYER_ADDRESS/start") {
//                contentType(ContentType.Application.Json)
//                body = LevelConfiguration(length = mapSize.first, width = mapSize.second)
//            }
//        }
//    }

    internal fun joinLocal(): JoinGameInfo {
        return runBlocking {
            println("apiJoinLocal")
            return@runBlocking client!!.post<JoinGameInfo>("$LOCAL_ADDRESS/join") {
                contentType(ContentType.Application.Json)
                body = JoinInfo(localNickname, length = mapSize.first, width = mapSize.second)
            }
        }
    }

    internal fun joinMultiplayer(): JoinGameInfo {
        return runBlocking {
            println("apiJoinMult")
            return@runBlocking client!!.post<JoinGameInfo>("$MULTIPLAYER_ADDRESS/join") {
                contentType(ContentType.Application.Json)
                body = JoinInfo(nickname!!, length = mapSize.first, width = mapSize.second)
            }
        }
    }

    internal fun getGameStateLocal(): GameState {
        return runBlocking {
            return@runBlocking client!!.get<GameState>("$LOCAL_ADDRESS/gameState") {
                parameter("playerName", localNickname)
            }
        }
    }

    internal fun getGameStateMultiplayer(): GameState {
        return runBlocking {
            return@runBlocking client!!.get<GameState>("$MULTIPLAYER_ADDRESS/gameState") {
                parameter("playerName", nickname!!)
            }
        }
    }

    internal fun moveLocal(oldPos: Position, newPos: Position): GameMove {
        return runBlocking {
            println("apiMoveLocal oldPos=$oldPos, newPos=$newPos")
            return@runBlocking client!!.post<GameMove>("$LOCAL_ADDRESS/move") {
                contentType(ContentType.Application.Json)
                body = PlayerPositionChanging(localNickname, oldPos, newPos)
            }
        }
    }

    internal fun moveMultiplayer(oldPos: Position, newPos: Position): GameMove {
        return runBlocking {
            println("apiMoveMult oldPos=$oldPos, newPos=$newPos")
            return@runBlocking client!!.post<GameMove>("$MULTIPLAYER_ADDRESS/move") {
                contentType(ContentType.Application.Json)
                body = PlayerPositionChanging(nickname!!, oldPos, newPos)
            }
        }
    }

    internal fun disconnectLocal(currPos: Position) {
        runBlocking {
            println("apiDisconnectLocal")
            client!!.post<Unit>("$LOCAL_ADDRESS/disconnect") {
                contentType(ContentType.Application.Json)
                body = PlayerInfo(localNickname, currPos)
            }
        }
    }

    internal fun disconnectMultiplayer(currPos: Position) {
        runBlocking {
            println("apiDisconnectMultiplayer")
            client!!.post<Unit>("$MULTIPLAYER_ADDRESS/disconnect") {
                contentType(ContentType.Application.Json)
                body = PlayerInfo(nickname!!, currPos)
            }
        }
    }

    // utilities

    internal fun increaseMapSize() {
        if (mapSize.first < 9) {
            mapSize = Pair(mapSize.first + 1, mapSize.second + 2)
        }
    }

    internal fun resetMapSize() {
        mapSize = Pair(4, 4)
    }

    private fun isAvailable(portNr: Int): Boolean {
        var portFree: Boolean
        try {
            ServerSocket(portNr).use { portFree = true }
        } catch (e: IOException) {
            portFree = false
        }
        return portFree
    }
}