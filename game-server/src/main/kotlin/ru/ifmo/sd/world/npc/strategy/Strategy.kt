package ru.ifmo.sd.world.npc.strategy

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.events.ChangeMazePositionEvent
import ru.ifmo.sd.world.npc.Npc
import ru.ifmo.sd.world.npc.strategy.Strategy.Companion.PlayerDirection.*
import ru.ifmo.sd.world.representation.Maze
import ru.ifmo.sd.world.representation.units.EnemyFactory
import kotlin.math.abs
import kotlin.math.atan2

/**
 * Интерфейс стратегии поведения NPC.
 */
interface Strategy {
    /**
     * Возвращает фабрику объектов лабиринта, соответствующих противникам с данной стратегией поведения.
     *
     * @return фабрика противников
     */
    fun getEnemyFactory(): EnemyFactory

    /**
     * Выполняет действие NPC на заданной позиции по отношению к игроку на заданной позиции.
     *
     * @param npc-- NPC, выполняющий действие
     * @param playerPos -- позиция игрока
     * @param maze -- игровой лабиринт
     * @return множество событий изменения лабиринта
     */
    fun execute(npc: Npc, playerPos: Position, maze: Maze): MutableSet<ChangeMazePositionEvent>

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
                    if ((x != npcPos.row || y != npcPos.column)
                        && (x != playerPos.row || y != playerPos.column)
                        && !checkVisibility(Position(x, y), maze)) {
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
                    if ((x != npcPos.row || y != npcPos.column)
                        && (x != playerPos.row || y != playerPos.column)
                        && !checkVisibility(Position(x, y), maze)) {
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

            return getDirection(npcPos, playerPos)
        }

        private fun getDirection(npcPos: Position, playerPos: Position): PlayerDirection {
            var rad = atan2((playerPos.row - npcPos.row).toDouble(), (playerPos.column - npcPos.column).toDouble());
            if (rad < 0) rad += (2 * Math.PI)
            return when (rad * (180 / Math.PI)) {
                in 45.0..135.0 -> {
                    South
                }
                in 135.0..225.0 -> {
                    East
                }
                in 225.0..315.0 -> {
                    North
                }
                else -> {
                    West
                }
            }
        }

        private fun checkVisibility(pos: Position, maze: Maze): Boolean {
            return maze[pos] == null
        }

        /**
         * Выбирает случайное перемещении NPC на заданной позиции.
         *
         * @param npcPos -- позиция NPC
         * @param directions -- список позиций для выбора
         * @param maze -- игровой лабиринт
         * @return выбранное перемещение NPC или нулевое перемещение, если доступных перемещений нет
         */
        fun randomDirection(npcPos: Position, directions: List<Position>, maze: Maze): Position {
            val mazeLength = maze.levelMaze.size
            val mazeWidth = maze.levelMaze[0].size
            val availablePos = directions.filter {
                val newNpcPos = npcPos + it
                (newNpcPos.row in 1..mazeLength - 2
                    && newNpcPos.column in 1..mazeWidth - 2
                    && maze[newNpcPos] == null)
            }
            return if (availablePos.isNotEmpty()) availablePos.random() else Position(0, 0)
        }
    }
}
