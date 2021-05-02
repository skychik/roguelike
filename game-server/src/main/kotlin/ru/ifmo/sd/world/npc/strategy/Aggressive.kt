package ru.ifmo.sd.world.npc.strategy

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.EventsHandler
import ru.ifmo.sd.world.events.MazeEvent
import ru.ifmo.sd.world.representation.units.Enemy
import kotlin.math.abs

class Aggressive : Strategy {
    private fun isNearby(npcPos: Position, playerPos: Position): Boolean {
        return abs(npcPos.row - playerPos.row) == 1 && abs(npcPos.column - playerPos.column) == 0
            || abs(npcPos.row - playerPos.row) == 0 && abs(npcPos.column - playerPos.column) == 1
    }

    override fun execute(npcPos: Position, playerPos: Position): MutableSet<MazeEvent> {
        return when (Strategy.findPlayer(npcPos, playerPos)) {
            Strategy.Companion.PlayerDirection.North ->
                if (isNearby(npcPos, playerPos)) {
                    EventsHandler.gameLevel!!.maze[playerPos]!!.interact(EventsHandler.interactionExecutor, playerPos)
                } else {
                    mutableSetOf(MazeEvent(npcPos, null), MazeEvent(npcPos + Position(-1, 0), Enemy()))
                }

            Strategy.Companion.PlayerDirection.South ->
                if (isNearby(npcPos, playerPos)) {
                    EventsHandler.gameLevel!!.maze[playerPos]!!.interact(EventsHandler.interactionExecutor, playerPos)
                } else {
                    mutableSetOf(MazeEvent(npcPos, null), MazeEvent(npcPos + Position(1, 0), Enemy()))
                }

            Strategy.Companion.PlayerDirection.West ->
                if (isNearby(npcPos, playerPos)) {
                    EventsHandler.gameLevel!!.maze[playerPos]!!.interact(EventsHandler.interactionExecutor, playerPos)
                } else {
                    mutableSetOf(MazeEvent(npcPos, null), MazeEvent(npcPos + Position(0, 1), Enemy()))
                }

            Strategy.Companion.PlayerDirection.East ->
                if (isNearby(npcPos, playerPos)) {
                    EventsHandler.gameLevel!!.maze[playerPos]!!.interact(EventsHandler.interactionExecutor, playerPos)
                } else {
                    mutableSetOf(MazeEvent(npcPos, null), MazeEvent(npcPos + Position(0, -1), Enemy()))
                }

            Strategy.Companion.PlayerDirection.Failed ->
                HashSet()
        }
    }
}
