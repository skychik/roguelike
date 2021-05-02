package ru.ifmo.sd.world.npc.strategy

import ru.ifmo.sd.world.events.MazeEvent
import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.EventsHandler
import ru.ifmo.sd.world.npc.strategy.Strategy.Companion.PlayerDirection.Failed


interface Strategy {
    fun execute(npcPos: Position, playerPos: Position): MutableSet<MazeEvent>

    companion object {
        enum class PlayerDirection {
            North, South, West, East, Failed;
        }

        // Bresenham's algorithm, see en.wikipedia.org/wiki/File:Bresenham.svg
        fun findPlayer(npcPos: Position, playerPos: Position): PlayerDirection {
            // TODO: implement Bresenham's algorithm
            return Failed
        }

        fun randomMove(positions: List<Position>): Position {
            val availablePos = positions.filter { EventsHandler.gameLevel!!.maze[it] == null }
            return if (availablePos.isNotEmpty()) availablePos.random() else Position(0, 0)
        }
    }
}
