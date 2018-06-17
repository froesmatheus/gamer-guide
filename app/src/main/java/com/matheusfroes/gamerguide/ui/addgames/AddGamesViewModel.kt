package com.matheusfroes.gamerguide.ui.addgames

import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.SingleLiveEvent
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.source.remote.GameRemoteSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AddGamesViewModel(val source: GameRemoteSource) : ViewModel() {
    private var nextPageId = ""
    val games = SingleLiveEvent<List<Game>>()

    fun searchGames(query: String = "") {
        source.searchGames(query)
                .map {
                    nextPageId = it.second

                    it.first
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    games.postValue(it)
                }
    }
}
