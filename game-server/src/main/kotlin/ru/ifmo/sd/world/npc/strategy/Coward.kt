package ru.ifmo.sd.world.npc.strategy

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.ChangeMazePositionEvent
import ru.ifmo.sd.world.npc.Npc
import ru.ifmo.sd.world.npc.strategy.Strategy.Companion.PlayerDirection
import ru.ifmo.sd.world.representation.Maze
import ru.ifmo.sd.world.representation.units.*

/**
 * Класс, отвечающий трусливой стратегии поведения NPC.
 */
class Coward : Strategy {
    companion object {
        private val north = listOf(
            Position(1, 0),
            Position(0, 1),
            Position(0, -1)
        )
        private val south = listOf(
            Position(-1, 0),
            Position(0, 1),
            Position(0, -1)
        )
        private val west = listOf(
            Position(1, 0),
            Position(0, -1),
            Position(-1, 0)
        )
        private val east = listOf(
            Position(1, 0),
            Position(0, 1),
            Position(1, 0)
        )
    }

    override fun getEnemyFactory(): EnemyFactory {
        return CowardEnemyFactory()
    }

    override fun execute(npc: Npc, playerPos: Position, maze: Maze): MutableSet<ChangeMazePositionEvent> =
        when (val direction = Strategy.findPlayer(npc.position, playerPos, maze)) {
            PlayerDirection.Failed -> HashSet()
            else -> {
                val directionsToMove = getDirections(direction)
                val randomPos = Strategy.randomDirection(npc.position, directionsToMove, maze)
                if (randomPos != Position(0, 0)) {
                    val oldNpcPos = npc.position
                    npc.position = npc.position + randomPos
                    mutableSetOf(
                        ChangeMazePositionEvent(oldNpcPos, null),
                        ChangeMazePositionEvent(npc.position, getEnemyFactory().getEnemy())
                    )
                } else {
                    HashSet()
                }
            }
        }

    private fun getDirections(direction: PlayerDirection): List<Position> =
        when (direction) {
            PlayerDirection.North -> north
            PlayerDirection.South -> south
            PlayerDirection.West -> west
            PlayerDirection.East -> east
            PlayerDirection.Failed -> emptyList()
        }
}
