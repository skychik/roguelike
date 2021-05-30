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

private val inputNameFrame = JFrame()

fun main() {
    ServerAPI.client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }
//    ServerAPI.initLocalServer()
    val response = inputName()
    println(ServerAPI.nickname)
    if (ServerAPI.nickname == null) {
        ServerAPI.client!!.close()
        inputNameFrame.dispose()
    } else {
        println(response)
        EventQueue.invokeLater { createAndShowGUI(response!!) }
    }
}

private fun createAndShowGUI(config: JoinGameInfo) {

    val frame = GUI("Roguelike", config)
    frame.isVisible = true
}

private fun inputName(): JoinGameInfo? {
    inputNameFrame.isVisible = true
    inputNameFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    inputNameFrame.setLocationRelativeTo(null)

    var isNameTaken = false
    var isNameValid = true
    var response: JoinGameInfo? = null
    while (true) {
        val message = "Type your nickname" +
                if (!isNameValid) " (name should contain latin letters or digits)"
                else if (isNameTaken) " (choose another one)"
                else ""
        ServerAPI.nickname = JOptionPane.showInputDialog(inputNameFrame, message)

        isNameValid = validateNickname(ServerAPI.nickname)
        if (!isNameValid) continue

        var success = true
        try {
            response = ServerAPI.joinMultiplayer()
        } catch (e: Exception) {
            println(e.localizedMessage)
            success = false
            isNameTaken = true
        }
        if (success) break
    }
    inputNameFrame.isVisible = false
    return response
}

private fun validateNickname(name: String?): Boolean {
    return name != null && name.length < 20 && name.matches("[a-zA-Z0-9]+".toRegex())
}