package com.matheusfroes.gamerguide.api

import com.matheusfroes.gamerguide.models.GameResponse
import com.matheusfroes.gamerguide.models.ObterStreamsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Created by matheusfroes on 26/09/2017.
 */
interface ApiService {
    companion object {
        val URL_BASE = "https://api-2445582011268.apicast.io/"
    }

    @Headers("user-key:81b44dc6a3b4284e6093dcea834aa49c", "Accept:application/json")
    @GET("games/")
    fun pesquisar(
            @Query("search") query: String,
            @Query("fields") fields: String = "name,id,summary,cover.url,game_engines,time_to_beat,developers,publishers,videos,first_release_date,genres,release_dates.date,release_dates.platform",
            @Query("expand") expands: String = "publishers,developers,genres,game_engines",
            @Query("order") popularity: String = "popularity:desc",
            @Query("scroll") scroll: String = "1"): Call<List<GameResponse>>

    @Headers("user-key:81b44dc6a3b4284e6093dcea834aa49c", "Accept:application/json")
    @GET
    fun proximaPagina(@Url scrollUrl: String): Call<List<GameResponse>>


    @GET("https://api.twitch.tv/kraken/streams")
    @Headers("Client-Id:8xydr1gey20rwhe79m5i328fitovuz")
    fun obterStreamsPorJogo(@Query("game") nomeJogo: String) : Call<ObterStreamsResponse>
}