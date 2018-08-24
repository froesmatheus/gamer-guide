package com.matheusfroes.gamerguide.ui.removegamedialog

import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.model.InsertType
import com.matheusfroes.gamerguide.data.source.local.GameListLocalSource
import com.matheusfroes.gamerguide.data.source.local.GameLocalSource
import javax.inject.Inject

class RemoveGameViewModel @Inject constructor(
        private val gameListLocalSource: GameListLocalSource,
        private val gameLocalSource: GameLocalSource
) : ViewModel() {

    fun gameIsInGameLists(gameId: Long): Boolean {
        return gameListLocalSource.gameIsInGameLists(gameId)
    }

    fun getGameByInsertType(gameId: Long, insertType: InsertType = InsertType.INSERT_BY_SEARCH): Game? {
        return gameLocalSource.getGamesByInsertType(gameId, insertType)
    }

    fun removeGameFromLists(gameId: Long) {
        gameListLocalSource.deleteGameFromLists(gameId)
    }

    fun removeGame(gameId: Long) {
        gameLocalSource.deleteGame(gameId)
    }

    fun updateGame(game: Game) {
        gameLocalSource.updateGame(game)
    }
}