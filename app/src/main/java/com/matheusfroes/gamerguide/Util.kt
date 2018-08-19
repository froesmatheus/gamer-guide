package com.matheusfroes.gamerguide

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun adicionarSchemaUrl(url: String?): String {
    return if (url != null && url != "" && !url.startsWith("http", ignoreCase = true)) {
        "http:" + url
    } else url ?: ""
}

fun obterImagemJogoCapa(urlImagem: String): String {
    val inicio = urlImagem.indexOf("t_")
    val fim = urlImagem.indexOf("/", inicio)

    val tamanhoImagem = urlImagem.substring(inicio, fim)

    return urlImagem.replace(tamanhoImagem, "t_720p")
}

fun esconderTeclado(activity: Activity) {
    val view = activity.currentFocus
    if (view != null) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}