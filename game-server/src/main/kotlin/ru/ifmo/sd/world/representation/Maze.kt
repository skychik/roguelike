package ru.ifmo.sd.world.representation

import ru.ifmo.sd.httpapi.models.Position
import ru.ifmo.sd.world.representation.units.MazeObject

data class Maze(
    val levelMaze: Array<Array<MazeObject?>>,
    val freePos: MutableSet<Position>
) {
    operator fun get(pos: Position): MazeObject? = levelMaze[pos.row][pos.column]
    operator fun set(position: Position, mazeObject: MazeObject?) {
        if (levelMaze[position.row][position.column] == null) {
            freePos.remove(position)
        }
        levelMaze[position.row][position.column] = mazeObject
    }

    fun size() = levelMaze.size * levelMaze[0].size
}
