package ru.ifmo.sd.world.events

import ru.ifmo.sd.httpapi.models.*
import ru.ifmo.sd.world.generation.LevelGenerator
import ru.ifmo.sd.world.representation.GameLevel
import ru.ifmo.sd.world.representation.units.*


/**
 * Класс, отвечающий за обработку игровых событий, приходящих с клиента.
 */
object EventsHandler {
    val interactionExecutor = InteractionExecutorImpl()
    var gameLevel: GameLevel? = null

    /**
     * Присоединяет нового игрока к текущей игровой сессии, если таковая есть, или создает новую.
     *
     * @param levelConfiguration -- конфигурация игрового уровня
     * @return начальную информацию об игровом уровне
     */
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

    /**
     * Выполняет игровое действие игрока на заданной позиции
     * по отношению к игровому объекту на заданной позиции.
     *
     * @param playerPos -- позиция игрока
     * @param targetPos -- позиция игрового объекта
     * @return данные об изменениях после игрового хода
     */
    fun move(playerPos: Position, targetPos: Position): GameMove {
        val maze = gameLevel!!.maze
        return if (maze[targetPos] == null) {
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
