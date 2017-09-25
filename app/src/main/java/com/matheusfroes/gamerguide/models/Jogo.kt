package com.matheusfroes.gamerguide.models

import java.util.*

/**
 * Created by matheusfroes on 23/09/2017.
 */
data class Jogo(
        val nome: String,
        val descricao: String,
        val publicadora: String,
        val produtora: String,
        val genero: String,
        val dataLancamento: Date
)