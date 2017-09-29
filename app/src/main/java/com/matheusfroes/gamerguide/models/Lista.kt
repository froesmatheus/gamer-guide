package com.matheusfroes.gamerguide.models

/**
 * Created by matheus_froes on 21/09/2017.
 */
data class Lista(
        val id: Int = 0,
        val nome: String,
        val jogos: List<Jogo> = listOf()) {

    override fun toString() = nome
}