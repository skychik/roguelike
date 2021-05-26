package ru.ifmo.sd.world.npc

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.ChangeMazePositionEvent
import ru.ifmo.sd.world.npc.strategy.Strategy
import ru.ifmo.sd.world.representation.Maze

/**
 * Класс NPC.
 */
class Npc(var position: Position, private val strategy: Strategy) {
    /**
     * Выполняет игровой ход текущего NPC по отношению к игровому персонажу на заданной позиции.
     *
     * @param playerPos -- позиция игрового персонажа
     * @param maze -- игровой лабиринт
     * @return множество событий игрового лабиринта
     */
    fun move(playerPos: Position, maze: Maze): MutableSet<ChangeMazePositionEvent> {
        return strategy.execute(this, playerPos, maze)
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
     * @param maze -- игровой лабиринт
     * @return множество событий игрового лабиринта
     */
    fun move(playerPos: Position, maze: Maze): MutableSet<ChangeMazePositionEvent> {
        return if (npc.isNotEmpty()) npc.random().move(playerPos, maze) else HashSet()
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

    /**
     * Возвращает флаг выживания npc.
     *
     * @return true -- если хотя бы один npc жив, иначе -- false
     */
    fun isAtLeastOneNpcAlive(): Boolean {
        return npc.isNotEmpty()
    }
}
