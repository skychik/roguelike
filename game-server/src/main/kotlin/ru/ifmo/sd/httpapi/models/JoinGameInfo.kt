package ru.ifmo.sd.httpapi.models

import kotlinx.serialization.Serializable

@Serializable
data class MazeData(
    val levelMaze: Array<Array<Int>>
)

@Serializable
data class JoinGameInfo(
    val playerPos: Position,
    val maze: MazeData,
    val unitsHealth: List<Pair<Position, Int>>
)
