package com.matheusfroes.gamerguide

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by matheusfroes on 29/09/2017.
 */
fun Date.formatarData(formato: String): String =
        SimpleDateFormat(formato, Locale("pt", "BR")).format(this)