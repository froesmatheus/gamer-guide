package com.matheusfroes.gamerguide.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.matheusfroes.gamerguide.data.model.News

@Dao
interface NewsDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(news: List<News>)

    @Query("SELECT * FROM news ORDER BY publishDate DESC")
    fun getNews(): List<News>

    @Query("SELECT publishDate FROM news ORDER BY publishDate DESC LIMIT 1")
    fun getMostRecentNewsPublishDate(): Long?
}