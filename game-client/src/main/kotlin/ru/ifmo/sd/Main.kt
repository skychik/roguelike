package ru.ifmo.sd

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import ru.ifmo.sd.httpapi.models.*
import ru.ifmo.sd.stuff.GUI
import ru.ifmo.sd.stuff.ServerAPI
import java.awt.EventQueue
import javax.swing.JFrame
import javax.swing.JOptionPane


fun main() {
    ServerAPI.client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }
//    ServerAPI.initLocalServer()
    val response = inputName()
    println(response)
    EventQueue.invokeLater { createAndShowGUI(response) }
}

private fun createAndShowGUI(config: JoinGameInfo) {

    val frame = GUI("Roguelike", config)
    frame.isVisible = true
}

private fun inputName(): JoinGameInfo {
    val frame = JFrame()
    frame.isVisible = true
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.setLocationRelativeTo(null)

    var shouldChooseAnotherNickname = false
    var response: JoinGameInfo? = null
    while (true) {
        val message = "Type your nickname" + if (shouldChooseAnotherNickname) " (choose another one)" else ""
        ServerAPI.nickname = JOptionPane.showInputDialog(frame, message)
        var success = true
        try {
            response = ServerAPI.joinMultiplayer()
        } catch (e: Exception) {
            println(e.localizedMessage)
            success = false
            shouldChooseAnotherNickname = true
        }
        if (success) break
    }
    frame.isVisible = false
    return response!!
}