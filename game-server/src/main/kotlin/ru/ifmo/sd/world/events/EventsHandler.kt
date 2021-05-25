package ru.ifmo.sd.world.events

import ru.ifmo.sd.httpapi.models.*
import ru.ifmo.sd.world.errors.GameServerException
import ru.ifmo.sd.world.generation.LevelGenerator
import ru.ifmo.sd.world.representation.*
import ru.ifmo.sd.world.representation.units.Player
import java.util.*
import java.util.stream.Collectors


/**
 * Класс, отвечающий за обработку игровых событий, приходящих с клиента.
 */
object EventsHandler {
    val interactionExecutor = InteractionExecutorImpl()
    var gameLevel: GameLevel? = null
    var playersQueue: Queue<String> = LinkedList()

    /**
     * Присоединяет нового игрока к текущей игровой сессии, если таковая есть, или создает новую.
     *
     * @param playerName -- имя нового игрока
     * @param length -- длина игрового уровня
     * @param width -- ширина игрового уровня
     * @return начальную информацию об игровом уровне
     */
    fun join(playerName: String, length: Int?, width: Int?): JoinGameInfo {
        if (playersQueue.contains(playerName)) {
            throw GameServerException("Player with $playerName name already exists.")
        }
        if (gameLevel == null) {
            startGame(length, width)
        }
        val maze = gameLevel!!.maze
        val newPlayerPos = maze.freePos.random()
        gameLevel!!.unitsHealthStorage.addUnit(newPlayerPos)
        maze[newPlayerPos] = Player()
        playersQueue.add(playerName)

        return JoinGameInfo(
            newPlayerPos, MazeData(getMazeData(gameLevel!!.maze)),
            getHealthsData(gameLevel!!.unitsHealthStorage)
        )
    }

    private fun getMazeData(maze: Maze): Array<Array<Int>> {
        val mazeData = Array(maze.levelMaze.size) {
            Array(maze.levelMaze[0].size) { 0 }
        }
        for (i in mazeData.indices) {
            for (j in mazeData[0].indices) {
                val mazeObj = maze[Position(i, j)]
                mazeData[i][j] = mazeObj?.getTypeIdentifier() ?: 0
            }
        }
        return mazeData
    }

    private fun getHealthsData(unitsHealthStorage: UnitsHealthStorage): List<Pair<Position, Int>> {
        return unitsHealthStorage
            .getHealthsDictionary()
            .entries
            .stream()
            .map { e -> Pair(e.key, e.value) }
            .collect(Collectors.toList())
    }

    /**
     * Отсоединяет указанного игрока от игры.
     * @param playerName -- имя игрока для отсоединения
     * @param playerPos -- позиция игрока для отсоединения
     */
    fun disconnect(playerName: String, playerPos: Position) {
        playersQueue.remove(playerName)
        if (playersQueue.isEmpty()) {
            closeGame()
        }
        if (gameLevel == null) {
            throw GameServerException("Game already finished.")
        }
        val healths = gameLevel!!.unitsHealthStorage
        healths.eliminateUnit(playerPos)
        val maze = gameLevel!!.maze
        maze[playerPos] = null
    }

    /**
     * Возвращает актуальное состояние игры.
     *
     * @return актуальное состояние игры
     */
    fun getActualGameState(playerName: String): GameState {
        if (gameLevel == null) {
            throw GameServerException("Game level has not been initialized yet.")
        }
        if (playersQueue.isEmpty()) {
            throw GameServerException("No player connected on level.")
        }
        return GameState(
            playersQueue.peek(), playersQueue.contains(playerName),
            MazeData(getMazeData(gameLevel!!.maze)),
            getHealthsData(gameLevel!!.unitsHealthStorage)
        )
    }

    private fun startGame(length: Int?, width: Int?) {
        gameLevel = if (length == null && width == null) {
            LevelGenerator.generateLevel()
        } else if (length == null) {
            LevelGenerator.generateLevel(width!!)
        } else if (width == null) {
            LevelGenerator.generateLevel(length)
        } else {
            LevelGenerator.generateLevel(
                length,
                width
            )
        }
    }

    private fun closeGame() {
        gameLevel = null
        playersQueue.clear()
    }

    /**
     * Выполняет игровое действие игрока на заданной позиции
     * по отношению к игровому объекту на заданной позиции.
     *
     * @param playerName -- имя игрока
     * @param playerPos -- позиция игрока
     * @param targetPos -- позиция игрового объекта
     * @return данные об изменениях после игрового хода
     */
    fun move(
        playerName: String, playerPos: Position,
        targetPos: Position, gameLevel: GameLevel
    ): GameMove {
        if (!playerName.contentEquals(playersQueue.peek())) {
            throw GameServerException("Player with name $playerName cannot move because it is not his time to move.")
        }
        if (playersQueue.isEmpty()) {
            throw GameServerException("No player connected on level.")
        }
        playersQueue.add(playersQueue.poll())
        return if (gameLevel.maze[targetPos] == null) {
            moveToFreePos(playerName, playerPos, targetPos, gameLevel)
        } else {
            moveToOccupiedPos(playerName, playerPos, targetPos, gameLevel)
        }
    }

    private fun moveToFreePos(
        playerName: String, playerPos: Position,
        targetPos: Position, gameLevel: GameLevel
    ): GameMove {
        val maze = gameLevel.maze
        val healths = gameLevel.unitsHealthStorage
        // ход игрока
        maze[playerPos] = null
        val playerHealth = healths.eliminateUnit(playerPos)!!
        maze[targetPos] = Player()
        healths.addUnit(targetPos, playerHealth)

        // результат хода игрового мира
        val npcMove = gameLevel.npcEventProvider.move(targetPos, maze)
        npcMove.forEach { maze[it.position] = it.newMazeObj }
        val newPlayerPos =
            if (maze[targetPos] != null) {
                targetPos
            } else {
                healths.eliminateUnit(targetPos)
                playersQueue.remove(playerName)
                Position(-1, -1)
            }
        return GameMove(newPlayerPos, npcMove.map {
            MazeEventData(
                it.position,
                it.newMazeObj?.getTypeIdentifier() ?: 0
            )
        })
    }

    private fun moveToOccupiedPos(
        playerName: String, playerPos: Position,
        targetPos: Position, gameLevel: GameLevel
    ): GameMove {
        val maze = gameLevel.maze
        // результат хода игрока
        val playerMove = maze[targetPos]!!.interact(interactionExecutor, targetPos)
        playerMove.forEach { maze[it.position] = it.newMazeObj }

        // результат хода игрового мира
        val npcMove = gameLevel.npcEventProvider.move(playerPos, maze)
        npcMove.forEach { maze[it.position] = it.newMazeObj }
        val newPlayerPos =
            if (maze[playerPos] != null) playerPos
            else {
                playersQueue.remove(playerName)
                Position(-1, -1)
            }

        playerMove.forEach { npcMove.add(it) }

        return GameMove(newPlayerPos, npcMove.map {
            MazeEventData(
                it.position,
                it.newMazeObj?.getTypeIdentifier() ?: 0
            )
        })
    }
}
