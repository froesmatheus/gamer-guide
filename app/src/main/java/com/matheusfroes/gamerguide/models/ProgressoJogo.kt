package com.matheusfroes.gamerguide.models

import java.io.Serializable

/**
 * Created by matheusfroes on 05/10/2017.
 */
data class ProgressoJogo(var horasJogadas: Int, var progressoPerc: Int, var zerado: Boolean) : Serializable