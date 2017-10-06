package com.matheusfroes.gamerguide.models

import java.io.Serializable

/**
 * Created by matheusfroes on 05/10/2017.
 */
data class ProgressoJogo(val horasJogadas: Int, val progressoPerc: Int, val zerado: Boolean) : Serializable