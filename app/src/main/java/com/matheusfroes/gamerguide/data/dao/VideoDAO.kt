package com.matheusfroes.gamerguide.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.matheusfroes.gamerguide.data.model.Video

@Dao
interface VideoDAO {

    @Insert
    fun insert(videos: List<Video>)

    @Query("SELECT * FROM videos WHERE gameId = :gameId")
    fun getVideosByGame(gameId: Long): List<Video>
}