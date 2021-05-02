package ru.ifmo.sd.httpapi.models

import kotlinx.serialization.Serializable

@Serializable
data class MazeEventData(
    val position: Position,
    val newMazeObj: Int
)

@Serializable
data class GameMove(
    val playerPosition: Position,
    val events: List<MazeEventData>
)
