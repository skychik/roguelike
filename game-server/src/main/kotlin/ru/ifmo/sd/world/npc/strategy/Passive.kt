package ru.ifmo.sd.world.npc.strategy

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.ChangeMazePositionEvent
import ru.ifmo.sd.world.npc.Npc
import ru.ifmo.sd.world.representation.Maze
import ru.ifmo.sd.world.representation.units.EnemyFactory
import ru.ifmo.sd.world.representation.units.PassiveEnemyFactory

/**
 * Класс, отвечающий пассивной стратегии поведения NPC.
 */
class Passive : Strategy {
    companion object {
        private val directionsToMove = listOf(
            Position(0, 1), Position(0, -1),
            Position(1, 0), Position(-1, 0)
        )
    }

    override fun getEnemyFactory(): EnemyFactory {
        return PassiveEnemyFactory()
    }

    override fun execute(npc: Npc, playerPos: Position, maze: Maze): MutableSet<ChangeMazePositionEvent> {
        val randomMove = Strategy.randomDirection(npc.position, directionsToMove, maze)
        return if (randomMove != Position(0, 0)) {
            val oldNpcPos = npc.position
            npc.position = npc.position + randomMove
            mutableSetOf(
                ChangeMazePositionEvent(oldNpcPos, null),
                ChangeMazePositionEvent(npc.position, getEnemyFactory().getEnemy())
            )
        } else HashSet()
    }
}
