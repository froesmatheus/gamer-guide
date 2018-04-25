package com.matheusfroes.gamerguide.data.models

val REGIAO_EUROPA = "Europa"
val REGIAO_AMERICA_NORTE = "América do Norte"
val REGIAO_AUSTRALIA = "Austrália"
val REGIAO_NOVA_ZELANDIA = "Nova Zelândia"
val REGIAO_JAPAO = "Japão"
val REGIAO_CHINA = "China"
val REGIAO_ASIA = "Ásia"
val REGIAO_MUNDIAL = "Mundial"

data class JogoLancamento(
        val id: Long,
        val nome: String,
        val cover: String
)