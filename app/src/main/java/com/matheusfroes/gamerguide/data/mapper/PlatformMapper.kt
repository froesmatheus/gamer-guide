package com.matheusfroes.gamerguide.data.mapper

import com.matheusfroes.gamerguide.data.db.PlataformasDAO
import com.matheusfroes.gamerguide.data.model.Platform
import com.matheusfroes.gamerguide.network.data.ReleaseDateResponse

class PlatformMapper {
    companion object {
        fun map(releaseDates: List<ReleaseDateResponse>?, plataformasDAO: PlataformasDAO): List<Platform> {
            return releaseDates
                    .orEmpty()
                    .map { plataformasDAO.obterPlataforma(it.platform) }
                    .distinct()
                    .sortedBy { it.name.toLowerCase() }
        }
    }
}