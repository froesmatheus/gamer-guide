package com.matheusfroes.gamerguide.network.data

data class GenreResponse(val id: Int, val name: String) {
    override fun toString() = name
}