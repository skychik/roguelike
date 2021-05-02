package ru.ifmo.sd.world.events

import ru.ifmo.sd.httpapi.models.LevelConfiguration
import ru.ifmo.sd.world.configuration.GameConfiguration
import ru.ifmo.sd.world.configuration.GameConfigurator
import ru.ifmo.sd.world.representation.Position
import ru.ifmo.sd.world.representation.units.GameUnit

class EventsHandler {
    companion object {
        fun startGame(levelConf: LevelConfiguration): GameConfiguration {
            return GameConfigurator.configure(levelConf.levelLength, levelConf.levelWidth)
        }

        fun move(targetUnit: GameUnit, newPos: Position) {
            GameConfigurator.getUnitsPositions().move(targetUnit, newPos)
        }
    }
}
