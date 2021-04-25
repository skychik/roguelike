package ru.ifmo.sd.httpapi

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import ru.ifmo.sd.httpapi.routes.registerGameRoutes

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        json()
    }
    registerGameRoutes()
}
