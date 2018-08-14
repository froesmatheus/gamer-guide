package com.matheusfroes.gamerguide.data.mapper

import com.matheusfroes.gamerguide.data.model.Region
import com.matheusfroes.gamerguide.data.model.Release
import com.matheusfroes.gamerguide.network.data.ObterLancamentosResponse
import java.util.*
import javax.inject.Inject


class ReleaseMapper @Inject constructor(private val platformMapper: PlatformMapper) {

    fun map(releaseResponse: ObterLancamentosResponse): Release {
        return Release(
                game = GameReleaseMapper.map(releaseResponse.game),
                platform = platformMapper.map(releaseResponse.platform),
                date = Date(releaseResponse.date),
                region = Region.fromValue(releaseResponse.region)
        )
    }

    fun map(releaseResponses: List<ObterLancamentosResponse>): List<Release> {
        return releaseResponses.map { map(it) }
    }
}