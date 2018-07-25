package com.matheusfroes.gamerguide.data.source.remote

import com.matheusfroes.gamerguide.data.mapper.GameMapper
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.network.GameService
import io.reactivex.Observable
import javax.inject.Inject

class GameRemoteSource @Inject constructor(private val gameService: GameService) {

    fun searchGames(query: String = "", nextPage: String = ""): Observable<Pair<List<Game>, String>> {
        val call = if (nextPage.isEmpty()) {
            gameService.searchGames(query)
        } else {
            gameService.searchGamesNextPage(nextPage)
        }

        return call.map { response ->
            val nextPageId = response.headers()["X-Next-Page"] ?: ""
            val games = GameMapper.map(response.body().orEmpty())

            Pair(games, nextPageId)
        }
    }
}