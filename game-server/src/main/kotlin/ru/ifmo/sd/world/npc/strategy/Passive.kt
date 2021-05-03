package ru.ifmo.sd.world.npc.strategy

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.ChangeMazePositionEvent
import ru.ifmo.sd.world.representation.units.Enemy

/**
 * Класс, отвечающий пассивной стратегии поведения NPC.
 */
class Passive : Strategy {
    override fun execute(npcPos: Position, playerPos: Position): MutableSet<ChangeMazePositionEvent> {
        val positionsToMove = listOf(
            Position(0, 1), Position(0, -1),
            Position(1, 0), Position(-1, 0)
        )
        val randomMove = Strategy.randomMove(npcPos, positionsToMove)
        return if (randomMove != Position(0, 0))
            mutableSetOf(
                ChangeMazePositionEvent(npcPos, null),
                ChangeMazePositionEvent(npcPos + randomMove, Enemy())
            )
        else HashSet()
    }
}
