package com.matheusfroes.gamerguide.network.data

import com.matheusfroes.gamerguide.data.model.Platform
import java.util.*

data class ObterLancamentosResponse(
        val id: Int,
        val game: GameResponse,
        val platform: Long,
        val date: Long,
        val region: Int?)