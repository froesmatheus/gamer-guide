package com.matheusfroes.gamerguide.data.source.remote

import com.matheusfroes.gamerguide.data.mapper.NewsMapper
import com.matheusfroes.gamerguide.data.model.News
import com.matheusfroes.gamerguide.network.RssService
import com.matheusfroes.gamerguide.network.RssServiceCoroutines
import com.matheusfroes.gamerguide.network.networkContext
import com.pkmmte.pkrss.PkRSS
import io.reactivex.Observable
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.withContext
import me.toptas.rssconverter.RssFeed
import javax.inject.Inject

class NewsRemoteSource @Inject constructor(
        private val rssService: RssService,
        private val feedService: RssServiceCoroutines,
        private val pkRSS: PkRSS) {

    fun getRssFeed(url: String): Observable<List<News>> {
        return rssService
                .getRssFeed(url)
                .map(RssFeed::getItems)
                .map { rssFeed -> NewsMapper.map2(rssFeed) }
    }

    suspend fun fetchRssFeed(url: String): List<News> = withContext(networkContext) {
        val feedResponse = feedService.fetchRSSFeed(url).await()
        NewsMapper.map2(feedResponse.items)
    }

    suspend fun fetchFeed(url: String): List<News> = withContext(networkContext) {
        val feed = pkRSS.load(url).skipCache().callback(null).get()

        NewsMapper.map(feed)
    }


}