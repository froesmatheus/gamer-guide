package com.matheusfroes.gamerguide.data.mapper

import android.content.Context
import com.matheusfroes.gamerguide.adicionarSchemaUrl
import com.matheusfroes.gamerguide.data.db.PlataformasDAO
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.network.data.GameResponse
import java.util.*

class GameMapper {
    companion object {
        fun map(gameResponse: GameResponse, context: Context): Game {
            val game = Game(
                    id = gameResponse.id,
                    name = gameResponse.name ?: "",
                    description = gameResponse.summary ?: "",
                    developers = gameResponse.developers?.joinToString() ?: "",
                    publishers = gameResponse.publishers?.joinToString() ?: "",
                    genres = GenreMapper.map(gameResponse.genres),
                    releaseDate = Date(gameResponse.firstReleaseDate),
                    gameEngine = GameEngineMapper.map(gameResponse.gameEngines),
                    timeToBeat = TimeToBeatMapper.map(gameResponse.timeToBeat),
                    coverImage = adicionarSchemaUrl(gameResponse.cover?.url))

            val videos = VideoMapper.map(gameResponse.videos)
            val platforms = PlatformMapper.map(gameResponse.releaseDates, PlataformasDAO(context))

            game.videos = videos
            game.platforms = platforms
            return game
        }
    }
}