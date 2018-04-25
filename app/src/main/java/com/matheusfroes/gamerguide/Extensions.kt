package com.matheusfroes.gamerguide

import java.text.SimpleDateFormat
import java.util.*

fun Date.formatarData(formato: String): String =
        SimpleDateFormat(formato, Locale("pt", "BR")).format(this)