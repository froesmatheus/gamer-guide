package com.matheusfroes.gamerguide

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.matheusfroes.gamerguide.network.data.*
import java.util.*

fun adicionarSchemaUrl(url: String?): String {
    return if (url != null && url != "" && !url.startsWith("http", ignoreCase = true)) {
        "http:$url"
    } else url ?: ""
}

fun normalizarDadosLancamentos(lancamentosResponse: ObterLancamentosResponse, plataformasDAO: PlataformasDAO): Lancamento {
    return Lancamento(
            game = extrairJogoLancamento(lancamentosResponse.game),
            platform = plataformasDAO.obterPlataforma(lancamentosResponse.platform),
            date = Date(lancamentosResponse.date),
            region = extrairRegiao(lancamentosResponse.region)
    )
}

fun extrairRegiao(region: Int) = when (region) {
    1 -> REGIAO_EUROPA
    2 -> REGIAO_AMERICA_NORTE
    3 -> REGIAO_AUSTRALIA
    4 -> REGIAO_NOVA_ZELANDIA
    5 -> REGIAO_JAPAO
    6 -> REGIAO_CHINA
    7 -> REGIAO_ASIA
    8 -> REGIAO_MUNDIAL
    else -> REGIAO_MUNDIAL
}

fun extrairJogoLancamento(game: GameResponse): JogoLancamento = JogoLancamento(game.id, game.name!!, adicionarSchemaUrl(game.cover?.url))

fun obterImagemJogoCapa(urlImagem: String): String {
    val inicio = urlImagem.indexOf("t_")
    val fim = urlImagem.indexOf("/", inicio)

    val tamanhoImagem = urlImagem.substring(inicio, fim)

//    return urlImagem.replace(tamanhoImagem, "t_screenshot_big")
    return urlImagem.replace(tamanhoImagem, "t_720p")
}

fun esconderTeclado(activity: Activity) {
    val view = activity.currentFocus
    if (view != null) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}