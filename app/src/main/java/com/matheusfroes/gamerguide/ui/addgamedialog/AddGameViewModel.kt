package com.matheusfroes.gamerguide.ui.addgamedialog

import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.model.GameList
import com.matheusfroes.gamerguide.data.source.local.GameListLocalSource
import com.matheusfroes.gamerguide.data.source.local.GameLocalSource
import javax.inject.Inject

class AddGameViewModel @Inject constructor(
        private val gameListsLocalSource: GameListLocalSource,
        private val gameLocalSource: GameLocalSource
) : ViewModel() {

    fun getGameLists(): List<GameList> {
        return gameListsLocalSource.getLists().blockingFirst()
    }

    fun addGameToList(gameId: Long, listId: Long) {
        gameListsLocalSource.addGameToList(gameId, listId)
    }

    fun addGame(game: Game) {
        gameLocalSource.addGame(game)
    }
}