package com.matheusfroes.gamerguide.data.source.local

import com.matheusfroes.gamerguide.data.GamerGuideDatabase
import com.matheusfroes.gamerguide.data.model.News
import com.matheusfroes.gamerguide.data.model.NewsSource
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject

class NewsLocalSource @Inject constructor(private val database: GamerGuideDatabase) {

    fun getNewsSources(): List<NewsSource> {
        return database.newsSourcesDao().getAll()
    }

    fun getNewsSourcesByStatus(enabled: Boolean): List<String> {
        return database.newsSourcesDao().getNewsSourcesByStatus(enabled)
    }

    fun updateNewsSourceStatus(enabled: Boolean, sourceId: Int) {
        database.newsSourcesDao().updateNewsSourceStatus(enabled, sourceId)
    }

    fun saveNews(news: List<News>) {
        database.newsDao().insert(news)
    }

    fun getNews(): List<News> {
        return database.newsDao().getNews()
    }

    suspend fun getNewsSourcesByStatusCO(enabled: Boolean): List<String> {
        return withContext(DefaultDispatcher) {
            database.newsSourcesDao().getNewsSourcesByStatus(enabled)
        }
    }

    suspend fun getNewsCO(): List<News> {
        return withContext(DefaultDispatcher) {
            database.newsDao().getNews()
        }
    }
}