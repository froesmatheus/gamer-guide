package com.matheusfroes.gamerguide.models

/**
 * Created by matheusfroes on 26/09/2017.
 */
data class Plataforma(val id: Long, val nome: String) {
    companion object {
        val ID_XBOX_360 = 12L
        val ID_XBOX_ONE = 49L
        val ID_PS3 = 9L
        val ID_PS4 = 48L
        val ID_PC = 6L
    }

}