package ru.ifmo.sd.world.generation

class LevelGenerator {
    companion object {
        private lateinit var level: Array<IntArray>

        fun generateLevel(length: Int, width: Int): Array<IntArray> {
            return Array(length) { IntArray(width){1} }
        }
    }
}
