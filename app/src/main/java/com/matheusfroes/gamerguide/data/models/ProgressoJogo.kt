package com.matheusfroes.gamerguide.data.models

import java.io.Serializable

data class ProgressoJogo(var horasJogadas: Int, var progressoPerc: Int, var zerado: Boolean) : Serializable