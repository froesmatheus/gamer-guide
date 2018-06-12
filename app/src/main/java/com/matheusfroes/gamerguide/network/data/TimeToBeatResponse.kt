package com.matheusfroes.gamerguide.network.data

import java.io.Serializable

data class TimeToBeatResponse(val hastly: Long, val normally: Long, val completely: Long) : Serializable
