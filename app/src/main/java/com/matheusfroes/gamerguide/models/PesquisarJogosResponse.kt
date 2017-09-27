package com.matheusfroes.gamerguide.models

import com.google.gson.annotations.SerializedName


/**
 * Created by matheusfroes on 26/09/2017.
 */
data class GameResponse(
        val id: Long,
        val name: String,
        val summary: String,
        var developers: List<Developer>? = null,
        var publishers: List<Publisher>? = null,
        val genres: List<Genre>? = null,
        @SerializedName("first_release_date")
        val firstReleaseDate: Long,
        @SerializedName("release_dates")
        val releaseDates: List<ReleaseDate>,
        val cover: Cover?)

data class Genre(val id: Int, val name: String) {
    override fun toString() = name
}

data class Developer(val id: Int, val name: String) {
    override fun toString() = name
}

class ReleaseDate(val platform: Long, val date: Long) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReleaseDate

        if (platform != other.platform) return false

        return true
    }

    override fun hashCode(): Int = platform.hashCode()
}

class Publisher(val id: Int, val name: String) {
    override fun toString() = name
}

data class Cover(val url: String?)