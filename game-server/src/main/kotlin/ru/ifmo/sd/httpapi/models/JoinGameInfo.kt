package ru.ifmo.sd.httpapi.models

import kotlinx.serialization.Serializable

@Serializable
data class MazeData(
    val levelMaze: Array<Array<Int>>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MazeData) return false

        if (!levelMaze.contentDeepEquals(other.levelMaze)) return false

        return true
    }

    override fun hashCode(): Int {
        return levelMaze.contentDeepHashCode()
    }
}

@Serializable
data class JoinGameInfo(
    val playerPos: Position,
    val maze: MazeData,
    val unitsHealth: List<Pair<Position, Int>>
)
