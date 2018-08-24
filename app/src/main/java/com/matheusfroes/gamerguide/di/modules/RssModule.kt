package com.matheusfroes.gamerguide.di.modules

import com.matheusfroes.gamerguide.di.RssParser
import com.matheusfroes.gamerguide.network.RssService
import dagger.Module
import dagger.Provides
import me.toptas.rssconverter.RssConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

@Module
class RssModule {

    @Provides
    @RssParser
    fun retrofit(
            okHttpClient: OkHttpClient,
            rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
            rssConverterFactory: RssConverterFactory
    ): Retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .addConverterFactory(rssConverterFactory)
            .baseUrl("https://github.com") // RssConverterFactory não precisa de baseUrl
            .build()

    @Provides
    fun rssService(@RssParser retrofit: Retrofit): RssService {
        return retrofit.create(RssService::class.java)
    }
}
