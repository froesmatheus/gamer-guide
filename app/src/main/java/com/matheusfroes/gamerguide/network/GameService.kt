package com.matheusfroes.gamerguide.network

import com.matheusfroes.gamerguide.network.data.GameResponse
import com.matheusfroes.gamerguide.network.data.ObterLancamentosResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Url

interface GameService {
    companion object {
        const val BASE_URL = "https://api-2445582011268.apicast.io/"
    }

    @Headers("user-key:81b44dc6a3b4284e6093dcea834aa49c", "Accept:application/json")
    @GET("games/")
    fun searchGames(
            @Query("search") query: String,
            @Query("fields") fields: String = "name,id,summary,cover.url,game_engines,time_to_beat,developers,publishers,videos,first_release_date,genres,release_dates.date,release_dates.platform",
            @Query("expand") expands: String = "publishers,developers,genres,game_engines",
            @Query("order") popularity: String = "popularity:desc",
            @Query("scroll") scroll: String = "1"): Observable<Response<List<GameResponse>>>


    @Headers("user-key:81b44dc6a3b4284e6093dcea834aa49c", "Accept:application/json")
    @GET
    fun searchGamesNextPage(@Url scrollUrl: String): Observable<Response<List<GameResponse>>>

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
}