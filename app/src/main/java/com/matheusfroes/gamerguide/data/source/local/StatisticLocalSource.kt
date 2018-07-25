package com.matheusfroes.gamerguide.data.source.local

import com.matheusfroes.gamerguide.data.GamerGuideDatabase
import javax.inject.Inject

class StatisticLocalSource @Inject constructor(private val database: GamerGuideDatabase) {

    fun getGameCountByProgressStatus(beaten: Boolean): Int {
        return database.gamesDao().getGameCountByProgressStatus(beaten)
    }

    fun getTotalHoursPlayed(): Int {
        return database.gamesDao().getTotalHoursPlayed()
    }

    fun getMostPlayedGenres(): List<Pair<String, Int>> {
        val genres = database.gamesDao()
                .getGenres()
                .flatMap { it.split(",") }


        val uniqueGenres = mutableSetOf<Pair<String, Int>>()

        genres.forEach { genre ->
            val count = genres.count { it == genre }
            if (count > 0) {
                uniqueGenres.add(Pair(genre, count))
            }
        }

        return uniqueGenres.sortedByDescending { it.second }.take(5)
    }
}