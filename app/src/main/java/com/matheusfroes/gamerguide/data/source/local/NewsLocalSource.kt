package com.matheusfroes.gamerguide.data.source.local

import com.matheusfroes.gamerguide.data.GamerGuideDatabase
import com.matheusfroes.gamerguide.data.model.News
import com.matheusfroes.gamerguide.data.model.NewsSource
import com.matheusfroes.gamerguide.network.ioContext
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject

class NewsLocalSource @Inject constructor(private val database: GamerGuideDatabase) {

    fun getNewsSources(): List<NewsSource> {
        return database.newsDao().getNewsSources()
    }

    fun updateNewsSourceStatus(enabled: Boolean, sourceId: Int) {
        database.newsDao().updateNewsSourceStatus(enabled, sourceId)
    }

    fun saveNews(news: List<News>) {
        database.newsDao().insert(news)
    }

    suspend fun getNewsSourcesByStatusCO(enabled: Boolean): List<String> = withContext(ioContext) {
        database.newsDao().getNewsSourcesByStatus(enabled)
    }

    suspend fun getNewsCO(): List<News> = withContext(ioContext) {
        database.newsDao().getNews()
    }

    suspend fun getMostRecentNewsPublishDate(): Long = withContext(ioContext) {
        database.newsDao().getMostRecentNewsPublishDate() ?: 0
    }
}