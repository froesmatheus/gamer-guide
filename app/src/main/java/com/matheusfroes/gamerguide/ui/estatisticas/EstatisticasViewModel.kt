package com.matheusfroes.gamerguide.ui.estatisticas

import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.data.source.local.GameLocalSource
import javax.inject.Inject

class EstatisticasViewModel @Inject constructor(
        private val gameLocalSource: GameLocalSource
) : ViewModel() {


    fun getTotalHoursPlayed(): Int {
        return gameLocalSource.getTotalHoursPlayed()
    }

    fun getGameCount(beaten: Boolean): Int {
        return gameLocalSource.getGameCount(beaten)
    }

    fun getMostPlayedGenres(): List<Pair<String, Int>> {
        return gameLocalSource.getMostPlayedGenres()
    }
}