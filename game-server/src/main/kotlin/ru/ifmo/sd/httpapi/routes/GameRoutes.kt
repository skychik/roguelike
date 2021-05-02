package ru.ifmo.sd.httpapi.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.ifmo.sd.httpapi.models.LevelConfiguration
import ru.ifmo.sd.httpapi.models.PlayerMove
import ru.ifmo.sd.world.events.EventsHandler

fun Route.gameRouting() {
    route("/") {
        post("/join") {
            val levelConfiguration = call.receive<LevelConfiguration>()
            val startedGame = EventsHandler.join(levelConfiguration)
            call.respond(message = startedGame, status = HttpStatusCode.Created)
        }

        post("/move") {
            val playerMove = call.receive<PlayerMove>()
            val move = EventsHandler.move(playerMove.oldPosition, playerMove.newPosition)
            call.respond(message = move, status = HttpStatusCode.Accepted)
        }
    }
}

fun Application.registerGameRoutes() {
    routing {
        gameRouting()
    }
}
