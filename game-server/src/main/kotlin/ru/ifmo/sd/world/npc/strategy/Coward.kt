package ru.ifmo.sd.world.npc.strategy

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.MazeEvent
import ru.ifmo.sd.world.representation.units.Enemy


class Coward : Strategy {
    override fun execute(npcPos: Position, playerPos: Position): MutableSet<MazeEvent> {
        return when (Strategy.findPlayer(npcPos, playerPos)) {
            Strategy.Companion.PlayerDirection.North -> {
                val positionsToMove = listOf(
                    Position(1, 0),
                    Position(0, 1),
                    Position(0, -1)
                )
                val randomPos = Strategy.randomMove(positionsToMove)
                if (randomPos != Position(0, 0)) {
                    mutableSetOf(
                        MazeEvent(npcPos, null),
                        MazeEvent(npcPos + randomPos, Enemy())
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
                val randomPos = Strategy.randomMove(positionsToMove)
                if (randomPos != Position(0, 0)) {
                    mutableSetOf(
                        MazeEvent(npcPos, null),
                        MazeEvent(npcPos + randomPos, Enemy())
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
                val randomPos = Strategy.randomMove(positionsToMove)
                if (randomPos != Position(0, 0)) {
                    mutableSetOf(
                        MazeEvent(npcPos, null),
                        MazeEvent(npcPos + randomPos, Enemy())
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
                val randomPos = Strategy.randomMove(positionsToMove)
                if (randomPos != Position(0, 0)) {
                    mutableSetOf(
                        MazeEvent(npcPos, null),
                        MazeEvent(npcPos + randomPos, Enemy())
                    )
                } else {
                    HashSet()
                }
            }
            Strategy.Companion.PlayerDirection.Failed -> HashSet()
        }
    }
}