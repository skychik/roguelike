package ru.ifmo.sd

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import ru.ifmo.sd.client.GUI
import ru.ifmo.sd.server.Server
import java.awt.EventQueue


suspend fun main() {
    startClient()
//    startServer()
}

private fun startServer() {
    Server.start()
}

private suspend fun startClient() {
    EventQueue.invokeLater(::createAndShowGUI)
//    startHttpClient()
}

private fun createAndShowGUI() {
    val frame = GUI("Roguelike")
    frame.isVisible = true
}

private suspend fun startHttpClient() {
    val client = HttpClient(CIO)
    val response: HttpResponse = client.get("https://ktor.io/")
    println(response.status)
    client.close()
}