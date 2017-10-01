package com.matheusfroes.gamerguide.models

import java.io.Serializable

/**
 * Created by matheusfroes on 26/09/2017.
 */
data class Plataforma(val id: Long, val nome: String) : Serializable {
    override fun toString() = nome
}