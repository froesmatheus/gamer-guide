package com.matheusfroes.gamerguide

import com.matheusfroes.gamerguide.network.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Injection {
    companion object {
        private val retrofit: Retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.URL_BASE)
                .build()

        val gameService: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }
    }
}