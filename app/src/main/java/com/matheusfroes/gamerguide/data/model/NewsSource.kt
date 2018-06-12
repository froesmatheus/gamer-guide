package com.matheusfroes.gamerguide.data.model

data class NewsSource(
        val id: Int = 0,
        val name: String,
        val website: String,
        var enabled: Boolean = true)