package com.matheusfroes.gamerguide.ui.gamelists

import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.data.model.GameList
import com.matheusfroes.gamerguide.data.source.local.GameListLocalSource
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GameListsViewModel @Inject constructor(private val gameListLocalSource: GameListLocalSource) : ViewModel() {

    val gameLists: Flowable<List<GameList>>
        get() {
            return gameListLocalSource
                    .getLists()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }

    fun addList(listName: String) {
        gameListLocalSource.addList(GameList(name = listName))
    }

    fun listAlreadyAdded(listName: String): Boolean {
        return gameListLocalSource.listAlreadyAdded(listName)
    }
}