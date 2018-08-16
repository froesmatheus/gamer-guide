package com.matheusfroes.gamerguide.data.source.remote

import com.matheusfroes.gamerguide.data.mapper.GameMapper
import com.matheusfroes.gamerguide.data.mapper.ReleaseMapper
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.model.Release
import com.matheusfroes.gamerguide.network.GameService
import com.matheusfroes.gamerguide.network.data.ObterStreamsResponse
import com.matheusfroes.gamerguide.network.data.Stream
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class GameRemoteSource @Inject constructor(
        private val gameService: GameService,
        private val gameMapper: GameMapper,
        private val releaseMapper: ReleaseMapper) {

    fun searchGames(query: String = "", nextPage: String = ""): Observable<Pair<List<Game>, String>> {
        val call = if (nextPage.isEmpty()) {
            gameService.searchGames(query)
        } else {
            gameService.searchGamesNextPage(nextPage)
        }

        return call.map { response ->
            val nextPageId = response.headers()["X-Next-Page"] ?: ""
            val games = gameMapper.map(response.body().orEmpty())

            Pair(games, nextPageId)
        }
    }

    fun getLivestreamsByGame(gameName: String, offset: Int): Single<List<Stream>> {
        return gameService
                .getLivestreamsByGame(gameName, offset = offset)
                .map(ObterStreamsResponse::streams)
    }

    fun getGameReleases(nextPage: String): Observable<Pair<List<Release>, String>> {
        val request = if (nextPage.isEmpty()) {
            gameService.getGameReleases(Calendar.getInstance().timeInMillis)
        } else {
            gameService.getGamesReleasesNextPage(nextPage)
        }

        return request.map { response ->
            val nextPageId = response.headers()["X-Next-Page"].toString()
            val releases = releaseMapper.map(response.body()!!)

            Pair(releases, nextPageId)
        }
    }
}