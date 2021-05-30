package ru.ifmo.sd.world.npc.strategy

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.ChangeMazePositionEvent
import ru.ifmo.sd.world.events.EventsHandler
import ru.ifmo.sd.world.npc.Npc
import ru.ifmo.sd.world.npc.strategy.Strategy.Companion.PlayerDirection
import ru.ifmo.sd.world.representation.Maze
import ru.ifmo.sd.world.representation.units.*
import kotlin.math.abs


/**
 * Класс, отвечающий агрессивной стратегии поведения NPC.
 */
class Aggressive : Strategy {
    private fun isAdjacent(npcPos: Position, playerPos: Position): Boolean {
        return abs(npcPos.row - playerPos.row) == 1 && abs(npcPos.column - playerPos.column) == 0
            || abs(npcPos.row - playerPos.row) == 0 && abs(npcPos.column - playerPos.column) == 1
    }

    override fun getEnemyFactory(): EnemyFactory {
        return AggressiveEnemyFactory()
    }

    override fun execute(npc: Npc, playerPos: Position, maze: Maze): MutableSet<ChangeMazePositionEvent> =
        when (val direction = Strategy.findPlayer(npc.position, playerPos, maze)) {
            PlayerDirection.Failed ->
                HashSet()
            else ->
                if (isAdjacent(npc.position, playerPos)) {
                    maze[playerPos]!!.interact(EventsHandler.interactionExecutor, playerPos)
                } else {
                    val directionsToMove = getDirection(direction)
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

    private fun getDirection(direction: PlayerDirection): List<Position> =
        listOf(when (direction) {
            PlayerDirection.North -> Position(-1, 0)
            PlayerDirection.South -> Position(1, 0)
            PlayerDirection.West -> Position(0, 1)
            PlayerDirection.East -> Position(0, -1)
            PlayerDirection.Failed -> Position(0, 0)
        })
}
