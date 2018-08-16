package com.matheusfroes.gamerguide.ui.gamelists

import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.SingleLiveEvent
import com.matheusfroes.gamerguide.data.model.GameList
import com.matheusfroes.gamerguide.data.source.local.GameListLocalSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GameListsViewModel @Inject constructor(private val gameListLocalSource: GameListLocalSource) : ViewModel() {
    val lists = SingleLiveEvent<List<GameList>>()

    init {
        getLists()
    }

    private fun getLists() {
        gameListLocalSource
                .getLists()
                .map { gameLists ->
                    gameLists.map { gameList ->
                        gameList.games = gameListLocalSource.gamesByList(gameList.id)

                    }
                    gameLists
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lists::postValue)
    }

    fun addList(listName: String) {
        gameListLocalSource.addList(GameList(name = listName))
    }

    fun listAlreadyAdded(listName: String): Boolean {
        return gameListLocalSource.listAlreadyAdded(listName)
    }
}