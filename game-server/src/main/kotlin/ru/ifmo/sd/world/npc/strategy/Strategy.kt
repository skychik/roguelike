package ru.ifmo.sd.world.npc.strategy

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.ChangeMazePositionEvent
import ru.ifmo.sd.world.npc.strategy.Strategy.Companion.PlayerDirection.Failed
import ru.ifmo.sd.world.representation.Maze
import kotlin.math.abs

/**
 * Интерфейс стратегии поведения NPC.
 */
interface Strategy {
    /**
     * Выполняет действие NPC на заданной позиции по отношению к игроку на заданной позиции.
     *
     * @param npcPos -- позиция NPC
     * @param playerPos -- позиция игрока
     * @param maze -- игровой лабиринт
     * @return множество событий изменения лабиринта
     */
    fun execute(npcPos: Position, playerPos: Position, maze: Maze): MutableSet<ChangeMazePositionEvent>

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
         * @param maze -- игровой лабиринт
         * @return направление или неудачу, если персонаж невидим для NPC
         */
        fun findPlayer(npcPos: Position, playerPos: Position, maze: Maze): PlayerDirection {
            var d = 0

            val dx: Int = abs(playerPos.row - npcPos.row)
            val dy: Int = abs(playerPos.column - npcPos.column)

            val dx2 = 2 * dx
            val dy2 = 2 * dy

            val ix = if (npcPos.row < playerPos.row) 1 else -1
            val iy = if (npcPos.column < playerPos.column) 1 else -1

            var x: Int = npcPos.row
            var y: Int = npcPos.column

            if (dx >= dy) {
                while (true) {
                    if (!checkVisibility(Position(x, y), maze)) {
                        return Failed
                    }
                    if (x == playerPos.row) break
                    x += ix
                    d += dy2
                    if (d > dx) {
                        y += iy
                        d -= dx2
                    }
                }
            } else {
                while (true) {
                    if (!checkVisibility(Position(x, y), maze)) {
                        return Failed
                    }
                    if (y == playerPos.column) break
                    y += iy
                    d += dx2
                    if (d > dy) {
                        x += ix
                        d -= dy2
                    }
                }
            }

            return Failed
        }

        private fun checkVisibility(pos: Position, maze: Maze): Boolean {
            return maze[pos] == null
        }

        /**
         * Выбирает случайное перемещении NPC на заданной позиции.
         *
         * @param npcPos -- позиция NPC
         * @param positionsToChoose -- список позиций для выбора
         * @param maze -- игровой лабиринт
         * @return выбранное перемещение NPC или нулевое перемещение, если доступных перемещений нет
         */
        fun randomMove(npcPos: Position, positionsToChoose: List<Position>, maze: Maze): Position {
            val mazeLength = maze.levelMaze.size
            val mazeWidth = maze.levelMaze[0].size
            val availablePos = positionsToChoose.filter {
                val newNpcPos = npcPos + it
                (newNpcPos.row in 1..mazeLength - 2
                    && newNpcPos.column in 1..mazeWidth - 2
                    && maze[newNpcPos] == null)

            }
            return if (availablePos.isNotEmpty()) availablePos.random() else Position(0, 0)
        }
    }
}
