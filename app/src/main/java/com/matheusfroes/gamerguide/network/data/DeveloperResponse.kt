package com.matheusfroes.gamerguide.network.data

data class DeveloperResponse(val id: Int, val name: String) {
    override fun toString() = name
}