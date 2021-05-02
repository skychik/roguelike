package ru.ifmo.sd.world.npc.strategy

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.MazeEvent
import ru.ifmo.sd.world.representation.units.Enemy


class Passive : Strategy {
    override fun execute(npcPos: Position, playerPos: Position): MutableSet<MazeEvent> {
        val positionsToMove = listOf(
            Position(0, 1), Position(0, -1),
            Position(1, 0), Position(-1, 0)
        )
        val randomMove = Strategy.randomMove(positionsToMove)
        return if (randomMove != Position(0, 0))
            mutableSetOf(
                MazeEvent(npcPos, null),
                MazeEvent(npcPos + randomMove, Enemy())
            )
        else HashSet()
    }
}
