package com.matheusfroes.gamerguide.ui.gamelists.gamelistdetails

import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.model.GameList
import com.matheusfroes.gamerguide.data.source.local.GameListLocalSource
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GameListDetailsViewModel @Inject constructor(private val gameListLocalSource: GameListLocalSource) : ViewModel() {

    fun getList(gameListId: Long): GameList {
        return gameListLocalSource.getList(gameListId)
    }

    fun getGamesByList(gameListId: Long): Flowable<List<Game>> {
        return gameListLocalSource
                .getGamesByList(gameListId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun removeList(gameListId: Long) {
        gameListLocalSource.delete(gameListId)
    }

    fun update(gameList: GameList) {
        gameListLocalSource.update(gameList)
    }

    fun listAlreadyAdded(gameListName: String): Boolean {
        return gameListLocalSource.listAlreadyAdded(gameListName)
    }
}