package ru.ifmo.sd.httpapi.models

import kotlinx.serialization.Serializable

@Serializable
data class JoinInfo(
    val playerName: String,
    val length: Int?,
    val width: Int?
)
