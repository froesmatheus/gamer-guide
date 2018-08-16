package com.matheusfroes.gamerguide.ui.mygames

import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.model.GameList
import com.matheusfroes.gamerguide.data.model.InsertType
import com.matheusfroes.gamerguide.data.source.local.GameListLocalSource
import com.matheusfroes.gamerguide.data.source.local.GameLocalSource
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MyGamesViewModel @Inject constructor(
        private val gameListLocalSource: GameListLocalSource,
        private val gameLocalSource: GameLocalSource) : ViewModel() {

    fun removeGameFromLists(gameId: Long) {
        gameListLocalSource.deleteGameFromLists(gameId)
    }

    fun listContainsGame(gameId: Long, listId: Long): Boolean {
        return gameListLocalSource.listContainsGame(gameId, listId)
    }

    fun getLists(): List<GameList> {
        return gameListLocalSource.getLists().blockingFirst()
    }

    fun removeGameFromList(gameId: Long, listId: Long) {
        gameListLocalSource.deleteGameFromList(gameId, listId)
    }

    fun addGameToList(gameId: Long, listId: Long) {
        gameListLocalSource.addGameToList(gameId, listId)
    }

    fun getUnfinishedGames(): Flowable<List<Game>> {
        return gameLocalSource.getUnfinishedGames()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getBeatenGames(): Flowable<List<Game>> {
        return gameLocalSource.getBeatenGames()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateGameProgress(game: Game) {
        gameLocalSource
                .updateGame(game)
    }

    fun removeGame(gameId: Long) {
        gameLocalSource.deleteGame(gameId)
    }

    fun getGameByInsertType(gameId: Long, insertType: InsertType = InsertType.INSERT_BY_SEARCH): Game? {
        return gameLocalSource.getGamesByInsertType(gameId, insertType)
    }

    fun gameIsInGameLists(gameId: Long): Boolean {
        return gameListLocalSource.gameIsInGameLists(gameId)
    }

    fun updateGame(game: Game) {
        gameLocalSource.updateGame(game)
    }
}