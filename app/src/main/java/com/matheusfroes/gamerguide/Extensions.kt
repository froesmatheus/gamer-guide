package com.matheusfroes.gamerguide

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by matheusfroes on 29/09/2017.
 */
fun Date.dataPorExtenso(): String =
        SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("pt", "BR")).format(this)