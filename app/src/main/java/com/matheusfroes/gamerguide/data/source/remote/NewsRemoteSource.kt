package com.matheusfroes.gamerguide.data.source.remote

import com.matheusfroes.gamerguide.data.mapper.NewsMapper
import com.matheusfroes.gamerguide.data.model.News
import com.matheusfroes.gamerguide.network.RssService
import com.matheusfroes.gamerguide.network.RssServiceCoroutines
import io.reactivex.Observable
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.withContext
import me.toptas.rssconverter.RssFeed
import javax.inject.Inject

class NewsRemoteSource @Inject constructor(
        private val rssService: RssService,
        private val feedService: RssServiceCoroutines) {

    fun getRssFeed(url: String): Observable<List<News>> {
        return rssService
                .getRssFeed(url)
                .map(RssFeed::getItems)
                .map { rssFeed -> NewsMapper.map2(rssFeed) }
    }

    fun getRssFeeds(urls: List<String>): Observable<List<News>> {
        return Observable.fromArray(urls)
                .flatMapIterable { it }
                .flatMap { rssService.getRssFeed(it) }
                .map(RssFeed::getItems)
                .map { rssFeed -> NewsMapper.map2(rssFeed) }
    }


    suspend fun fetchRssFeed(url: String): List<News> {
        return withContext(DefaultDispatcher) {
            val feedResponse = feedService.fetchRSSFeed(url).await()
            NewsMapper.map2(feedResponse.items)
        }
    }


}