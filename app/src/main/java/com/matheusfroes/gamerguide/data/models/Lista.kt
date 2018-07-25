package com.matheusfroes.gamerguide.data.models

data class Lista(
        val id: Int = 0,
        var nome: String,
        val jogos: List<Jogo> = listOf()) {

    override fun toString() = nome
}