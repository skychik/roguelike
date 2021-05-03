package ru.ifmo.sd.world.npc.strategy

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.EventsHandler
import ru.ifmo.sd.world.events.ChangeMazePositionEvent
import ru.ifmo.sd.world.representation.units.Enemy
import kotlin.math.abs


/**
 * Класс, отвечащий агрессивной стратегии поведения NPC.
 */
class Aggressive : Strategy {
    private fun isAdjacent(npcPos: Position, playerPos: Position): Boolean {
        return abs(npcPos.row - playerPos.row) == 1 && abs(npcPos.column - playerPos.column) == 0
            || abs(npcPos.row - playerPos.row) == 0 && abs(npcPos.column - playerPos.column) == 1
    }

    override fun execute(npcPos: Position, playerPos: Position): MutableSet<ChangeMazePositionEvent> {
        val player = EventsHandler.gameLevel!!.maze[playerPos]!!
        return when (Strategy.findPlayer(npcPos, playerPos)) {
            Strategy.Companion.PlayerDirection.North ->
                if (isAdjacent(npcPos, playerPos)) {
                    player.interact(EventsHandler.interactionExecutor, playerPos)
                } else {
                    mutableSetOf(
                        ChangeMazePositionEvent(npcPos, null),
                        ChangeMazePositionEvent(npcPos + Position(-1, 0), Enemy())
                    )
                }

            Strategy.Companion.PlayerDirection.South ->
                if (isAdjacent(npcPos, playerPos)) {
                    player.interact(EventsHandler.interactionExecutor, playerPos)
                } else {
                    mutableSetOf(
                        ChangeMazePositionEvent(npcPos, null),
                        ChangeMazePositionEvent(npcPos + Position(1, 0), Enemy())
                    )
                }

            Strategy.Companion.PlayerDirection.West ->
                if (isAdjacent(npcPos, playerPos)) {
                    player.interact(EventsHandler.interactionExecutor, playerPos)
                } else {
                    mutableSetOf(
                        ChangeMazePositionEvent(npcPos, null),
                        ChangeMazePositionEvent(npcPos + Position(0, 1), Enemy())
                    )
                }

            Strategy.Companion.PlayerDirection.East ->
                if (isAdjacent(npcPos, playerPos)) {
                    player.interact(EventsHandler.interactionExecutor, playerPos)
                } else {
                    mutableSetOf(
                        ChangeMazePositionEvent(npcPos, null),
                        ChangeMazePositionEvent(npcPos + Position(0, -1), Enemy())
                    )
                }

            Strategy.Companion.PlayerDirection.Failed ->
                HashSet()
        }
    }
}
