package com.matheusfroes.gamerguide.data.model

enum class Region {
    EUROPE,
    NORTH_AMERICA,
    AUSTRALIA,
    NEW_ZEALAND,
    JAPAN,
    CHINA,
    ASIA,
    WORLD;

    companion object {
        fun fromValue(region: Int?): Region {
            return when (region) {
                1 -> EUROPE
                2 -> NORTH_AMERICA
                3 -> AUSTRALIA
                4 -> NEW_ZEALAND
                5 -> JAPAN
                6 -> CHINA
                7 -> ASIA
                8 -> WORLD
                else -> WORLD
            }
        }
    }

    override fun toString(): String {
        return when (this) {
            Region.EUROPE -> "Europa"
            Region.NORTH_AMERICA -> "América do Norte"
            Region.AUSTRALIA -> "Austrália"
            Region.NEW_ZEALAND -> "Nova Zelândia"
            Region.JAPAN -> "Japão"
            Region.CHINA -> "China"
            Region.ASIA -> "Ásia"
            Region.WORLD -> "Mundial"
        }
    }
}