package ru.ifmo.sd.world.npc

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.ChangeMazePositionEvent
import ru.ifmo.sd.world.npc.strategy.Strategy

class Npc(var position: Position, private val strategy: Strategy) {
    fun move(playerPos: Position): MutableSet<ChangeMazePositionEvent> {
        return strategy.execute(position, playerPos)
    }
}

class NpcEventProvider {
    private val npc: MutableSet<Npc> = HashSet()

    fun move(playerPos: Position): MutableSet<ChangeMazePositionEvent> {
        return npc.random().move(playerPos)
    }

    fun addNpc(pos: Position, strategy: Strategy) {
        npc.add(Npc(pos, strategy))
    }

    fun eliminateNpc(pos: Position) {
        npc.removeIf { it.position == pos }
    }
}
