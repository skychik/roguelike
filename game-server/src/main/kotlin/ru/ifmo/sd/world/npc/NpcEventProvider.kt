package ru.ifmo.sd.world.npc

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.ChangeMazePositionEvent
import ru.ifmo.sd.world.events.EventsHandler
import ru.ifmo.sd.world.npc.strategy.Strategy

/**
 * Класс NPC.
 */
class Npc(var position: Position, private val strategy: Strategy) {
    /**
     * Выполняет игровой ход текущего NPC по отношению к игровому персонажу на заданной позиции.
     *
     * @param playerPos -- позиция игрового персонажа
     * @return множество событий игрового лабиринта
     */
    fun move(playerPos: Position): MutableSet<ChangeMazePositionEvent> {
        return strategy.execute(position, playerPos, EventsHandler.gameLevel!!.maze)
    }
}

/**
 * Класс, отвечающий за игровые действия NPC.
 */
class NpcEventProvider {
    private val npc: MutableSet<Npc> = HashSet()

    /**
     * Выполняет игровое действие случайного NPC по отношению к игровому персонажу на заданной позиции.
     *
     * @param playerPos -- позиция игрового персонажа
     * @return множество событий игрового лабиринта
     */
    fun move(playerPos: Position): MutableSet<ChangeMazePositionEvent> {
        return if (npc.isNotEmpty()) npc.random().move(playerPos) else HashSet()
    }

    /**
     * Добавляет нового NPC с заданной стратегией на заданную позицию.
     *
     * @param pos -- позиция нового NPC
     * @param strategy -- стратегия поведения нового NPC
     */
    fun addNpc(pos: Position, strategy: Strategy) {
        npc.add(Npc(pos, strategy))
    }

    /**
     * Уничтожает NPC на заданной позиции.
     *
     * @param pos -- позиция NPC
     */
    fun eliminateNpc(pos: Position) {
        npc.removeIf { it.position == pos }
    }
}
