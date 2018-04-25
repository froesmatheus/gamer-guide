package com.matheusfroes.gamerguide.data.models

import java.io.Serializable

data class Plataforma(val id: Long, val nome: String) : Serializable {
    override fun toString() = nome
}