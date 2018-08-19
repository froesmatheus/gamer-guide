package com.matheusfroes.gamerguide.data.source.local

import com.matheusfroes.gamerguide.data.GamerGuideDatabase
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.model.InsertType
import io.reactivex.Flowable
import javax.inject.Inject

class GameLocalSource @Inject constructor(private val database: GamerGuideDatabase) {

    fun getTotalHoursPlayed(): Int {
        return database.gamesDao().getTotalHoursPlayed()
    }

    fun getGameCount(beaten: Boolean): Int {
        return database.gamesDao().getGameCount(beaten)
    }

    fun getMostPlayedGenres(): List<Pair<String, Int>> {
        val genres = database.gamesDao().getGenres()
        return genres
                .map { genre -> genre.split(", ") }
                .flatten()
                .map { genre -> Pair(genre, genres.count { it == genre }) }
                .filter { it.second > 0 }
                .distinctBy { pair -> pair.first }
                .sortedByDescending { pair -> pair.second }
                .take(5)
    }

    fun getGame(gameId: Long): Game? {
        val game = database.gamesDao().get(gameId)

        game?.videos = database.videosDao().getVideosByGame(gameId)
        game?.platforms = database.platformsDao().getPlatformsByGame(gameId)

        return game
    }

    fun addGame(game: Game) {
        database.gamesDao().insert(game)
    }

    fun updateGame(game: Game) {
        database.gamesDao().update(game)
    }

    fun deleteGame(gameId: Long) {
        database.gamesDao().delete(gameId)
    }

    fun getGamesByInsertType(gameId: Long, insertType: InsertType): Game? {
        return database.gamesDao().getGamesByInsertType(gameId, insertType)
    }

    fun getUnfinishedGames(): Flowable<List<Game>> {
        return database.gamesDao().getUnfinishedGames()
    }

    fun getBeatenGames(): Flowable<List<Game>> {
        return database.gamesDao().getBeatenGames()
    }

    fun isGameAdded(gameId: Long): Boolean {
        return database.gamesDao().isGameAdded(gameId)
    }
}