package com.matheusfroes.gamerguide.data.mapper

import com.matheusfroes.gamerguide.data.model.Platform
import com.matheusfroes.gamerguide.data.source.local.PlatformLocalSource
import com.matheusfroes.gamerguide.network.data.ReleaseDateResponse
import javax.inject.Inject

class PlatformMapper @Inject constructor(private val platformLocalSource: PlatformLocalSource) {

    fun map(releaseDates: List<ReleaseDateResponse>?): List<Platform> {
        return releaseDates
                .orEmpty()
                .map { platformLocalSource.getPlatform(it.platform) }
                .distinct()
                .sortedBy { it.name.toLowerCase() }
    }
}