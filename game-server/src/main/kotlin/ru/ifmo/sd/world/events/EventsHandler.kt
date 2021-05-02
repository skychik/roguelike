package ru.ifmo.sd.world.events

import ru.ifmo.sd.httpapi.models.*
import ru.ifmo.sd.world.generation.LevelGenerator
import ru.ifmo.sd.world.representation.GameLevel
import ru.ifmo.sd.world.representation.units.*

object EventsHandler {
    val interactionExecutor = InteractionExecutorImpl()
    var gameLevel: GameLevel? = null

    fun join(levelConfiguration: LevelConfiguration): JoinGameInfo {
        if (gameLevel == null) {
            init(levelConfiguration.length, levelConfiguration.width)
        }
        val playerPos = gameLevel!!.maze.freePos.random()
        gameLevel!!.unitsHealthStorage.addUnit(playerPos)
        gameLevel!!.maze[playerPos] = Player()

        val length = gameLevel!!.maze.levelMaze.size
        val width = gameLevel!!.maze.levelMaze[0].size
        val mazeData: Array<Array<Int>> = Array(length) { Array(width) { 0 } }
        for (i in 0 until length) {
            for (j in 0 until width) {
                val mazeObj: MazeObject? = gameLevel!!.maze[Position(i, j)]
                mazeData[i][j] = mazeObj?.getTypeIdentifier() ?: 0
            }
        }

        return JoinGameInfo(playerPos, MazeData(mazeData))
    }

    private fun init(length: Int, width: Int) {
        gameLevel = LevelGenerator.generateLevel(length, width)
    }

    fun move(playerPos: Position, targetPos: Position): GameMove {
        val maze = gameLevel!!.maze
        return if (maze[targetPos] != null) {
            // ход игрока
            maze[playerPos] = null
            maze[targetPos] = Player()

            // результат хода игрового мира
            val npcMove = gameLevel!!.npcEventProvider.move(targetPos)
            npcMove.forEach { maze[it.position] = it.newMazeObj }
            val newPlayerPos =
                if (maze[targetPos] != null) targetPos
                else Position(-1, -1)

            GameMove(newPlayerPos, npcMove.map {
                MazeEventData(
                    it.position,
                    it.newMazeObj?.getTypeIdentifier() ?: 0
                )
            })
        } else {
            // результат хода игрока
            val playerMove = maze[targetPos]!!.interact(interactionExecutor, targetPos)
            playerMove.forEach { maze[it.position] = it.newMazeObj }

            // результат хода игрового мира
            val npcMove = gameLevel!!.npcEventProvider.move(playerPos)
            npcMove.forEach { maze[it.position] = it.newMazeObj }
            val newPlayerPos =
                if (maze[playerPos] != null) playerPos
                else Position(-1, -1)

            playerMove.forEach { npcMove.add(it) }

            GameMove(newPlayerPos, npcMove.map {
                MazeEventData(
                    it.position,
                    it.newMazeObj?.getTypeIdentifier() ?: 0
                )
            })
        }
    }
}
