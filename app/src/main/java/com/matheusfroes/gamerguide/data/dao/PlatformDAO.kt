package com.matheusfroes.gamerguide.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.matheusfroes.gamerguide.data.model.Platform

@Dao
interface PlatformDAO {

    @Query("SELECT * FROM platforms WHERE id = :platformId")
    fun get(platformId: Long): Platform

    @Query("SELECT * FROM platforms")
    fun getAll(): List<Platform>

    @Query("SELECT p.* FROM platforms P INNER JOIN games_platforms gp ON p.id = gp.platformId WHERE gp.gameId = :gameId")
    fun getPlatformsByGame(gameId: Long): List<Platform>
}