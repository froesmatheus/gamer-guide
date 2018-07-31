package com.matheusfroes.gamerguide.data

import android.arch.persistence.room.Embedded
import com.matheusfroes.gamerguide.data.model.Game
import com.matheusfroes.gamerguide.data.model.Platform

data class GameWithPlatforms(
        @Embedded
        val game: Game,
        val platforms: List<Platform>
)