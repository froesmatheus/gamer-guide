package com.matheusfroes.gamerguide.data.source.remote

import com.matheusfroes.gamerguide.data.mapper.NewsMapper
import com.matheusfroes.gamerguide.data.model.News
import com.matheusfroes.gamerguide.network.RssService
import io.reactivex.Observable
import me.toptas.rssconverter.RssFeed
import javax.inject.Inject

class NewsRemoteSource @Inject constructor(
        private val rssService: RssService) {

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
}