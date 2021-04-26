package ru.ifmo.sd.httpapi.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.ifmo.sd.httpapi.models.LevelConfiguration
import ru.ifmo.sd.httpapi.models.MoveEventData
import ru.ifmo.sd.world.events.EventsHandler

fun Route.gameRouting() {
    route("/") {
        post("/start") {
            val levelConfiguration = call.receive<LevelConfiguration>()
            val startedGame = EventsHandler.startGame(levelConfiguration)
            call.respond(message = startedGame.makeSerializable(), status = HttpStatusCode.Created)
        }

        post("/move") {
            val moveEventData = call.receive<MoveEventData>()
            EventsHandler.move(moveEventData.targetUnit, moveEventData.newPos)
            call.respondText("Position successfully updated", status = HttpStatusCode.Accepted)
        }
    }
}

fun Application.registerGameRoutes() {
    routing {
        gameRouting()
    }
}
