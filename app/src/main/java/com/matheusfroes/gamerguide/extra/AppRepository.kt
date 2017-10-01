package com.matheusfroes.gamerguide.extra

import com.matheusfroes.gamerguide.api.IGDBService
import com.matheusfroes.gamerguide.models.GameResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by matheusfroes on 29/09/2017.
 */
class AppRepository {
    private val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(IGDBService.URL_BASE)
            .build()

    private val service: IGDBService by lazy {
        retrofit.create(IGDBService::class.java)
    }

    fun pesquisarJogos(query: String): MutableList<GameResponse> {
        val listaJogos = mutableListOf<GameResponse>()

        val call = service.pesquisarJogos(query = query)

        val response = call.execute()

        if (response.isSuccessful) {
            val listaResponse = response.body()

            listaResponse?.let { listaJogos.addAll(it) }
        }

        return listaJogos
    }

}