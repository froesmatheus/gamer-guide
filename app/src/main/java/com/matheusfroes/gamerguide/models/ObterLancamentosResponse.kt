package com.matheusfroes.gamerguide.models

import java.util.*

/**
 * Created by matheus_froes on 24/10/2017.
 */
data class ObterLancamentosResponse(
        val id: Int,
        val game: GameResponse,
        val platform: Long,
        val date: Long,
        val region: Int)


data class Lancamento(
        val game: JogoLancamento,
        val platform: Plataforma,
        val date: Date,
        val region: String
)