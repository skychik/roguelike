package ru.ifmo.sd.world.configuration

import io.ktor.serialization.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ru.ifmo.sd.world.representation.*
import ru.ifmo.sd.world.representation.units.GameUnit

@Serializable
data class GameConfiguration(
    val playerPos: Position,
    val level: Array<IntArray>,
    val unitsPositions: Map<GameUnit, Position>,
    val unitsHealthStorage: Map<GameUnit, Int>
) {
    fun makeSerializable(): GameConfigurationSerializable {
        return GameConfigurationSerializable(
            playerPos, level,
            unitsPositions.mapKeys { (gameUnit, _) -> Json.encodeToString(GameUnit.serializer(), gameUnit) },
            unitsHealthStorage.mapKeys { (gameUnit, _) -> Json.encodeToString(GameUnit.serializer(), gameUnit) }
        )
    }
}

@Serializable
data class GameConfigurationSerializable(
    val playerPos: Position,
    val level: Array<IntArray>,
    val unitsPositions: Map<String, Position>,
    val unitsHealthStorage: Map<String, Int>
) {
    fun deserializeBack(): GameConfiguration {
        return GameConfiguration(
            playerPos, level,
            unitsPositions.mapKeys { (jsonString, _) -> Json.decodeFromString(GameUnit.serializer(), jsonString) },
            unitsHealthStorage.mapKeys { (jsonString, _) -> Json.decodeFromString(GameUnit.serializer(), jsonString) }
        )
    }

}
