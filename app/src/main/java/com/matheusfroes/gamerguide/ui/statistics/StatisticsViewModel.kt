package com.matheusfroes.gamerguide.ui.statistics

import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.data.source.local.GameLocalSource
import javax.inject.Inject

class StatisticsViewModel @Inject constructor(
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