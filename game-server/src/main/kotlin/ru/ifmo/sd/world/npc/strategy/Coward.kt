package ru.ifmo.sd.world.npc.strategy

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.ChangeMazePositionEvent
import ru.ifmo.sd.world.representation.Maze
import ru.ifmo.sd.world.representation.units.Enemy

/**
 * Класс, отвечащий трусливой стратегии поведения NPC.
 */
class Coward : Strategy {
    override fun execute(npcPos: Position, playerPos: Position, maze: Maze): MutableSet<ChangeMazePositionEvent> {
        return when (Strategy.findPlayer(npcPos, playerPos, maze)) {
            Strategy.Companion.PlayerDirection.North -> {
                val positionsToMove = listOf(
                    Position(1, 0),
                    Position(0, 1),
                    Position(0, -1)
                )
                val randomPos = Strategy.randomMove(npcPos, positionsToMove, maze)
                if (randomPos != Position(0, 0)) {
                    mutableSetOf(
                        ChangeMazePositionEvent(npcPos, null),
                        ChangeMazePositionEvent(npcPos + randomPos, Enemy())
                    )
                } else {
                    HashSet()
                }
            }
            Strategy.Companion.PlayerDirection.South -> {
                val positionsToMove = listOf(
                    Position(-1, 0),
                    Position(0, 1),
                    Position(0, -1)
                )
                val randomPos = Strategy.randomMove(npcPos,positionsToMove, maze)
                if (randomPos != Position(0, 0)) {
                    mutableSetOf(
                        ChangeMazePositionEvent(npcPos, null),
                        ChangeMazePositionEvent(npcPos + randomPos, Enemy())
                    )
                } else {
                    HashSet()
                }
            }
            Strategy.Companion.PlayerDirection.West -> {
                val positionsToMove = listOf(
                    Position(1, 0),
                    Position(0, -1),
                    Position(-1, 0)
                )
                val randomPos = Strategy.randomMove(npcPos,positionsToMove, maze)
                if (randomPos != Position(0, 0)) {
                    mutableSetOf(
                        ChangeMazePositionEvent(npcPos, null),
                        ChangeMazePositionEvent(npcPos + randomPos, Enemy())
                    )
                } else {
                    HashSet()
                }
            }
            Strategy.Companion.PlayerDirection.East -> {
                val positionsToMove = listOf(
                    Position(1, 0),
                    Position(0, 1),
                    Position(1, 0)
                )
                val randomPos = Strategy.randomMove(npcPos,positionsToMove, maze)
                if (randomPos != Position(0, 0)) {
                    mutableSetOf(
                        ChangeMazePositionEvent(npcPos, null),
                        ChangeMazePositionEvent(npcPos + randomPos, Enemy())
                    )
                } else {
                    HashSet()
                }
            }
            Strategy.Companion.PlayerDirection.Failed -> HashSet()
        }
    }
}
