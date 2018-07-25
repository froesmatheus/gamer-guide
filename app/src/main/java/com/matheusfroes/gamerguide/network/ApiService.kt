package com.matheusfroes.gamerguide.network

import com.matheusfroes.gamerguide.network.data.GameResponse
import com.matheusfroes.gamerguide.network.data.LomadeeResponse
import com.matheusfroes.gamerguide.network.data.ObterLancamentosResponse
import com.matheusfroes.gamerguide.network.data.ObterStreamsResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    companion object {
        val URL_BASE = "https://api-2445582011268.apicast.io/"
    }

    @Headers("user-key:81b44dc6a3b4284e6093dcea834aa49c", "Accept:application/json")
    @GET("games/")
    fun pesquisarJogos(
            @Query("search") query: String,
            @Query("fields") fields: String = "name,id,summary,cover.url,game_engines,time_to_beat,developers,publishers,videos,first_release_date,genres,release_dates.date,release_dates.platform",
            @Query("expand") expands: String = "publishers,developers,genres,game_engines",
            @Query("order") popularity: String = "popularity:desc",
            @Query("scroll") scroll: String = "1"): Call<List<GameResponse>>

    @Headers("user-key:81b44dc6a3b4284e6093dcea834aa49c", "Accept:application/json")
    @GET("games/")
    fun searchGames(
            @Query("search") query: String,
            @Query("fields") fields: String = "name,id,summary,cover.url,game_engines,time_to_beat,developers,publishers,videos,first_release_date,genres,release_dates.date,release_dates.platform",
            @Query("expand") expands: String = "publishers,developers,genres,game_engines",
            @Query("order") popularity: String = "popularity:desc",
            @Query("scroll") scroll: String = "1"): Observable<List<GameResponse>>


    @Headers("user-key:81b44dc6a3b4284e6093dcea834aa49c", "Accept:application/json")
    @GET
    fun proximaPaginaJogos(@Url scrollUrl: String): Call<List<GameResponse>>


    @GET("https://api.twitch.tv/kraken/streams")
    @Headers("Client-Id:8xydr1gey20rwhe79m5i328fitovuz")
    fun obterStreamsPorJogo(@Query("game") nomeJogo: String, @Query("limit") limit: Int = 10, @Query("offset") offset: Int = 0): Call<ObterStreamsResponse>


    @Headers("user-key:81b44dc6a3b4284e6093dcea834aa49c", "Accept:application/json")
    @GET("release_dates/")
    fun obterUltimosLancamentos(
            @Query("filter[date][gt]") dataLancamento: Long,
            @Query("fields") fields: String = "game.name,game.cover.url,date,platform,region",
            @Query("expand") expands: String = "game",
            @Query("order") order: String = "date:asc",
            @Query("scroll") scroll: String = "1"): Call<List<ObterLancamentosResponse>>

    @Headers("user-key:81b44dc6a3b4284e6093dcea834aa49c", "Accept:application/json")
    @GET
    fun proximaPaginaLancamentos(@Url scrollUrl: String): Call<List<ObterLancamentosResponse>>


    @GET("https://sandbox-api.lomadee.com/v2/15103313189856185110c/product/_search?sourceId=35877935&categoryId=6409")
    fun obterOfertasJogo(@Query("keyword") keyword: String): Call<LomadeeResponse>
}