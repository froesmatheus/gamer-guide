package com.matheusfroes.gamerguide.data.mapper

import com.matheusfroes.gamerguide.data.dao.PlatformDAO
import com.matheusfroes.gamerguide.data.model.Platform
import com.matheusfroes.gamerguide.network.data.ReleaseDateResponse

class PlatformMapper {
    companion object {
        fun map(releaseDates: List<ReleaseDateResponse>?, platformsDAO: PlatformDAO): List<Platform> {
            return releaseDates
                    .orEmpty()
                    .map { platformsDAO.get(it.platform) }
                    .distinct()
                    .sortedBy { it.name.toLowerCase() }
        }
    }
}