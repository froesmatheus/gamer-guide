package com.matheusfroes.gamerguide.ui.statistics

import android.arch.lifecycle.ViewModel
import com.matheusfroes.gamerguide.data.source.local.StatisticLocalSource
import javax.inject.Inject

class StatisticsViewModel @Inject constructor(private val source: StatisticLocalSource) : ViewModel() {

    fun getGameCountByProgressStatus(beaten: Boolean): Int {
        return source.getGameCountByProgressStatus(beaten)
    }

    fun getTotalHoursPlayed(): Int {
        return source.getTotalHoursPlayed()
    }

    fun getMostPlayedGenres(): List<Pair<String, Int>> {
        return source.getMostPlayedGenres()
    }
}