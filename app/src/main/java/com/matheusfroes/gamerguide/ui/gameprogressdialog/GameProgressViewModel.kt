package com.matheusfroes.gamerguide.ui.gameprogressdialog

import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.source.local.GameLocalSource
import javax.inject.Inject

class GameProgressViewModel @Inject constructor(
        private val gameLocalSource: GameLocalSource
) : ViewModel() {

    fun updateGameProgress(game: Game) {
        gameLocalSource.updateGame(game)
    }
}