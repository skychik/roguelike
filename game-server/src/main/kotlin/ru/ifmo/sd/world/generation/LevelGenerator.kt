package ru.ifmo.sd.world.generation

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.npc.NpcEventProvider
import ru.ifmo.sd.world.npc.strategy.*
import ru.ifmo.sd.world.representation.*
import ru.ifmo.sd.world.representation.units.Enemy
import kotlin.random.Random

/**
 * Класс, отвечающий за генерацию игрового лабиринта и
 * размещения на нем противников.
 */
class LevelGenerator {
    companion object {
        private const val GAME_DIFFICULTY = 0.1
        private lateinit var level: Array<IntArray>
        private lateinit var freePos: MutableList<Position>

        /**
         * Метод для генерации случайного лабиринта.
         *
         * @param length -- длина лабиринта
         * @param width -- ширина лабиринта
         * @return двумерный массив соответствующий игровому лабиринту
         */
        fun generateLevel(length: Int = 10, width: Int = 10): GameLevel {
            /*
            Шаг смещения позиции при создании лабиринта определим равным 2.
            Создаем лабиринт с нечетными размерностями, чтобы поддерживать
            инвариант нахождения в границах игрового поля.
             */
            val actualLength = if (length < 4) 9 else 2 * length + 1
            val actualWidth = if (width < 4) 9 else 2 * width + 1
            // Изначально лабиринт заполненяем стенками
            level = Array(actualLength) { IntArray(actualWidth) { 1 } }

            // Позиция входа в лабиринт
            val enterPosRow = 0
            val enterPosColumn = 1
            level[enterPosRow][enterPosColumn] = 0

            // Позиция выхода из лабиринта
            val exitPosRow = actualLength - 1
            val exitPosColumn = actualWidth - 2
            level[exitPosRow][exitPosColumn] = 0

            // Стартовая позиция для генерации
            val startPosRow = 1
            val startPosColumn = 1
            level[startPosRow][startPosColumn] = 0

            freePos = ArrayList()
            // Рекурсивно генерируем лабиринт
            generateRec(startPosRow, startPosColumn)

            val maze = Maze(Array(actualLength) {
                Array(actualWidth) {
                    null
                }
            }, HashSet())
            val unitsHealths = UnitsHealthStorage()
            val npc = NpcEventProvider()
            placeEnemies(maze, unitsHealths, npc)
            return GameLevel(maze, unitsHealths, npc)
        }

        // Идея в том, чтобы пробуривать соседние занятые клетки и избегать при этом циклов.
        private fun generateRec(curRow: Int, curColumn: Int) {
            // Идентификатор текущего направления движения при генерации лабиринта
            var i: Int
            // Массив доступных направлении и смещения в этих направлениях
            val dirs: Array<IntArray> = Array(3) { IntArray(2) { 0 } }
            // Двигаемся по лабиринту пока можем
            while (true) {
                i = 0
                /*
                Если текущаяя позиция в границах игрового поля, и через клетку не свободно,
                иначе цикл, то пробуриваемся в текущем направлении.
                 */
                if (curRow > 1 && level[curRow - 2][curColumn] != 0) {
                    dirs[i][0] = curRow - 2
                    dirs[i][1] = curColumn
                    i++
                }
                if (curRow < level.size - 2 && level[curRow + 2][curColumn] != 0) {
                    dirs[i][0] = curRow + 2
                    dirs[i][1] = curColumn
                    i++
                }
                if (curColumn > 1 && level[curRow][curColumn - 2] != 0) {
                    dirs[i][0] = curRow
                    dirs[i][1] = curColumn - 2
                    i++
                }
                if (curColumn < level[0].size - 2 && level[curRow][curColumn + 2] != 0) {
                    dirs[i][0] = curRow
                    dirs[i][1] = curColumn + 2
                    i++
                }
                // Если доступных направлений для движения нет, то прекращаем работу
                if (i == 0) break
                // Выбираем случайное направление для следующего движения
                i = (i * Random.nextDouble()).toInt()
                // Пробуриваем две стенки в направлении движения
                val firstPosToRemoveWall = Position(
                    (dirs[i][0] + curRow) / 2,
                    (dirs[i][1] + curColumn) / 2
                )
                val secondPosToRemoveWall = Position(dirs[i][0], dirs[i][1])
                level[firstPosToRemoveWall.row][firstPosToRemoveWall.column] = 0
                level[secondPosToRemoveWall.row][secondPosToRemoveWall.column] = 0

                freePos.add(firstPosToRemoveWall)
                freePos.add(secondPosToRemoveWall)

                // Рекурсивно запускаемся от позиции, в которой оказались после пробуривания стенок
                generateRec(dirs[i][0], dirs[i][1])
            }
        }

        private fun placeEnemies(maze: Maze, healths: UnitsHealthStorage, npc: NpcEventProvider) {
            val countOfEnemies = (maze.size() * GAME_DIFFICULTY).toInt()
            freePos.forEach { maze.freePos.add(it) }
            val enemiesPositions = freePos
                .shuffled()
                .take(countOfEnemies)
            val strategies = listOf(Passive(), Aggressive(), Coward())
            enemiesPositions.forEach {
                maze[it] = Enemy()
                healths.addUnit(it, 100)
                npc.addNpc(it, strategies.random())
            }
        }
    }
}
