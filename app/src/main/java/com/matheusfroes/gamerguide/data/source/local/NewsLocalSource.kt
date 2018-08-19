package com.matheusfroes.gamerguide.data.source.local

import com.matheusfroes.gamerguide.data.GamerGuideDatabase
import com.matheusfroes.gamerguide.data.model.News
import com.matheusfroes.gamerguide.data.model.NewsSource
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
}