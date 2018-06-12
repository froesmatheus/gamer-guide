package com.matheusfroes.gamerguide.data.model

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "games")
data class Game(
        @PrimaryKey
        val id: Long,
        val name: String,
        val description: String,
        val developers: String,
        val publishers: String,
        val genres: String,
        val releaseDate: Date,
        val gameEngine: String,
        @Embedded(prefix = "time_to_beat")
        val timeToBeat: TimeToBeat? = null,
        val coverImage: String,
        @Embedded(prefix = "game_progress_")
        val progress: GameProgress = GameProgress(0, 0, false),
        var formaCadastro: FormaCadastro = FormaCadastro.CADASTRO_POR_BUSCA) : Serializable {

    @Ignore
    lateinit var platforms: List<Platform>

    @Ignore
    lateinit var videos: List<Video>
}