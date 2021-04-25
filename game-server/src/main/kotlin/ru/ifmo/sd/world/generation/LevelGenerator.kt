package ru.ifmo.sd.world.generation

import kotlin.random.Random

class LevelGenerator {
    companion object {
        private lateinit var level: Array<IntArray>

        fun generateLevel(length: Int, width: Int): Array<IntArray> {
            val actualLength = if (length < 4) 9 else 2 * length + 1
            val actualWidth = if (width < 4) 9 else 2 * width + 1
            level = Array(actualLength) { IntArray(actualWidth) { 1 } }

            val enterPosRow = 0
            val enterPosColumn = 1
            level[enterPosRow][enterPosColumn] = 0

            val exitPosRow = actualLength - 1
            val exitPosColumn = actualWidth - 2
            level[exitPosRow][exitPosColumn] = 0

            val playerPosRow = 1
            val playerPosColumn = 1
            level[playerPosRow][playerPosColumn] = 0

            generateRec(playerPosRow, playerPosColumn)

            return level
        }

        private fun generateRec(curRow: Int, curColumn: Int) {
            var i: Int
            val dirs: Array<IntArray> = Array(3) { IntArray(2) { 0 } }
            while (true) {
                i = 0
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
                if (i == 0) break
                i = (i * Random.nextDouble()).toInt()
                level[(dirs[i][0] + curRow) / 2][(dirs[i][1] + curColumn) / 2] = 0
                level[dirs[i][0]][dirs[i][1]] = 0
                generateRec(dirs[i][0], dirs[i][1])
            }
        }
    }
}
