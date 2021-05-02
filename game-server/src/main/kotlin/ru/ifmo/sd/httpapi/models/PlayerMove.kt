package ru.ifmo.sd.httpapi.models

import kotlinx.serialization.Serializable

@Serializable
data class PlayerMove(val oldPosition: Position, val newPosition: Position)
