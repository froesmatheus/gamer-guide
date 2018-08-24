package com.matheusfroes.gamerguide.data.mapper

import com.matheusfroes.gamerguide.extra.adicionarSchemaUrl
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.network.data.GameResponse
import java.util.*
import javax.inject.Inject

class GameMapper @Inject constructor(private val platformMapper: PlatformMapper) {

    fun map(gameResponse: GameResponse): Game {
        val game = Game(
                id = gameResponse.id,
                name = gameResponse.name ?: "",
                description = gameResponse.summary ?: "",
                developers = gameResponse.developers?.joinToString() ?: "",
                publishers = gameResponse.publishers?.joinToString() ?: "",
                genres = GenreMapper.map(gameResponse.genres),
                releaseDate = if (gameResponse.firstReleaseDate == 0L) null else Date(gameResponse.firstReleaseDate),
                gameEngine = GameEngineMapper.map(gameResponse.gameEngines),
                timeToBeat = TimeToBeatMapper.map(gameResponse.timeToBeat),
                coverImage = adicionarSchemaUrl(gameResponse.cover?.url))

        val videos = VideoMapper.map(gameResponse.videos, gameResponse.id)
        val platforms = platformMapper.map(gameResponse.releaseDates)

        game.videos = videos
        game.platforms = platforms
        return game
    }

    fun map(gameResponse: List<GameResponse>): List<Game> {
        return gameResponse.map { map(it) }
    }
}