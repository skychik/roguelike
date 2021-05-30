package ru.ifmo.sd.httpapi.models

import kotlinx.serialization.Serializable

@Serializable
data class LevelConfiguration(val length: Int, val width: Int)
