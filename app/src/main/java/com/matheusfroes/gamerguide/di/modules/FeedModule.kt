package com.matheusfroes.gamerguide.di.modules

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.matheusfroes.gamerguide.di.RssParserCoroutines
import com.matheusfroes.gamerguide.network.GameService
import com.matheusfroes.gamerguide.network.RssServiceCoroutines
import dagger.Module
import dagger.Provides
import me.toptas.rssconverter.RssConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class FeedModule {

    @Provides
    @RssParserCoroutines
    fun retrofit(
            okHttpClient: OkHttpClient,
            rssConverterFactory: RssConverterFactory,
            coroutineCallAdapterFactory: CoroutineCallAdapterFactory
    ): Retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(coroutineCallAdapterFactory)
            .addConverterFactory(rssConverterFactory)
            .baseUrl("http://www.google.com.br/")
            .build()

    @Provides
    fun feedService(@RssParserCoroutines retrofit: Retrofit): RssServiceCoroutines {
        return retrofit.create(RssServiceCoroutines::class.java)
    }
}
