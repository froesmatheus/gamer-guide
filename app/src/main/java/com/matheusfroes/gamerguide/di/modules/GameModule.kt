package com.matheusfroes.gamerguide.di.modules

import com.matheusfroes.gamerguide.network.GameService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class GameModule {

    @Provides
    fun retrofit(
            okHttpClient: OkHttpClient,
            rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
            gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(GameService.BASE_URL)
            .build()

    @Provides
    fun gameService(retrofit: Retrofit): GameService {
        return retrofit.create(GameService::class.java)
    }


}
