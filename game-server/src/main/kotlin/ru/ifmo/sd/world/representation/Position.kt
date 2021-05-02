package ru.ifmo.sd.world.representation

import kotlinx.serialization.Serializable

@Serializable
data class Position(val row: Int, val column: Int)
