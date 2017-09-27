package com.matheusfroes.gamerguide.models

import com.google.gson.annotations.SerializedName


/**
 * Created by matheusfroes on 26/09/2017.
 */
data class PesquisarJogosResponse(
        val jogos: List<Game>
)


data class Game(
        val id: Long,
        val name: String,
        val summary: String,
        val developers: List<Developer>,
        val publishers: List<Publisher>,
        val genres: List<Genre>,
        @SerializedName("first_release_date")
        val firstReleaseDate: Long,
        @SerializedName("release_dates")
        val releaseDates: List<ReleaseDate>,
        val cover: Cover)

data class Genre(val id: Int, val name: String)

data class Developer(val id: Int, val name: String)

class ReleaseDate(val platform: Long, val date: Long)

class Publisher(val id: Int, val name: String)

data class Cover(val url: String)
