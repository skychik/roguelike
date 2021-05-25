package ru.ifmo.sd.httpapi.models

import kotlinx.serialization.Serializable

@Serializable
data class PlayerInfo(
    val name: String,
    val position: Position
)
