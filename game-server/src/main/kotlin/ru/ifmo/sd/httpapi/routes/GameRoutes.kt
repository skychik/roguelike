package ru.ifmo.sd.httpapi.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.ifmo.sd.httpapi.models.*
import ru.ifmo.sd.world.errors.GameServerException
import ru.ifmo.sd.world.events.EventsHandler

fun Route.gameRouting() {
    route("/") {
        post("/start") {
            val levelConfiguration = call.receive<LevelConfiguration>()
            EventsHandler.startGame(levelConfiguration)
        }

        get("/gameState") {
            val gameState = try {
                EventsHandler.getActualGameState()
            } catch (e: GameServerException) {
                call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
            }
            call.respond(HttpStatusCode.Accepted, gameState)
        }

        get("/join") {
            val newPlayerName = call.request.queryParameters["playerName"]
            if (newPlayerName == null) {
                call.respond(HttpStatusCode.BadRequest, "Could not get player name.")
            }
            val startedGame = try {
                EventsHandler.join(newPlayerName!!)
            } catch (e: GameServerException) {
                call.respond(message = e.localizedMessage, status = HttpStatusCode.BadRequest)
            }
            call.respond(message = startedGame, status = HttpStatusCode.Created)
        }

        post("/disconnect") {
            val playerInfo = call.receive<PlayerInfo>()
            val startedGame = try {
                EventsHandler.disconnect(playerInfo.name, playerInfo.position)
            } catch (e: GameServerException) {
                call.respond(message = e.localizedMessage, status = HttpStatusCode.BadRequest)
            }
            call.respond(message = startedGame, status = HttpStatusCode.Created)
        }

        post("/move") {
            val playerMove = call.receive<PlayerPositionChanging>()
            val move = try {
                EventsHandler.move(
                    playerMove.name, playerMove.oldPosition,
                    playerMove.newPosition, EventsHandler.gameLevel!!
                )
            } catch (e: GameServerException) {
                call.respond(message = e.localizedMessage, status = HttpStatusCode.BadRequest)
            }
            call.respond(message = move, status = HttpStatusCode.Accepted)
        }
    }
}

fun Application.registerGameRoutes() {
    routing {
        gameRouting()
    }
}
