package com.matheusfroes.gamerguide.models

/**
 * Created by matheus_froes on 24/10/2017.
 */
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
        val nome: String
)