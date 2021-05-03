package ru.ifmo.sd.world.npc.strategy

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.ChangeMazePositionEvent
import ru.ifmo.sd.world.events.EventsHandler
import ru.ifmo.sd.world.npc.strategy.Strategy.Companion.PlayerDirection.Failed

/**
 * Интерфейс стратегии поведения NPC.
 */
interface Strategy {
    /**
     * Выполняет действие NPC на заданной позиции по отношению к игроку на заданной позиции.
     *
     * @param npcPos -- позиция NPC
     * @param playerPos -- позиция игрока
     * @return множество событий изменения лабиринта
     */
    fun execute(npcPos: Position, playerPos: Position): MutableSet<ChangeMazePositionEvent>

    companion object {
        enum class PlayerDirection {
            North, South, West, East, Failed;
        }

        /**
         * Проверяет нахождение игрового персонажа на заданной позиции
         * в области видимости NPC на заданной позиции. Возвращает направление,
         * в котором находится игровой персонаж относительно заданного NPC.
         * Bresenham's algorithm, see en.wikipedia.org/wiki/File:Bresenham.svg
         *
         * @param npcPos -- позиция NPC
         * @param playerPos -- позиция игрового персонажа
         * @return направление или неудачу, если персонаж невидим для NPC
         */
        fun findPlayer(npcPos: Position, playerPos: Position): PlayerDirection {
            // TODO: implement Bresenham's algorithm
            return Failed
        }

        /**
         * Выбирает случайное перемещении NPC на заданной позиции.
         *
         * @param npcPos -- позиция NPC
         * @param positionsToChoose -- список позиций для выбора
         * @return выбранное перемещение NPC или нулевое перемещение, если доступных перемещений нет
         */
        fun randomMove(npcPos: Position, positionsToChoose: List<Position>): Position {
            val mazeLength = EventsHandler.gameLevel!!.maze.levelMaze.size
            val mazeWidth = EventsHandler.gameLevel!!.maze.levelMaze[0].size
            val availablePos = positionsToChoose.filter {
                val newNpcPos = npcPos + it
                (newNpcPos.row in 1..mazeLength - 2
                    && newNpcPos.column in 1..mazeWidth - 2
                    && EventsHandler.gameLevel!!.maze[newNpcPos] == null)

            }
            return if (availablePos.isNotEmpty()) availablePos.random() else Position(0, 0)
        }
    }
}
