package com.matheusfroes.gamerguide.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.matheusfroes.gamerguide.data.model.NewsSource

@Dao
interface NewsSourceDAO {

    @Query("SELECT * FROM news_sources")
    fun getAll(): List<NewsSource>

    @Query("SELECT website FROM news_sources WHERE enabled = :enabled")
    fun getNewsSourcesByStatus(enabled: Boolean): List<String>

    @Query("UPDATE news_sources SET enabled = :enabled WHERE id = :sourceId")
    fun updateNewsSourceStatus(enabled: Boolean, sourceId: Int)
}