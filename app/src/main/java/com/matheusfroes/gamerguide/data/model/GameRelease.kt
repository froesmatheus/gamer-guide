package com.matheusfroes.gamerguide.data.model

import java.util.*

data class GameRelease(
        val id: Long,
        val name: String,
        val cover: String
)

data class Release(
        val game: GameRelease,
        val platform: Platform,
        val date: Date,
        val region: Region
) {

    override fun equals(other: Any?): Boolean {
        return if (other is Release) {
            this.game.name == other.game.name
        } else {
            false
        }

    }

    override fun hashCode(): Int {
        var result = game.hashCode()
        result = 31 * result + platform.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + region.hashCode()
        return result
    }
}