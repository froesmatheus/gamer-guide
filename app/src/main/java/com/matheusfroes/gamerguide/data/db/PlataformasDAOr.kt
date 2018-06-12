package com.matheusfroes.gamerguide.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.matheusfroes.gamerguide.data.model.Platform

@Dao
interface PlataformasDAOr {

    @Query("SELECT * FROM platforms WHERE id = :platformId")
    fun get(platformId: Long): Platform

    @Query("SELECT * FROM platforms")
    fun getAll(): List<Platform>
//
//    @Query("SELECT p.* FROM platforms p INNER JOIN games g ON p.id")
//    fun getPlatformsByGame(gameId: Long)
}