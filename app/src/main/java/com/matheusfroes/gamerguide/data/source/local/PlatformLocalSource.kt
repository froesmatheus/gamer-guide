package com.matheusfroes.gamerguide.data.source.local

import com.matheusfroes.gamerguide.data.GamerGuideDatabase
import com.matheusfroes.gamerguide.data.model.Platform
import javax.inject.Inject

class PlatformLocalSource @Inject constructor(private val database: GamerGuideDatabase) {

    fun getPlatforms(): List<Platform> {
        return database.platformsDao().getAll()
    }

    fun getPlatform(platformId: Long): Platform {
        return database.platformsDao().get(platformId)
    }
}