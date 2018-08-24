package com.matheusfroes.gamerguide.ui.addgames

import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.extra.Result
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.model.GameList
import com.matheusfroes.gamerguide.data.model.InsertType
import com.matheusfroes.gamerguide.data.source.local.GameListLocalSource
import com.matheusfroes.gamerguide.data.source.local.GameLocalSource
import com.matheusfroes.gamerguide.data.source.remote.GameRemoteSource
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddGamesViewModel @Inject constructor(
        private val gameRemoteSource: GameRemoteSource,
        private val gameListLocalSource: GameListLocalSource,
        private val gameLocalSource: GameLocalSource) : ViewModel() {
    var nextPageId = ""
    var queryDigitada = ""

    fun searchGames(): Observable<Result<List<Game>>> {
        return gameRemoteSource.searchGames(queryDigitada, nextPageId)
                .map {
                    nextPageId = it.second
                    it.first
                }
                .map<Result<List<Game>>> { Result.Complete(it) }
                .startWith(Result.InProgress())
                .onErrorReturn { Result.Error(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLists(): List<GameList> {
        return gameListLocalSource.getLists().blockingFirst()
    }

    fun listContainsGame(gameId: Long, listId: Long): Boolean {
        return gameListLocalSource.listContainsGame(gameId, listId)
    }

    fun removeGameFromList(gameId: Long, listId: Long) {
        gameListLocalSource.deleteGameFromList(gameId, listId)
    }

    fun addGameToList(gameId: Long, listId: Long) {
        gameListLocalSource.addGameToList(gameId, listId)
    }

    fun addGame(game: Game) {
        gameLocalSource.addGame(game)
    }

    fun isGameAdded(gameId: Long): Boolean {
        return gameLocalSource.isGameAdded(gameId)
    }

    fun getGameByInsertType(gameId: Long, insertType: InsertType = InsertType.INSERT_BY_SEARCH): Game? {
        return gameLocalSource.getGamesByInsertType(gameId, insertType)
    }
}
