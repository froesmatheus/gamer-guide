package com.matheusfroes.gamerguide.data.mapper

import com.matheusfroes.gamerguide.extra.adicionarSchemaUrl
import com.matheusfroes.gamerguide.data.model.GameRelease
import com.matheusfroes.gamerguide.network.data.GameResponse

class GameReleaseMapper {

    companion object {
        fun map(gameResponse: GameResponse): GameRelease {
            return GameRelease(id = gameResponse.id, name = gameResponse.name
                    ?: "", cover = adicionarSchemaUrl(gameResponse.cover?.url))
        }
    }

}