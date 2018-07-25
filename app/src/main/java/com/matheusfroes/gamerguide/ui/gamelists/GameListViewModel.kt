package com.matheusfroes.gamerguide.ui.gamelists

import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.data.model.GameList
import com.matheusfroes.gamerguide.data.source.local.GameListLocalSource
import io.reactivex.Flowable
import javax.inject.Inject

class GameListViewModel @Inject constructor(private val gameListLocalSource: GameListLocalSource) : ViewModel() {

    fun getLists(): Flowable<List<GameList>> {
        return gameListLocalSource.getLists()
    }

    fun addList(gameList: GameList) {
        gameListLocalSource.addList(gameList)
    }

    fun listAlreadyAdded(listName: String): Boolean {
        return gameListLocalSource.listAlreadyAdded(listName)
    }

}