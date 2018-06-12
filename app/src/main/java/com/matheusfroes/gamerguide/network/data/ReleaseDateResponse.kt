package com.matheusfroes.gamerguide.network.data

class ReleaseDateResponse(val platform: Long, val date: Long) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReleaseDate

        if (platform != other.platform) return false

        return true
    }

    override fun hashCode(): Int = platform.hashCode()
}