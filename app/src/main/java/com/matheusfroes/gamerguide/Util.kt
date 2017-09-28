package com.matheusfroes.gamerguide

import com.matheusfroes.gamerguide.models.GameResponse
import com.matheusfroes.gamerguide.models.Jogo
import com.matheusfroes.gamerguide.models.Plataforma
import com.matheusfroes.gamerguide.models.ReleaseDate
import java.util.*

/**
 * Created by matheus_froes on 27/09/2017.
 */
fun adicionarSchemaUrl(url: String?): String {
    return if (url != null && !url.startsWith("http", ignoreCase = true)) {
        "http:" + url
    } else url ?: ""
}

fun normalizarDadosJogo(game: GameResponse): Jogo =
        Jogo(
                game.id,
                game.name ?: "",
                game.summary ?: "",
                game.publishers?.joinToString() ?: "",
                game.developers?.joinToString() ?: "",
                game.genres?.joinToString() ?: "",
                Date(game.firstReleaseDate),
                extrairPlataformas(game.releaseDates ?: mutableListOf()),
                adicionarSchemaUrl(game.cover?.url))

fun extrairPlataformas(releaseDates: List<ReleaseDate>): List<Plataforma> {
    return releaseDates.map {
        when (it.platform) {
            Plataforma.ID_XBOX_360 -> Plataforma(Plataforma.ID_XBOX_360, "Xbox 360")
            Plataforma.ID_XBOX_ONE -> Plataforma(Plataforma.ID_XBOX_ONE, "Xbox One")
            Plataforma.ID_PS3 -> Plataforma(Plataforma.ID_PS3, "PS3")
            Plataforma.ID_PS4 -> Plataforma(Plataforma.ID_PS4, "PS4")
            Plataforma.ID_PC -> Plataforma(Plataforma.ID_PC, "PC")
            else -> Plataforma(0, "")
        }
    }.distinct().filter { it.id != 0.toLong() }
}
