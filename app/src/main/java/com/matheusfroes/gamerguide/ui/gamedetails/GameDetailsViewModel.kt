package com.matheusfroes.gamerguide.ui.gamedetails

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.model.InsertType
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
    val currentAppTheme = MutableLiveData<String>()

    var gameId: Long = 0
        set(value) {
            field = value
            getGame(field)
        }

    private fun getGame(gameId: Long) {
        val game = gameLocalSource.getGame(gameId)
        this.game.postValue(game)
    }

    fun getGameByInsertType(gameId: Long, insertType: InsertType = InsertType.INSERT_BY_SEARCH): Game? {
        return gameLocalSource.getGamesByInsertType(gameId, insertType)
    }

    fun removeGame(gameId: Long) {
        gameLocalSource.deleteGame(gameId)
    }

    fun updateGame(game: Game) {
        gameLocalSource.updateGame(game)
    }

    fun addGame(game: Game) {
        gameLocalSource.addGame(game)
    }

    fun getLivestreamsByGame(offset: Int): Single<List<Stream>> {
        return gameRemoteSource.getLivestreamsByGame(game.value?.name ?: "", offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}