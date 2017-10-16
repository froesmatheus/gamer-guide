package com.matheusfroes.gamerguide.models

import java.io.Serializable
import java.util.*

/**
 * Created by matheusfroes on 23/09/2017.
 */
data class Jogo(
        val id: Long,
        val nome: String,
        val descricao: String,
        val desenvolvedores: String,
        val publicadoras: String,
        val generos: String,
        val dataLancamento: Date,
        val plataformas: List<Plataforma>,
        val videos: List<Video>,
        val gameEngine: String,
        val timeToBeat: TimeToBeat? = null,
        val imageCapa: String,
        val progresso: ProgressoJogo = ProgressoJogo(0, 0, false)) : Serializable