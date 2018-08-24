package com.matheusfroes.gamerguide.ui.gamedetails

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.source.local.GameLocalSource
import com.matheusfroes.gamerguide.data.source.remote.GameRemoteSource
import com.matheusfroes.gamerguide.network.data.Stream
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GameDetailsViewModel @Inject constructor(
        private val gameLocalSource: GameLocalSource,
        private val gameRemoteSource: GameRemoteSource) : ViewModel() {
    val game = MutableLiveData<Game>()

    var gameId: Long = 0
        set(value) {
            field = value
            val game = getGame(field)
            this.game.postValue(game)
        }

    fun getGame(gameId: Long): Game? {
        return gameLocalSource.getGame(gameId)
    }

    fun getLivestreamsByGame(offset: Int): Single<List<Stream>> {
        return gameRemoteSource.getLivestreamsByGame(game.value?.name ?: "", offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}