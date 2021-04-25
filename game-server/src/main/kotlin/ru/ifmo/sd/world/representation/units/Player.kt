package ru.ifmo.sd.world.representation.units

class Player(private val identifier: Int) : GameUnit() {
    override val id: Int
        get() = this.identifier
}
