package ru.ifmo.sd.httpapi.models

import kotlinx.serialization.Serializable

@Serializable
data class PlayerPositionChanging(
    val name: String,
    val oldPosition: Position,
    val newPosition: Position
)
