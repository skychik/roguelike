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
        get("/gameState") {
            val playerName = call.request.queryParameters["playerName"]
            if (playerName == null) {
                call.respond(HttpStatusCode.BadRequest, "Cannot get game state.")
            } else {
                val gameState = try {
                    EventsHandler.getActualGameState(playerName)
                } catch (e: GameServerException) {
                    call.respond(HttpStatusCode.BadRequest, e.localizedMessage)
                }
                call.respond(HttpStatusCode.Accepted, gameState)
            }
        }

        post("/join") {
            val joinInfo = call.receive<JoinInfo>()
            val startedGame = try {
                EventsHandler.join(joinInfo.playerName, joinInfo.length, joinInfo.width)
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
