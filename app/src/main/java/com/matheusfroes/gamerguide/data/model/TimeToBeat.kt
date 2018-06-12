package com.matheusfroes.gamerguide.data.model

import java.io.Serializable

data class TimeToBeat(
        val hastly: Long,
        val normally: Long,
        val completely: Long) : Serializable
