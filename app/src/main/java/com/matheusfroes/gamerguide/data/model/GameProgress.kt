package com.matheusfroes.gamerguide.data.model

import java.io.Serializable

data class GameProgress(
        var hoursPlayed: Int,
        var percentage: Int,
        var beaten: Boolean) : Serializable