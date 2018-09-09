package com.matheusfroes.gamerguide.network

import com.matheusfroes.gamerguide.network.data.GameResponse
import com.matheusfroes.gamerguide.network.data.ObterLancamentosResponse
import com.matheusfroes.gamerguide.network.data.ObterStreamsResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface GameService {
    companion object {
        const val BASE_URL = "https://api-endpoint.igdb.com/"
    }

    @Headers("Accept:application/json")
    @GET("games/")
    fun searchGames(
            @Query("search") query: String,
            @Query("fields") fields: String = "name,id,summary,cover.url,game_engines,time_to_beat,developers,publishers,videos,first_release_date,genres,release_dates.date,release_dates.platform",
            @Query("expand") expands: String = "publishers,developers,genres,game_engines",
            @Query("order") popularity: String = "popularity:desc",
            @Query("scroll") scroll: String = "1",
            @Header("user-key") apiKey: String): Observable<Response<List<GameResponse>>>


    @Headers("Accept:application/json")
    @GET
    fun searchGamesNextPage(
            @Url scrollUrl: String,
            @Header("user-key") apiKey: String): Observable<Response<List<GameResponse>>>

    @Headers("Accept:application/json")
    @GET("release_dates/")
    fun getGameReleases(
            @Query("filter[date][gt]") dataLancamento: Long,
            @Query("fields") fields: String = "game.name,game.cover.url,date,platform,region",
            @Query("expand") expands: String = "game",
            @Query("order") order: String = "date:asc",
            @Query("scroll") scroll: String = "1",
            @Header("user-key") apiKey: String): Observable<Response<List<ObterLancamentosResponse>>>

    @Headers("Accept:application/json")
    @GET
    fun getGamesReleasesNextPage(
            @Url scrollUrl: String,
            @Header("user-key") apiKey: String): Observable<Response<List<ObterLancamentosResponse>>>

    @GET("https://api.twitch.tv/kraken/streams")
    fun getLivestreamsByGame(
            @Query("game") gameName: String,
            @Query("limit") limit: Int = 10,
            @Query("offset") offset: Int = 0,
            @Header("Client-Id") apiKey: String): Single<ObterStreamsResponse>
}