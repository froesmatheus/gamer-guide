package com.matheusfroes.gamerguide.data.source.local

import android.content.Context
import com.matheusfroes.gamerguide.data.GamerGuideDatabase
import com.matheusfroes.gamerguide.data.mapper.NewsMapper
import com.matheusfroes.gamerguide.data.model.News
import com.pkmmte.pkrss.Article
import com.pkmmte.pkrss.Callback
import com.pkmmte.pkrss.PkRSS
import timber.log.Timber
import javax.inject.Inject

class FeedLocalSource @Inject constructor(private val database: GamerGuideDatabase, val context: Context) {

    fun refreshFeed(): List<News> {
        val newsSources = database.newsSourcesDao().getNewsSourcesByStatus(enabled = true).map { it.website }

        return newsSources.map {
            PkRSS.with(context).load(it).skipCache().safe(true).callback(object : Callback {
                override fun onLoadFailed() {
                    Timber.d("onLoadFailed() $it")
                }

                override fun onPreload() {
                    Timber.d("onPreload() $it")
                }

                override fun onLoaded(newArticles: MutableList<Article>) {}
            }).get()
        }.map { NewsMapper.map(it) }
                .flatten()
                .sortedByDescending { it.publishDate }
    }
}