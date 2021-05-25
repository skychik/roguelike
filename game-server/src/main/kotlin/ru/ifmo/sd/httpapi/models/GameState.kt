package ru.ifmo.sd.httpapi.models

import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val currentMovePlayerName: String,
    val isAlive: Boolean,
    val maze: MazeData,
    val unitsHealth: List<Pair<Position, Int>>
)
