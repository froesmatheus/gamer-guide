package com.matheusfroes.gamerguide.data.source.remote

import com.matheusfroes.gamerguide.data.mapper.NewsMapper
import com.matheusfroes.gamerguide.data.model.News
import com.matheusfroes.gamerguide.network.networkContext
import com.pkmmte.pkrss.PkRSS
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject

class NewsRemoteSource @Inject constructor(
        private val pkRSS: PkRSS) {

    suspend fun fetchFeed(url: String): List<News> = withContext(networkContext) {
        val feed = pkRSS
                .load(url)
                .skipCache()
                .callback(null)
                .get()

        NewsMapper.map(feed)
    }
}