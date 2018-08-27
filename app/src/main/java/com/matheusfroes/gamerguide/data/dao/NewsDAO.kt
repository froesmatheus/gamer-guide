package com.matheusfroes.gamerguide.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.matheusfroes.gamerguide.data.model.News
import com.matheusfroes.gamerguide.data.model.NewsSource

@Dao
interface NewsDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(news: List<News>)

    @Query("SELECT * FROM news ORDER BY publishDate DESC")
    fun getNews(): List<News>

    @Query("SELECT publishDate FROM news ORDER BY publishDate DESC LIMIT 1")
    fun getMostRecentNewsPublishDate(): Long?

    @Query("SELECT * FROM news_sources")
    fun getNewsSources(): List<NewsSource>

    @Query("SELECT website FROM news_sources WHERE enabled = :enabled")
    fun getNewsSourcesByStatus(enabled: Boolean): List<String>

    @Query("UPDATE news_sources SET enabled = :enabled WHERE id = :sourceId")
    fun updateNewsSourceStatus(enabled: Boolean, sourceId: Int)

    @Query("SELECT * FROM news_sources WHERE lower(website) LIKE '%' || :website || '%'")
    fun getNewsSource(website: String): NewsSource?

    @Query("SELECT * FROM news_sources WHERE id = :sourceId")
    fun getNewsSource(sourceId: Int): NewsSource?

    @Query("DELETE FROM news WHERE sourceId = :sourceId")
    fun deleteNewsFromSource(sourceId: Int)
}