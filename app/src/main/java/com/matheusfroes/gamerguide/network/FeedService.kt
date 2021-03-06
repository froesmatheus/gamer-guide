package com.matheusfroes.gamerguide.network

import kotlinx.coroutines.experimental.Deferred
import me.toptas.rssconverter.RssFeed
import retrofit2.http.GET
import retrofit2.http.Url


interface FeedService {
    /**
     * No baseUrl defined. Each RSS Feed will be consumed by it's Url
     * @param url RSS Feed Url
     * @return Retrofit Call
     */
    @GET
    fun fetchRSSFeed(@Url url: String): Deferred<RssFeed>
}