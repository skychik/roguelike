package ru.ifmo.sd.world.representation

import ru.ifmo.sd.world.npc.NpcEventProvider

data class GameLevel(
    val maze: Maze,
    val unitsHealthStorage: UnitsHealthStorage,
    val npcEventProvider: NpcEventProvider
)
