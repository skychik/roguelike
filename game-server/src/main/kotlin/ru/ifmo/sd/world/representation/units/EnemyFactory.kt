package ru.ifmo.sd.world.representation.units

/**
 * Интерфейс фабрики противников.
 */
interface EnemyFactory {
    /**
     * Возвращает противника.
     */
    fun getEnemy(): Enemy
}

class AggressiveEnemyFactory : EnemyFactory {
    override fun getEnemy(): Enemy {
        return AggressiveEnemy()
    }
}

class PassiveEnemyFactory : EnemyFactory {
    override fun getEnemy(): Enemy {
        return PassiveEnemy()
    }
}

class CowardEnemyFactory : EnemyFactory {
    override fun getEnemy(): Enemy {
        return CowardEnemy()
    }
}
